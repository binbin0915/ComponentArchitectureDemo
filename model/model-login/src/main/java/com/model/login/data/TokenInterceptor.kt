package com.model.login.data

import android.util.Log
import com.library.base.application.BaseApplication
import com.library.base.datastore.DataStoreUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 保存登录后的token
 * @author WangKai
 * @date 2022/08/21
 */
class TokenInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取到response
        val request = chain.request()
        val response: Response = chain.proceed(request)
        val url = request.url.toString()
        if (url.isNotEmpty() && url.contains("/api/login")) {
            //登录接口获取token并保存
            runBlocking {
                response.headers["Authorization"]?.run {
                    DataStoreUtils.put(BaseApplication.appContext, "Authorization", this)
                }
            }

        }
        return response
    }
}