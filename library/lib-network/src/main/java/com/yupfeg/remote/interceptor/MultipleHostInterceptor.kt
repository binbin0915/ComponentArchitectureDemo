package com.yupfeg.remote.interceptor

import com.yupfeg.remote.tools.url.UrlRedirectHelper
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 描述：动态替换多域名的拦截器
 * @author WangKai
 * @date 2022/07/04
 */
@Suppress("unused")
class MultipleHostInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val redirectRequest = UrlRedirectHelper.obtainRedirectedUrlRequest(chain.request())
        return chain.proceed(redirectRequest)
    }

}