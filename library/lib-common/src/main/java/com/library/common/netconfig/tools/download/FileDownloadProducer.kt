package com.library.common.netconfig.tools.download

import android.content.Context
import android.util.Log
import com.jakewharton.rxrelay3.PublishRelay
import com.library.common.commonutils.SPUtils
import com.library.common.netconfig.LoggerHttpLogPrinterImpl
import com.library.common.netconfig.constant.AppConstant
import com.yupfeg.executor.ExecutorProvider
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.config.HttpRequestConfig
import com.yupfeg.remote.download.BaseFileDownloadProducer
import com.yupfeg.remote.download.entity.DownloadProgressBean
import com.yupfeg.remote.interceptor.DownloadProgressInterceptor
import com.yupfeg.remote.log.HttpLogPrinter
import com.library.common.netconfig.tools.remote.DownloadApiService
import com.yupfeg.remote.download.DownloadListener
/*coroutines*/
import okhttp3.Interceptor
import okhttp3.ResponseBody
/*rxjava*/
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * 基于RxJava3+Retrofit实现下载文件功能的数据源提供类
 * 使用[BehaviorSubject]向外部发送下载进度变化，fileUrl作为文件唯一标识符，用于区分下载进度所属文件
 * @author 王凯
 * @date 2022/07/20
 */
