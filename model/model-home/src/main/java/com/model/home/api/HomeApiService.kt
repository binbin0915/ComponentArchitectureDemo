package com.model.home.api

import com.library.network.data.NetworkData

/**
 * 作用描述：首页模块Api服务
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
interface HomeApiService {
    suspend fun getData(): NetworkData<String>
}