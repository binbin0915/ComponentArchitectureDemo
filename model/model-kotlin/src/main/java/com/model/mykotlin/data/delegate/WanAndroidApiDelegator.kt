package com.model.mykotlin.data.delegate

import com.google.gson.GsonBuilder
import com.library.common.net.LoggerHttpLogPrinterImpl
import com.yupfeg.executor.ExecutorProvider
import com.wangkai.remote.ext.addDslRemoteConfig
import com.wangkai.remote.tools.delegator.BaseRequestApiDelegator
import com.wangkai.remote.interceptor.HttpLogInterceptor
import retrofit2.converter.gson.GsonConverterFactory


/**
 * wanAndroid的网络请求retrofit api接口的委托
 * * 通过by关键字委托创建api接口实例
 * */
inline fun <reified T> wanAndroidApiDelegate() = WanAndroidApiDelegator(clazz = T::class.java)

/**
 * 玩Android的个公开Api接口网络请求配置代理类
 * @param clazz 需要代理的retrofit api接口的类对象
 * @author WangKai
 */
class WanAndroidApiDelegator<T>(clazz: Class<T>) :
    BaseRequestApiDelegator<T>(clazz, com.wangkai.remote.HttpRequestMediator.DEFAULT_CLIENT_KEY) {
    override fun addHttpRequestConfig(configKey: String) {
        addDslRemoteConfig(configKey) {
            baseUrl = "https://www.wanandroid.com/"
            connectTimeout = 5
            readTimeout = 10
            writeTimeout = 15
            isAllowProxy = true
            //设置使用外部线程调度器
            executorService = ExecutorProvider.ioExecutor
            networkInterceptors.add(HttpLogInterceptor(LoggerHttpLogPrinterImpl()))
            //添加gson解析支持
            converterFactories.add(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }
    }
}