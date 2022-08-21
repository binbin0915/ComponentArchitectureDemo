package com.library.common.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 保存登录后的token
 * @author WangKai
 * @date 2022/08/21
 */
class HeaderParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取到response
        val request = chain.request()
        val response: Response = chain.proceed(request)
        val url = request.url.toString()
        if (url.isNotEmpty() && url.contains("/api/login")) {
            //登录接口获取token并保存
            val value = response.headers["Authorization"]
            Log.d("AAAAAAAAAAAAAAA", "Authorization:$value")
        }
        return response
    }
}