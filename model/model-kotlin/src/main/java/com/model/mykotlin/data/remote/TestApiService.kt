package com.model.mykotlin.data.remote

import com.model.mykotlin.data.entity.WanAndroidArticleListResponseEntity
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 测试的API接口
 * @author WangKai
 * @date 2022/04/07
 */
interface TestApiService {
    /**
     * 基于kotlin 协程，获取WanAndroid的文章列表
     * @param pageNum 列表分页页数
     * */
    @GET("article/list/{pageNum}/json")
    suspend fun queryWanAndroidArticleByCoroutine(
        @Path("pageNum") pageNum: Int
    ): WanAndroidArticleListResponseEntity
}