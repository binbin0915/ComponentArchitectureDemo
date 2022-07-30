package com.yupfeg.remote.download.producer

/*coroutines*/
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.HttpRequestMediator.createRequestApi
import com.yupfeg.remote.config.HttpRequestConfig
import com.yupfeg.remote.download.DownloadApiService
import com.yupfeg.remote.download.DownloadListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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

    /*监听的集合*/
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
    private fun downloadFile(
        fileUrl: String,
    ): Flow<ResponseBody> =
        flow {
            val download = mDownloadApiService.download(fileUrl)
            emit(download)
        }

    /**
     * 给Flow拓展next方法
     *
     * Flow本身是一个接口，在这个接口里面定义了一个挂起函数collect函数，它接收的是一个FlowCollector对象。
     * FlowCollector接口中有一个挂起函数emit。
     *
     * 通过collect函数来收集flow这些数据。但是因为collect是挂起函数。
     * 挂起函数的调用又必须在另一个挂起函数或者协程作用域中。此时就需要我们使用协程来执行。
     *
     * bloc: suspend T.(T) -> Unit ：FlowCollector<ResponseBody>
     * it指T，即ResponseBody
     * bloc(it, it)：传入ResponseBody返回ResponseBody
     */
    suspend fun <T> Flow<T>.next(bloc: suspend T.(T) -> Unit): Unit = collect { bloc(it, it) }

    suspend fun load(
        listener: DownloadListener,
        coroutine: CoroutineContext
    ) {
        withContext(coroutine) {
            listenerMap[listener.fileDownloadBean.url] = listener
            val flow = downloadFile(listener.fileDownloadBean.url)
            flow.flowOn(Dispatchers.IO).cancellable().map {
                it
            }.onCompletion {

            }.collect { responseBody ->
                writeResponseBodyToDiskFile(
                    responseBody = responseBody,
                    listener = listener
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
        listenerMap.clear()
    }
}