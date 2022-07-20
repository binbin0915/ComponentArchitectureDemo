package com.model.mykotlin.data.remote

import android.content.Context
import com.library.base.application.BaseApplication
import com.library.common.netconfig.tools.download.Bean
import com.model.mykotlin.data.delegate.wanAndroidApiDelegate
import com.library.common.netconfig.tools.download.FileDownloadProducer
import com.library.common.netconfig.tools.rxjava3.preHandlerRxJava3Response
import com.model.mykotlin.data.entity.JueJinHttpResultBean
import com.model.mykotlin.data.entity.WanAndroidArticleListResponseEntity
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.config.HttpRequestConfig
import com.yupfeg.remote.download.entity.DownloadProgressBean
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import com.yupfeg.remote.tools.handler.RestApiException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * 远程网络数据源
 *
 * 基本使用
 * * 接口请求
 * * 上传
 * * 下载
 * @author WangKai
 * @date 2022/04/03
 */
object RemoteDataSource {

    /**
     * wanAndroid的网络请求retrofit api接口的委托
     * * 通过by关键字委托创建api接口实例
     * */
    private val mApiService: TestApiService by wanAndroidApiDelegate()

    /**
     * 基于kotlin 协程获取wanAndroid的文章列表数据
     * @param pageIndex 分页页数
     * */
    suspend fun queryWanAndroidArticleByCoroutine(pageIndex: Int): WanAndroidArticleListResponseEntity {
        val result = mApiService.queryWanAndroidArticleByCoroutine(pageIndex)
        val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(result)
        if (!isSuccess) {
            //业务执行异常
            throw RestApiException(result.code, result.message)
        }
        //业务执行成功
        return result
    }

    /**
     * 基于RxJava3获取wanAndroid的文章列表数据
     * @param pageIndex 分页页数
     * */
    fun queryWanAndroidArticlesByRxJava3(pageIndex: Int): Maybe<WanAndroidArticleListResponseEntity> {
        return mApiService.getWanAndroidArticlesByRxJava3(pageIndex)
            .compose(preHandlerRxJava3Response())
    }

    /**
     * 基于RxJava3，获取掘金PC端的文章列表接口
     * * 无法正常请求，仅用于测试动态切换baseUrl
     * */
    fun queryJueJinAdvertsByRxJava3(): Maybe<JueJinHttpResultBean> {
        return mApiService.queryJueJinAdverts()
    }

    /**
     * 基于RxJava3，获取百度PC端查询接口
     * * 无法正常请求，仅用于测试动态切换baseUrl
     */
    fun queryBaiduData(): Maybe<JueJinHttpResultBean> {
        return mApiService.queryBaiduData("98010089_dg", "android")
    }

    /**
     * 下载的配置
     */

    private const val TEST_DOWNLOAD_APK_NAME = "testDownload.apk"

    private val mApkDownloadProducer: FileDownloadProducer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FileDownloadProducer(
            requestTag = HttpRequestMediator.DEFAULT_DOWNLOAD_CLIENT_KEY,
            requestConfig = createApkDownloadHttpRequestConfig()
        )
    }

    fun download(context: Context, bean: Bean) {
        mApkDownloadProducer.jobList.add(MainScope().launch {
            mApkDownloadProducer.load(context, bean, newFixedThreadPoolContext(1, "DownloadContext"))
        })
    }

    private fun createApkDownloadHttpRequestConfig(): HttpRequestConfig {
        return HttpRequestConfig().apply {
            baseUrl = "http://baidu.com"
            connectTimeout = 15
            readTimeout = 15
            writeTimeout = 20
            isAllowProxy = true
            //支持RxJava3(create()方法创建的是采用okHttp内置的线程池，下游数据流使用subscribeOn无效
            // createSynchronous()则使用下游subscribeOn提供的线程池)
            callAdapterFactories.add(RxJava3CallAdapterFactory.create())
        }
    }

    /**
     * 订阅apk下载进度变化
     * @param fileUrl
     * @return
     */
    fun observeApkDownloadProgress(fileUrl: String): Flowable<DownloadProgressBean> {
        return mApkDownloadProducer.observeDownloadProgressChange(fileUrl)
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 下载apk
     * @param fileUrl
     * @return
     */
    fun downloadApk(fileUrl: String): Maybe<Unit> {
        val downloadPath = getApkDownloadFilePath()
        val downloadFile = File(downloadPath)
        if (downloadFile.exists()) {
            //删除已存在的文件
            downloadFile.delete()
        }
        return mApkDownloadProducer.downloadApk(fileUrl, downloadPath)
    }


    private fun getApkDownloadFilePath(): String {
        val appFileDirPath = BaseApplication.instance.applicationContext.filesDir.absolutePath
        val apkDirPath = appFileDirPath + File.separator + ".apk"
        return "$apkDirPath$TEST_DOWNLOAD_APK_NAME"
    }
}