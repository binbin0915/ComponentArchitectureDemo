package com.library.base.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import kotlin.properties.Delegates

/**
 * 作用描述：Application的基类
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
abstract class BaseApplication : Application() {
    //instance
    companion object{
        var appContext: Context by Delegates.notNull()
            private set
        lateinit var instance : BaseApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = this.applicationContext
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