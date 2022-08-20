package com.library.common.net

import com.wangkai.remote.interceptor.BaseCommParamsInterceptor
import java.util.concurrent.ConcurrentHashMap

/**
 * 公共参数拦截器
 */
class CommParamsInterceptor : BaseCommParamsInterceptor() {
    private var commPathParam = ConcurrentHashMap<String, String>()
    private var commBodyParam = ConcurrentHashMap<String, String>()

    /**
     * 在这添加参数
     */
    init {
        commPathParam["testHeader111"] = "testHeader111"
        commPathParam["testHeader222"] = "testHeader222"
        commBodyParam["testBody111"] = "testBody111"
        commBodyParam["testBody222"] = "testBody222"
    }

    override val pathParams: ConcurrentHashMap<String, String>
        get() = commPathParam
    override val bodyParams: ConcurrentHashMap<String, String>
        get() = commBodyParam
    override val excludeUrls: List<String>
        get() = listOf()
}