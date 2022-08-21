package com.model.login.data

import com.google.gson.GsonBuilder
import com.library.common.net.CommParamsInterceptor
import com.library.common.net.LoggerHttpLogPrinterImpl
import com.wangkai.remote.HttpRequestMediator
import com.wangkai.remote.ext.addDslRemoteConfig
import com.wangkai.remote.flow.FlowCallAdapterFactory
import com.wangkai.remote.interceptor.HttpLogInterceptor
import com.wangkai.remote.tools.delegator.BaseRequestApiDelegator
import com.yupfeg.executor.ExecutorProvider
import retrofit2.converter.gson.GsonConverterFactory


/**
 * wanAndroid的网络请求retrofit api接口的委托
 * * 通过by关键字委托创建api接口实例
 * */
inline fun <reified T> loginApiDelegate() = LoginApiDelegator(clazz = T::class.java)

/**
 * 玩Android的个公开Api接口网络请求配置代理类
 * @param clazz 需要代理的retrofit api接口的类对象
 * @author WangKai
 */
class LoginApiDelegator<T>(clazz: Class<T>) :
    BaseRequestApiDelegator<T>(clazz, HttpRequestMediator.DEFAULT_CLIENT_KEY) {
    override fun addHttpRequestConfig(configKey: String) {

        addDslRemoteConfig(configKey) {
            baseUrl = "http://39.106.144.234/api/"
            connectTimeout = 5
            readTimeout = 10
            writeTimeout = 15
            isAllowProxy = true
            isRetryOnConnectionFailure = false
            //设置使用外部线程调度器
            executorService = ExecutorProvider.ioExecutor
            networkInterceptors.add(CommParamsInterceptor())
            networkInterceptors.add(HttpLogInterceptor(LoggerHttpLogPrinterImpl(), "LOGIN_TAG"))
            //添加gson解析支持
            converterFactories.add(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            callAdapterFactories.add(FlowCallAdapterFactory.create(true))
        }
    }
}