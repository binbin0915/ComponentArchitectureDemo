package com.library.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.library.base.utils.ApplicationInitUtils

/**
 * 作用描述：Application的基类
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInit()
        ApplicationInitUtils.initOnCreate(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        ApplicationInitUtils.initAttachBaseContext(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ApplicationInitUtils.initOnLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
        ApplicationInitUtils.initOnTerminate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        ApplicationInitUtils.initOnConfigurationChanged(newConfig)
    }

    /**
     * APP初始化一些东西
     */
    abstract fun appInit()
}