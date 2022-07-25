package com.model.mykotlin.data.remote

import android.util.Log
import com.library.common.netconfig.tools.download.FileDownloadBean
import com.library.common.netconfig.tools.download.FileDownloadProducer
import com.model.mykotlin.data.delegate.wanAndroidApiDelegate
import com.model.mykotlin.data.entity.WanAndroidArticleListResponseEntity
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.config.HttpRequestConfig
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import com.yupfeg.remote.tools.handler.RestApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

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



    private val mApkDownloadProducer: FileDownloadProducer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FileDownloadProducer(
            requestTag = HttpRequestMediator.DEFAULT_DOWNLOAD_CLIENT_KEY,
            requestConfig = createApkDownloadHttpRequestConfig()
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun download(downloadUrl: String, savePath: String,coroutineScope: CoroutineScope) {
        Log.d("AAAAAAAAAAXXXXXSW", "savePath:$savePath")
        mApkDownloadProducer.jobList.add(coroutineScope.launch {
            mApkDownloadProducer.filePath = savePath
            mApkDownloadProducer.load(
                FileDownloadBean(downloadUrl),
                newFixedThreadPoolContext(1, "DownloadContext")
            )
        })
    }

    private fun createApkDownloadHttpRequestConfig(): HttpRequestConfig {
        return HttpRequestConfig().apply {
            baseUrl = "http://baidu.com"
            connectTimeout = 15
            readTimeout = 15
            writeTimeout = 20
            isAllowProxy = true
//            callAdapterFactories.add()
        }
    }
}