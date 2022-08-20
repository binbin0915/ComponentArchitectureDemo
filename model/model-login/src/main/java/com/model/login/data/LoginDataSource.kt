package com.model.login.data

import kotlinx.coroutines.flow.Flow

/**
 * 远程网络数据源
 * @author WangKai
 * @date 2022/04/03
 */
object LoginDataSource {

    private val mApiService: LoginApiService by loginApiDelegate()

    suspend fun queryLoginByCoroutine(username: String, password: String): LoginData{
        return mApiService.queryLoginByCoroutine(username, password)
    }

    fun queryLoginByCoroutineFlow(username: String, password: String): Flow<LoginData> {
        return mApiService.queryLoginByCoroutineFlow(username, password)
    }
}