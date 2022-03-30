package com.wangkai.myapplication

import com.library.base.application.BaseApplication
import com.library.common.GlobalResponseHandler
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor

/**
 * 作用描述：壳项目MainApplication
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class MainApplication : BaseApplication() {

    override fun appInit() {
        //设置全局http响应
        GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())
    }
}