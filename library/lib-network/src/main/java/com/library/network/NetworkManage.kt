package com.library.network

import android.text.TextUtils
import com.library.network.config.DefaultNetworkConfig
import com.library.network.config.NetworkConfig
import com.library.network.ssl.SSLManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

/**
 * 作用描述：网络全局管理
 *
 * 创建时间：2022/703/18
 * @author：WangKai
 */
object NetworkManage {

    /**
     * 网络配置项
     */
    private lateinit var configBean: NetworkConfig

    /**
     * okHttp客户端【用于配置一些东西】
     */
    private lateinit var okHttpClient: OkHttpClient

    /**
     * retrofit实例集合
     */
    private var retrofitInstance: HashMap<String, Retrofit> = hashMapOf()

    /**
     * 全局的Host key
     */
    const val GLOBAL_API_KEY = "GlobalApiKey"

    /**
     * 请求过程中网络异常的提示文案
     */
    const val NETWORK_ERROR_TIPS = "网络不稳定，请检查网络"

    /**
     * 请求过程中网络连接异常的提示文案
     */
    const val NETWORK_CONNECT_ERROR_TIPS = "网络连接异常，请检查网络"


    /**
     * 配置网络
     */
    fun config(config: NetworkConfig = DefaultNetworkConfig()): NetworkManage {
        this.configBean = config
        return this
    }


    /**
     * 根据Host 创建ApiService
     *
     * Host默认值：GLOBAL_API_KEY
     */
    fun <T> createApiService(key: String = GLOBAL_API_KEY, clazz: Class<T>): T {
        if (retrofitInstance.containsKey(key)) {
            //如果在连接池中有这个key
            val instance = retrofitInstance[key]
            return if (instance != null) {
                instance.create(clazz)
            } else {
                throw NullPointerException("The instance does not exist")
            }
        } else {
            throw NullPointerException("The incoming Host does not exist")
        }
    }


    /**
     * 开始配置
     */
    fun build() {
        if (::configBean.isInitialized) {
            //TODO:获取Builder【是否支持https】
            val okHttpClientBuilder = if (configBean.isSupportHttps()) {
                SSLManager.getSSLSocketFactory()
            } else {
                OkHttpClient.Builder()
            }
            //TODO:对okHttpClientBuilder进行一些配置
            //添加拦截器
            configBean.interceptors().forEach {
                okHttpClientBuilder.addInterceptor(it)
            }
            //配置连接超时时间
            okHttpClientBuilder.connectTimeout(configBean.connectTimeout(), TimeUnit.SECONDS)
            //读取超时时间
            okHttpClientBuilder.readTimeout(configBean.readTimeout(), TimeUnit.SECONDS)
            //写入超时时间
            okHttpClientBuilder.writeTimeout(configBean.writeTimeout(), TimeUnit.SECONDS)
            //TODO:获取okHttpClient
            okHttpClient = okHttpClientBuilder.build()

            //TODO:创建Host实例并保存
            retrofitInstance.clear()
            if (configBean.isSingleHost()) {
                retrofitInstance[GLOBAL_API_KEY] = getRetrofitInstance(configBean.globalApiHost())
            } else {
                //添加多个Host对应的实例
                configBean.multipleApiHost().forEach {
                    retrofitInstance[it.key] = getRetrofitInstance(it.value)
                }
                //如果存在全局Host则也创建
                if (!TextUtils.isEmpty(configBean.globalApiHost())) {
                    retrofitInstance[GLOBAL_API_KEY] =
                        getRetrofitInstance(configBean.globalApiHost())
                }
            }
        } else {
            throw NullPointerException("NetworkManage configBean Not configured")
        }
    }

    /**
     * 创建Retrofit实例
     */
    private fun getRetrofitInstance(host: String): Retrofit {
        val instance = Retrofit.Builder()
        instance.baseUrl(host)
        //直接转换成实体对象
        instance.addConverterFactory(MoshiConverterFactory.create())
        instance.client(okHttpClient)
        instance.build()
        return instance.build()
    }
}