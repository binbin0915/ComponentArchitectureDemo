package com.model.mykotlin.data.remote

import com.model.mykotlin.data.delegate.wanAndroidApiDelegate
import com.model.mykotlin.data.entity.WanAndroidArticleListResponseEntity
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import com.yupfeg.remote.tools.handler.RestApiException

/**
 * 远程网络数据源
 *
 * 基本使用
 * * 接口请求
 * * 上传
 * * 下载
 * @author WangKai
 * @date 2022/04/03
 */
object RemoteDataSource {

    /**
     * 网络请求retrofit api接口的委托
     * * 通过by关键字委托创建api接口实例
     * */
    private val mApiService: TestApiService by wanAndroidApiDelegate()
    /**
     * 基于kotlin 协程获取wanAndroid的文章列表数据
     * @param pageIndex 分页页数
     * */
    suspend fun queryWanAndroidArticleByCoroutine(pageIndex: Int): WanAndroidArticleListResponseEntity {
        val result = mApiService.queryWanAndroidArticleByCoroutine(pageIndex)
        val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(result)
        if (!isSuccess) {
            //业务执行异常
            throw RestApiException(result.code, result.message)
        }
        //业务执行成功
        return result
    }
}