package com.wangkai.remote

import com.wangkai.remote.HttpRequestMediator.addDefaultHttpClientFactory
import com.wangkai.remote.HttpRequestMediator.addHttpClientFactory
import com.wangkai.remote.HttpRequestMediator.createRequestApi
import com.wangkai.remote.HttpRequestMediator.getRetrofitInstance
import com.wangkai.remote.config.HttpRequestConfig
import com.wangkai.remote.factory.DefaultHttpClientFactoryImpl
import com.wangkai.remote.factory.HttpClientFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 描述：（Retrofit）网络请求的核心管理类
 *
 * * 使用流程：
 *
 * 1.先通过[addDefaultHttpClientFactory]或者[addHttpClientFactory]添加网络请求的工厂对象。
 *
 * 2.调用[getRetrofitInstance]获取[Retrofit]实例，来创建请求api对象，或者直接调用[createRequestApi]创建请求api对象
 *
 * @author 王凯
 * @date 2022/07/04
 */
@Suppress("unused")
object HttpRequestMediator {
    /**
     * 连接池的默认key
     */
    const val DEFAULT_CLIENT_KEY = "defaultClient"
    const val DEFAULT_DOWNLOAD_CLIENT_KEY = "defaultDownloadClient"

    /**
     * 描述:创建网络请求client对象的工厂类集合
     * */
    private val mHttpClientFactories: HashMap<String, HttpClientFactory> = HashMap()

    /**
     * 描述:获取OkHttpClient实例
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @return OkHttpClient
     * @exception NullPointerException
     * @exception IllegalAccessException
     * */
    @JvmStatic
    @Throws(NullPointerException::class, IllegalAccessException::class)
    fun getOkHttpInstance(configKey: String = DEFAULT_CLIENT_KEY): OkHttpClient {
        return fetchHttpClientFactory(configKey)
            .getOkHttpClientInstance()
    }

    /**
     * 描述:获取Retrofit实例
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @return Retrofit
     * @exception NullPointerException
     * @exception IllegalAccessException
     * */
    @JvmStatic
    @Throws(NullPointerException::class, IllegalAccessException::class)
    fun getRetrofitInstance(configKey: String = DEFAULT_CLIENT_KEY): Retrofit {
        return fetchHttpClientFactory(configKey)
            .getRetrofitInstance()
    }

    /**
     * 描述:网络请求client是否已存在
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @return true - 表示已添加指定配置
     */
    @JvmStatic
    fun containsRequestKey(configKey: String): Boolean {
        return mHttpClientFactories.containsKey(configKey)
    }

    /**
     * 描述:尝试获取对应key的网络请求配置工厂
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @return HttpClientFactory
     */
    @Throws(NullPointerException::class, IllegalAccessException::class)
    private fun fetchHttpClientFactory(
        configKey: String = DEFAULT_CLIENT_KEY
    ): HttpClientFactory {
        if (mHttpClientFactories.containsKey(configKey)) {
            return mHttpClientFactories[configKey]
                ?: throw NullPointerException(
                    "Http Client is Null,you should add clientFactory before use"
                )
        }
        throw IllegalAccessException("You must add http client Factory")
    }

    /**
     * 描述:构建Retrofit的API接口动态代理实例
     * * 内部调用[Retrofit.create]
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @param clazz retrofit代理接口类
     * @param <T> 接口class实例，代理请求
     * @return Retrofit实例
     */
    @JvmStatic
    @Throws(NullPointerException::class, IllegalAccessException::class)
    fun <T> createRequestApi(
        configKey: String = DEFAULT_CLIENT_KEY,
        clazz: Class<T>
    ): T {
        return getRetrofitInstance(configKey).create(clazz)
    }

    /**
     * 描述:方式一添加默认实现的网络请求工厂类
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @param init 用于kotlin DSL方式配置网络请求参数[HttpRequestConfig]
     * @return HttpRequestMediator
     * */
    @JvmStatic
    fun addDefaultHttpClientFactory(
        configKey: String = DEFAULT_CLIENT_KEY,
        init: HttpRequestConfig.() -> Unit
    ): HttpRequestMediator {
        mHttpClientFactories[configKey] =
            DefaultHttpClientFactoryImpl.create(init)
        return this
    }

    /**
     * 描述:方式二添加默认实现的网络请求工厂类
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @param config 网络请求参数配置[HttpRequestConfig]
     * @return HttpRequestMediator
     * */
    @JvmStatic
    fun addDefaultHttpClientFactory(
        configKey: String = DEFAULT_CLIENT_KEY,
        config: HttpRequestConfig
    ): HttpRequestMediator {
        mHttpClientFactories[configKey] =
            DefaultHttpClientFactoryImpl.create(config)
        return this
    }

    /**
     * 描述:直接添加网络请求工厂
     * @param configKey 配置标识符，标识特定配置的网络请求client
     * @param factory [HttpClientFactory]实现类，网络请求配置工厂类对象
     * @return HttpRequestMediator
     * */
    @JvmStatic
    fun addHttpClientFactory(
        configKey: String = DEFAULT_CLIENT_KEY,
        factory: HttpClientFactory
    ): HttpRequestMediator {
        mHttpClientFactories[configKey] = factory
        return this
    }
}