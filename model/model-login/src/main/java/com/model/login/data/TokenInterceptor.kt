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
            //请尽可能避免在 DataStore 数据读取时阻塞线程。阻塞界面线程可能会导致 ANR 或界面卡顿，而阻塞其他线程可能会导致死锁。
            //DataStore 的主要优势之一是异步 API，但可能不一定始终能将周围的代码更改为异步代码。如果您使用的现有代码库采用同步磁盘 I/O，或者您的依赖项不提供异步 API，就可能出现这种情况。
            //Kotlin 协程提供 runBlocking() 协程构建器，以帮助消除同步与异步代码之间的差异。您可以使用 runBlocking() 从 DataStore 同步读取数据。
            runBlocking {
                response.headers["Authorization"]?.run {
                    DataStoreUtils.put(BaseApplication.appContext, "Authorization", this)
                }
            }

        }
        return response
    }
}