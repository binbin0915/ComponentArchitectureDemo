package com.library.common.net

import com.library.base.application.BaseApplication
import com.library.base.datastore.DataStoreUtils
import com.wangkai.remote.interceptor.BaseCommParamsInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
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
        val flow = flow<String> {
            emit(DataStoreUtils.get(BaseApplication.appContext, "Authorization"))
        }
        CoroutineScope(Dispatchers.Main).launch {
            flow.collect {
                headersParam["Authorization"] = it
            }
        }
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