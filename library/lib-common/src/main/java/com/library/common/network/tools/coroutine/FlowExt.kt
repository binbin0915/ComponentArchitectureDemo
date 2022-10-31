package com.library.common.network.tools.coroutine

import com.wangkai.remote.data.HttpResponseParsable
import com.wangkai.remote.tools.handler.GlobalHttpResponseProcessor
import com.wangkai.remote.tools.handler.RestApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * 预处理网络请求返回
 *
 * T.() -> Unit 和 (T) -> Unit
 *
 *
 * @author wangkai
 */
suspend fun <T : HttpResponseParsable> Flow<T>.preHandleHttpResponse(
    success: suspend (T) -> Unit,
) {
    map {
        val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(it)
        if (!isSuccess) {
            //业务执行异常
            throw RestApiException(it.code, it.message)
        }
        it
    }.catch {
        /*异常预处理*/
        GlobalHttpResponseProcessor.handleHttpError(it)
    }.flowOn(Dispatchers.IO).collect {
        success(it)
    }
}