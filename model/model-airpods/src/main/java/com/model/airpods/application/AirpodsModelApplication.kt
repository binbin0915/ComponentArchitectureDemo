package com.model.airpods.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.alibaba.android.arouter.launcher.ARouter
import com.library.base.application.BaseModelApplication
import com.model.airpods.BuildConfig

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
        ARouter.init(application)
    }

    override fun onLowMemory() {
    }

    override fun onTerminate() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }
}