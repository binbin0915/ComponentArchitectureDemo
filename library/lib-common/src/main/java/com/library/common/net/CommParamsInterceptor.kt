package com.library.common.net

import com.wangkai.remote.interceptor.BaseCommParamsInterceptor
import java.util.concurrent.ConcurrentHashMap

/**
 * 公共参数拦截器
 */
class CommParamsInterceptor : BaseCommParamsInterceptor() {
    private var commPathParam = ConcurrentHashMap<String, String>()
    private var commBodyParam = ConcurrentHashMap<String, String>()
    private var headersParam = ConcurrentHashMap<String, String>()

    /**
     * 在这添加参数
     */
    init {

    }

    override val pathParams: ConcurrentHashMap<String, String>
        get() = commPathParam
    override val bodyParams: ConcurrentHashMap<String, String>
        get() = commBodyParam
    override val headersParams: ConcurrentHashMap<String, String>
        get() = headersParam
    override val excludeUrls: List<String>
        get() = listOf("api/login")
}