package com.library.common.network.tools.coroutine

import android.util.Log
import com.wangkai.remote.data.HttpResponseParsable
import com.wangkai.remote.tools.handler.GlobalHttpResponseProcessor
import com.wangkai.remote.tools.handler.RestApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * 预处理网络请求返回
 * @author wangkai
 */
suspend fun <T : HttpResponseParsable> Flow<T>.preHandleHttpResponse(success: suspend T.() -> Unit) {
    map {
        val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(it)
        if (!isSuccess) {
            //业务执行异常
            Log.d("AAAAAAAAAAXXWDAC", "message:${it.message}")
            throw RestApiException(it.code, it.message)
        }
        it
    }.catch {
        GlobalHttpResponseProcessor.handleHttpError(it)
        Log.d("AAAAAAAAAAXXWDAC", "请求异常：$it")
    }.flowOn(Dispatchers.IO).collect {
        success(it)
    }
}