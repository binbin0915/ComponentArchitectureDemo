package com.model.center.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.alibaba.android.arouter.launcher.ARouter
import com.library.base.application.BaseModelApplication
import com.library.common.netconfig.GlobalResponseHandler
import com.model.center.BuildConfig
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor

class CenterModelApplication : BaseModelApplication(){
    override fun attachBaseContext(context: Context) {

    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
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