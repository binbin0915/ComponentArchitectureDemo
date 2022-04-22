package com.model.airpods.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.library.base.application.BaseModelApplication
import com.library.common.netconfig.GlobalResponseHandler
import com.model.airpods.BuildConfig
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor

/**
 * 作用描述：模块独有的初始化
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class AirpodsModelApplication : BaseModelApplication() {
    override fun attachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        Log.e("AAAAAAAAAAAAA","AirpodsModelApplication已经初始化")
        ARouter.init(application)
        if (BuildConfig.IS_RUN_MODEL) {
            //已模块化运行的话 需要初始化网络请求框架
            GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())
        }
    }

    override fun onLowMemory() {
    }

    override fun onTerminate() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }
}