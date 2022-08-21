package com.model.login.data

import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface LoginApiService {
    /**
     * 登录接口页 url提交
     * @param username 用户名
     * @param password 密码
     * @return [LoginData] 用户信息
     * */
    @POST("login")
    suspend fun queryLoginByCoroutine(
        @Query("username") username: String, @Query("password") password: String
    ): LoginData

    /**
     * 登录接口页 表单提交
     * @param username 用户名
     * @param password 密码
     * @return [LoginData] 用户信息
     * */
    @POST("login")
    @FormUrlEncoded
    suspend fun queryLoginBodyByCoroutine(
        @Field("username") username: String, @Field("password") password: String
    ): LoginData

    /**
     * 登录接口页表单提交
     * @param username 用户名
     * @param password 密码
     * @return [Flow] 用户信息flow
     * */
    @POST("login")
    @FormUrlEncoded
    fun queryLoginByCoroutineFlow(
        @Field("username") username: String, @Field("password") password: String
    ): Flow<LoginData>


    @GET("admin/users")
    suspend fun queryUsersByCoroutine(): UsersData
}