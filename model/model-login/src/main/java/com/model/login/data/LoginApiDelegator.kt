package com.model.login.data

import com.google.gson.GsonBuilder
import com.library.common.netconfig.LoggerHttpLogPrinterImpl
import com.yupfeg.executor.ExecutorProvider
import com.yupfeg.remote.HttpRequestMediator
import com.yupfeg.remote.ext.addDslRemoteConfig
import com.yupfeg.remote.tools.delegator.BaseRequestApiDelegator
import com.yupfeg.remote.interceptor.HttpLogInterceptor
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
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
            baseUrl = "http://39.106.144.234/"
            connectTimeout = 5
            readTimeout = 10
            writeTimeout = 15
            isAllowProxy = true
            //设置使用外部线程调度器
            executorService = ExecutorProvider.ioExecutor
            networkInterceptors.add(HttpLogInterceptor(LoggerHttpLogPrinterImpl()))
            //添加gson解析支持
            converterFactories.add(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            //设置返回对象处理(kotlin协程在新版本原生支持)
//            callAdapterFactories.apply {
//                //支持RxJava3(create()方法创建的是采用okHttp内置的线程池，下游数据流使用subscribeOn无效
//                // createSynchronous()则使用下游subscribeOn提供的线程池)
////                add(RxJava3CallAdapterFactory.createSynchronous())
//                //支持RxJava2(create()方法创建的工厂类，采用外部下游subscribeOn提供的线程池，
//                // 而createAsync()则使用okHttp内置的线程池)
////                add(RxJava2CallAdapterFactory.create())
//            }
        }
    }
}