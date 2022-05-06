package com.model.login.data

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginApiService {
    /**
     * 登录接口页
     * @param username 用户名
     * @param password 密码
     * */
    @POST("api/login")
    suspend fun queryLoginByCoroutine(
        @Query("username") username: String, @Query("password") password: String
    ): LoginData
}