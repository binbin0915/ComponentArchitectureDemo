package com.model.splash.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.alibaba.android.arouter.launcher.ARouter
import com.library.base.application.BaseModelApplication

/**
 * 作用描述：开屏页Application
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class SplashModelApplication : BaseModelApplication() {

    override fun attachBaseContext(context: Context) {
        Log.i("===>>>", "SplashModelApplication -> attachBaseContext")
    }

    override fun onCreate(application: Application) {
        Log.i("===>>>", "SplashModelApplication -> onCreate")
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(application)
    }

    override fun onLowMemory() {
        Log.i("===>>>", "SplashModelApplication -> onLowMemory")
    }

    override fun onTerminate() {
        Log.i("===>>>", "SplashModelApplication -> onTerminate")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.i("===>>>", "SplashModelApplication -> onConfigurationChanged")
    }
}