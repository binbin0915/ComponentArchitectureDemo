package com.wangkai.remote.factory

import com.wangkai.remote.config.HttpRequestConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 默认实现的网络请求实例工厂类
 * @author 王凯
 * @date 2021/07/19
 */
class DefaultHttpClientFactoryImpl private constructor() : BaseHttpClientFactoryImpl(){

    companion object{

        /**
         * 使用kotlin DSL方式配置创建默认的网络请求工厂类
         * @param init 接受者为[HttpRequestConfig]的函数，可直接进行DSL配置
         * */
        @Suppress("unused")
        fun create(init : com.wangkai.remote.config.HttpRequestConfig.()->Unit) : BaseHttpClientFactoryImpl {
            return com.wangkai.remote.config.HttpRequestConfig().run {
                this.init()
                DefaultHttpClientFactoryImpl().performRetrofitConfig(this)
            }
        }

        /**
         * 创建网络请求工厂类
         * @param config 网络请求参数配置
         * */
        @Suppress("unused")
        fun create(config : com.wangkai.remote.config.HttpRequestConfig) : BaseHttpClientFactoryImpl {
            return DefaultHttpClientFactoryImpl().performRetrofitConfig(config)
        }

    }

    /**
     * 获取[Retrofit]单例
     * @return [Retrofit]对象单例
     */
    override fun getRetrofitInstance(): Retrofit {
        return mRetrofit ?: synchronized(this){
            mRetrofit ?: buildRetrofitInstance(getOkHttpClientInstance()).apply { mRetrofit = this }
        }
    }

    /**
     * 获取[OkHttpClient]单例
     * @return [OkHttpClient]单例对象
     * */
    override fun getOkHttpClientInstance(): OkHttpClient {
        return mOkHttpClient ?: synchronized(this){
            mOkHttpClient ?: buildOkHttpClientInstance().apply { mOkHttpClient = this }
        }
    }

}