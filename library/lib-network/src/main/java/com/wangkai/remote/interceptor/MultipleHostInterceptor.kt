package com.wangkai.remote.interceptor

import com.wangkai.remote.tools.url.UrlRedirectHelper
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 描述：动态替换多域名的拦截器
 * @author WangKai
 * @date 2022/07/04
 */
class MultipleHostInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val redirectRequest = UrlRedirectHelper.obtainRedirectedUrlRequest(chain.request())
        return chain.proceed(redirectRequest)
    }

}