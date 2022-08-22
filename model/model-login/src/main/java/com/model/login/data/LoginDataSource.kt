package com.model.login.data

import com.library.common.net.tools.json.JsonUtils
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 远程网络数据源
 * @author WangKai
 * @date 2022/04/03
 */
object LoginDataSource {

    private val mApiService: LoginApiService by loginApiDelegate()

    suspend fun queryLoginByCoroutine(
        token: String, username: String, password: String
    ): LoginData {
        val params: MutableMap<String, String> = LinkedHashMap()
        params["username"] = username
        params["password"] = password
        val requestBody: RequestBody = JsonUtils.toJson(params)
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return mApiService.queryLoginByCoroutine(token, username, password)
    }

    suspend fun queryLoginBodyByCoroutine(
        token: String, username: String, password: String
    ): LoginData {
        val params: MutableMap<String, String> = LinkedHashMap()
        params["username"] = username
        params["password"] = password
        return mApiService.queryLoginBodyByCoroutine(token, username, password)
    }

    fun queryLoginByCoroutineFlow(username: String, password: String): Flow<LoginData> {
        return mApiService.queryLoginByCoroutineFlow(username, password)
    }

    suspend fun queryUsersByCoroutine(): UsersData {
        return mApiService.queryUsersByCoroutine()
    }
}