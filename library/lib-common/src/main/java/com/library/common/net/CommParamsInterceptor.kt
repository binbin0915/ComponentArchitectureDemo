package com.library.common.net

import com.wangkai.remote.interceptor.BaseCommParamsInterceptor

class CommParamsInterceptor(
    override val commHeaders: Map<String, String>,
    override val commBodyParams: Map<String, String>
) : BaseCommParamsInterceptor()