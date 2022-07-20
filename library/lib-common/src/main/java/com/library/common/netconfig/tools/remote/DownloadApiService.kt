package com.library.common.netconfig.tools.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 文件下载的Retrofit API声明
 *
 * 大文件官方建议用 [Streaming]来进行注解，不然会出现IO异常，小文件可以忽略不注入
 * @author 王凯
 * @date 2020/07/20
 */
interface DownloadApiService {
    /**
     * 协程下载
     * @param url 文件下载完整地址
     */
    @Streaming
    @GET
    suspend fun download(@Url url: String): ResponseBody
}