class FileDownloadProducer(
    /*连接池的key*/
    private val requestTag: String = HttpRequestMediator.DEFAULT_DOWNLOAD_CLIENT_KEY,
    /*网络请求相关配置*/
    private val requestConfig: HttpRequestConfig
) : BaseFileDownloadProducer() {

    companion object {
        /*日志TAG*/
        const val TAG = "FILE_DOWNLOAD_TAG"
    }

    /*下载路径*/
    var path = ""
    var listenerMap: MutableMap<String, DownloadListener> = mutableMapOf()

    /*协程job集合*/
    var jobList: MutableList<Job> = mutableListOf()

    /*一、添加网络请求的工厂*/
    init {
        HttpRequestMediator.addDefaultHttpClientFactory(requestTag) {
            this.baseUrl = requestConfig.baseUrl
            connectTimeout = requestConfig.connectTimeout
            readTimeout = requestConfig.readTimeout
            writeTimeout = requestConfig.writeTimeout
            isAllowProxy = requestConfig.isAllowProxy
            applicationInterceptors = requestConfig.applicationInterceptors
            networkInterceptors = mutableListOf<Interceptor>().apply {
                addAll(requestConfig.networkInterceptors)
                //添加下载进度监听的拦截器
                add(createDownloadInterceptor(LoggerHttpLogPrinterImpl()))
            }
            //支持RxJava3(create()方法创建的是采用okHttp内置的线程池，下游数据流使用subscribeOn无效
            // createSynchronous()则使用下游subscribeOn提供的线程池)
            callAdapterFactories = mutableListOf(RxJava3CallAdapterFactory.createSynchronous())
        }
    }

    private fun createDownloadInterceptor(logPrinter: HttpLogPrinter? = null): Interceptor {
        return DownloadProgressInterceptor(
            logPrinter = logPrinter,
        ) { progressBean ->
            //此时处于子线程，不能直接回调执行UI操作
            sendDownloadProgressChange(progressBean)
        }
    }

    /**
     * 二、从工厂中获取下载的Retrofit实例创建
     */
    private val mDownloadApiService: DownloadApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        HttpRequestMediator.createRequestApi(
            requestTag, DownloadApiService::class.java
        )
    }

    //TODO:--------------------------------------协程方式下载-----------------------------------------

    /**
     * 下载监听器
     */
    private var downloadListener: DownloadListener? = null

    /**
     * 开始下载方法
     */
    private fun downloadFile(fileUrl: String, listener: DownloadListener): Flow<ResponseBody> =
        flow {
            delay(500)
            downloadListener = listener
            listener.onStartDownload()
            val download = mDownloadApiService.download(fileUrl)
            emit(download)
        }

    suspend fun <T> Flow<T>.next(bloc: suspend T.(T) -> Unit): Unit = collect { bloc(it, it) }

    suspend fun load(context: Context, bean: Bean, coroutine: CoroutineContext) {
        withContext(coroutine) {
            val listener = object : DownloadListener {
                var indexProgress = 0
                var total = 0L
                var isPauseState = false
                var isCancelState = false
                var isResumeState = bean.isResume
                var filePointerLength = 0L
                var mFileTotalSize = 0L
                override var isPause: Boolean
                    get() = isPauseState
                    set(value) {
                        isPauseState = value
                    }
                override var isCancel: Boolean
                    get() = isCancelState
                    set(value) {
                        isCancelState = value
                    }
                override var isResume: Boolean
                    get() = isResumeState
                    set(value) {
                        isResumeState = value
                    }
                override var filePointer: Long
                    get() = filePointerLength
                    set(value) {
                        filePointerLength = value
                    }
                override var fileTotalSize: Long
                    get() = mFileTotalSize
                    set(value) {
                        mFileTotalSize = value
                    }

                override fun onStartDownload() {
                    filePointerLength =
                        SPUtils[context, AppConstant.FILE_POINTER + bean.path, 0L] as Long
                    mFileTotalSize =
                        SPUtils[context, AppConstant.FILE_TOTAL + bean.path, 0L] as Long
                }

                override fun onProgress(progress: Int, totalLength: Long) {
                    bean.progress = progress
                    indexProgress = progress
                    total = totalLength
                    Log.d(TAG, "progress:$progress")
//                    GlobalScope.launch(Dispatchers.Main) {
////                        adapter.notifyItemChanged(position)
//                    }
                }

                override fun onFinishDownload() {
                    bean.downloadState = 3
//                    GlobalScope.launch(Dispatchers.Main) {
////                        adapter.notifyItemChanged(position)
//                    }
                    bean.isResume = false
                    Log.d(TAG, "完成下载")
                    listenerMap.remove(bean.path)
                    remove(context, bean.path)
                }

                override fun onFail(errorInfo: String?) {
                    Log.e(TAG, errorInfo.toString())
                    //暂时全部取消,根据具体需求更改
                    cancel()
                    Log.d(TAG, "下载失败:$errorInfo")
                    remove(context, bean.path)
                }

                override fun onKeepOn() {
                    isPauseState = false
                }

                override fun onPause(path: String) {
                    save(context, bean.path, path, indexProgress, total, filePointerLength)
                }

                override fun onCancel(path: String) {
                    remove(context, bean.path)
                    File(path).delete()
                }
            }
            Log.d(TAG, "listener = $listener")
            listenerMap.put(bean.path, listener)
            val flow = downloadFile(bean.path, listener)
            flow.flowOn(Dispatchers.IO).cancellable().map {
                Log.d("*******", "map-----------------------------")
                //转换成字节流
//                    it.byteStream()
                it
            }.onCompletion { cause ->

            }.next { input ->
                Log.d("*******", "collect+++++++++++++++++")
                writeResponseBodyToDiskFile(
                    fileUrl = bean.path,
                    fileBody = input,
                    filePath = context.getExternalFilesDir("download")?.path.toString(),
                    listener = listener
                )
//                    writeFile(input, bean.path, path, listener = listener)
            }
        }
    }

    /*取消下载*/
    fun cancel() {
        for (job in jobList) {
            job.cancel()
        }
        jobList.clear()

        for (map in listenerMap) {
            map.value.isCancel = true
        }
        listenerMap.clear()
    }

    //TODO:--------------------------------------协程方式下载-----------------------------------------


    //TODO:--------------------------------------rxjava方式下载--------------------------------------
    /**
     * 下载网络文件
     * @param fileUrl 文件下载地址
     * @param saveFilePath 文件保存路径
     * */
    fun downloadApk(
        fileUrl: String, saveFilePath: String
    ): Maybe<Unit> {
        //TODO 后续可使用flow替代
        return mDownloadApiService.downloadFileFromUrl(fileUrl)
            //step 下载文件响应，将byte字节数组保存到本地文件
            .map { responseBody ->
//                writeResponseBodyToDiskFile(
//                    fileUrl = fileUrl, fileBody = responseBody, filePath = saveFilePath
//                )
            }
            //step 下载出现异常
            .doOnError {
                sendDownloadProgressChange(DownloadProgressBean.createDownloadFailure(fileUrl))
            }
            //上游执行在子线程，下游执行在主线程
            .subscribeOn(Schedulers.from(ExecutorProvider.ioExecutor))
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 文件下载进度百分比的可观察对象，
     * [PublishRelay]只有onNext的subject，不会因为OnError中断信息
     * */
    private val mDownloadProgressSubject = PublishRelay.create<DownloadProgressBean>()

    /**
     * 订阅指定地址的下载进度变化
     * @param fileUrl 文件下载地址
     * */
    fun observeDownloadProgressChange(fileUrl: String): Flowable<DownloadProgressBean> {
        return mDownloadProgressSubject.toFlowable(BackpressureStrategy.LATEST)
            //仅允许指定下载地址的进度继续向下游进行
            .filter { it.fileTag == fileUrl }
    }

    //TODO:--------------------------------------rxjava方式下载--------------------------------------
    private fun sendDownloadProgressChange(progressBean: DownloadProgressBean) {
        mDownloadProgressSubject.accept(progressBean)
    }


    fun save(
        context: Context, url: String, path: String, progress: Int, total: Long, filePointer: Long
    ) {
        SPUtils.putApply(context, AppConstant.FILE_PATH + url, path)
        SPUtils.putApply(context, AppConstant.FILE_PROGRESS + url, progress)
        SPUtils.putApply(context, AppConstant.FILE_TOTAL + url, total)
        SPUtils.putApply(context, AppConstant.FILE_POINTER + url, filePointer)
    }

    fun remove(context: Context, url: String) {
        SPUtils.remove(context, AppConstant.FILE_PATH + url)
        SPUtils.remove(context, AppConstant.FILE_PROGRESS + url)
        SPUtils.remove(context, AppConstant.FILE_TOTAL + url)
        SPUtils.remove(context, AppConstant.FILE_POINTER + url)
    }
}