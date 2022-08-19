package com.model.login.data

import com.wangkai.remote.tools.handler.GlobalHttpResponseProcessor
import com.wangkai.remote.tools.handler.RestApiException

/**
 * 远程网络数据源
 *
 * 基本使用
 * *
 *
 *
 *
 *
 *
 * @author WangKai
 * @date 2022/04/03
 */
object LoginDataSource {

    private val mApiService: LoginApiService by loginApiDelegate()

    suspend fun queryLoginByCoroutine(username: String, password: String): LoginData {
        val result = mApiService.queryLoginByCoroutine(username, password)
        val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(result)
        if (!isSuccess) {
            //业务执行异常
            throw RestApiException(result.code, result.message)
        }
        //业务执行成功
        return result
    }
}