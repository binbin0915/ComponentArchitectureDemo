package com.library.common.net

import com.wangkai.remote.interceptor.BaseCommParamsInterceptor

/**
 * 公共参数拦截器
 */
class CommParamsInterceptor : BaseCommParamsInterceptor() {
    var commHeader = mutableMapOf<String, String>()
    var commBodyParam = mutableMapOf<String, String>()

    init {
        commHeader["testHeader"] = "testHeader"
        commBodyParam["testBody"] = "testBody"
    }

    override val commHeaders: Map<String, String>
        get() = commHeader
    override val commBodyParams: Map<String, String>
        get() = commBodyParam

}