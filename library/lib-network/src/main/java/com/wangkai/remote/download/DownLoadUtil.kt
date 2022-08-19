package com.wangkai.remote.download

import com.wangkai.remote.download.producer.FileDownloadProducer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

object DownLoadUtil {
    /**
     * 下载文件功能的数据源配置
     */
    private val downloadProducer: FileDownloadProducer by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        FileDownloadProducer(
            requestTag = com.wangkai.remote.HttpRequestMediator.DEFAULT_DOWNLOAD_CLIENT_KEY,
            requestConfig = createDownloadHttpRequestConfig()
        )
    }

    /**
     * retrofit的配置
     */
    private fun createDownloadHttpRequestConfig(): com.wangkai.remote.config.HttpRequestConfig {
        return com.wangkai.remote.config.HttpRequestConfig().apply {
            connectTimeout = 15
            readTimeout = 15
            writeTimeout = 20
            isAllowProxy = true
        }
    }

    /**
     * 下载的方法
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun download(listener: DownloadListener) {
        downloadProducer.jobList.add(GlobalScope.launch {
            downloadProducer.load(
                listener,
                newFixedThreadPoolContext(3, "DownloadContext")
            )
        })
    }

    /**
     * 取消下载的方法
     */
    fun cancel() {
        downloadProducer.cancel()
    }
}