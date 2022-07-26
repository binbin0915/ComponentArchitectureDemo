package com.library.common.netconfig.tools.download

/*coroutines*/
import android.util.Log
import com.library.common.commonutils.SPUtils
import com.library.common.netconfig.LoggerHttpLogPrinterImpl
import com.library.common.netconfig.constant.AppConstant
import com.library.common.netconfig.tools.remote.DownloadApiService
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.HttpRequestMediator.createRequestApi
import com.yupfeg.remote.config.HttpRequestConfig
import com.yupfeg.remote.download.BaseFileDownloadProducer
import com.yupfeg.remote.download.DownloadListener
import com.yupfeg.remote.interceptor.DownloadProgressInterceptor
import com.yupfeg.remote.log.HttpLogPrinter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.Interceptor
import okhttp3.ResponseBody
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * * 基于协程flow+Retrofit实现下载文件功能的数据源提供类
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
    var listenerMap: MutableMap<String, DownloadListener> = mutableMapOf()

    /*协程job集合*/
    var jobList: MutableList<Job> = mutableListOf()

    /**
     * 一、添加网络请求的工厂
     */
    init {
        HttpRequestMediator.addDefaultHttpClientFactory(requestTag) {
            baseUrl = "http://baidu.com"
            connectTimeout = requestConfig.connectTimeout
            readTimeout = requestConfig.readTimeout
            writeTimeout = requestConfig.writeTimeout
            isAllowProxy = requestConfig.isAllowProxy
            applicationInterceptors = requestConfig.applicationInterceptors
            networkInterceptors.add(createDownloadInterceptor(LoggerHttpLogPrinterImpl()))
        }
    }

    private fun createDownloadInterceptor(logPrinter: HttpLogPrinter? = null): Interceptor {
        return DownloadProgressInterceptor(
            logPrinter = logPrinter,
        ) { progressBean ->
            //此时处于子线程，不能直接回调执行UI操作
            Log.e(TAG, "progressBean:" + progressBean.progress)
        }
    }

    /**
     * 二、直接调用[createRequestApi]创建请求api对象
     */
    private val mDownloadApiService: DownloadApiService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        createRequestApi(
            requestTag, DownloadApiService::class.java
        )
    }

    //TODO:--------------------------------------协程方式下载-----------------------------------------

    /**
     * 开始下载方法
     */
    private fun downloadFile(fileUrl: String, listener: DownloadListener): Flow<ResponseBody> =
        flow {
            delay(500)
            listener.onStartDownload()
            val download = mDownloadApiService.download(fileUrl)
            emit(download)
        }

    suspend fun <T> Flow<T>.next(bloc: suspend T.(T) -> Unit): Unit = collect { bloc(it, it) }

    suspend fun load(
        fileDownloadBean: FileDownloadBean,
        savePath: String,
        coroutine: CoroutineContext
    ) {
        withContext(coroutine) {
            val listener = object : DownloadListener {
                var indexProgress = 0
                var total = 0L
                var isPauseState = false
                var isCancelState = false
                var isResumeState = fileDownloadBean.isResume
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
                        SPUtils[AppConstant.FILE_POINTER + fileDownloadBean.url, 0L] as Long
                    mFileTotalSize =
                        SPUtils[AppConstant.FILE_TOTAL + fileDownloadBean.url, 0L] as Long
                }

                override fun onProgress(progress: Int, totalLength: Long) {
                    fileDownloadBean.progress = progress
                    indexProgress = progress
                    total = totalLength
                    Log.d(TAG, "progress:$progress")
                }

                override fun onFinishDownload() {
                    fileDownloadBean.downloadState = 3
                    fileDownloadBean.isResume = false
                    Log.d(TAG, "完成下载")
                    listenerMap.remove(fileDownloadBean.url)
                    remove(fileDownloadBean.url)
                }

                override fun onFail(errorInfo: String?) {
                    Log.e(TAG, errorInfo.toString())
                    //暂时全部取消,根据具体需求更改
                    cancel()
                    Log.d(TAG, "下载失败:$errorInfo")
                    SPUtils.remove(fileDownloadBean.url)
                }

                override fun onKeepOn() {
                    isPauseState = false
                }

                override fun onPause(path: String) {
                    save(fileDownloadBean.url, path, indexProgress, total, filePointerLength)
                }

                override fun onCancel(path: String) {
                    remove(fileDownloadBean.url)
                    File(path).delete()
                }
            }
            Log.d(TAG, "listener = $listener")
            listenerMap[fileDownloadBean.url] = listener
            val flow = downloadFile(fileDownloadBean.url, listener)
            flow.flowOn(Dispatchers.IO).cancellable().map {
                it
            }.onCompletion {

            }.next { responseBody ->
                writeResponseBodyToDiskFile(
                    fileBody = responseBody, filePath = savePath, listener = listener
                )
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


    fun save(url: String, path: String, progress: Int, total: Long, filePointer: Long) {
        SPUtils.putApply(AppConstant.FILE_PATH + url, path)
        SPUtils.putApply(AppConstant.FILE_PROGRESS + url, progress)
        SPUtils.putApply(AppConstant.FILE_TOTAL + url, total)
        SPUtils.putApply(AppConstant.FILE_POINTER + url, filePointer)
    }

    fun remove(url: String) {
        SPUtils.remove(AppConstant.FILE_PATH + url)
        SPUtils.remove(AppConstant.FILE_PROGRESS + url)
        SPUtils.remove(AppConstant.FILE_TOTAL + url)
        SPUtils.remove(AppConstant.FILE_POINTER + url)
    }
}