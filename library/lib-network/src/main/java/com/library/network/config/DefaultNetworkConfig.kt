package com.library.network.config

import okhttp3.Interceptor

/**
 * 作用描述：默认的网络配置
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class DefaultNetworkConfig : NetworkConfig {

    override fun isSingleHost(): Boolean {
        return true
    }

    override fun globalApiHost(): String {
        return "https://test.com/api/"
    }

    override fun multipleApiHost(): HashMap<String, String> {
        return hashMapOf()
    }

    /**
     * 是否支持HTTPS
     */
    override fun isSupportHttps(): Boolean {
        return true
    }

    /**
     * 拦截器
     */
    override fun interceptors(): ArrayList<Interceptor> {
        return arrayListOf()
    }

    /**
     * 超时时间
     */
    override fun connectTimeout(): Long {
        return 10
    }

    override fun readTimeout(): Long {
        return 10
    }

    override fun writeTimeout(): Long {
        return 10
    }
}