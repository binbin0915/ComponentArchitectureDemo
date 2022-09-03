package com.model.home.application

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import com.alibaba.android.arouter.launcher.ARouter
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.MaterialStyle
import com.library.base.application.BaseModelApplication
import com.model.home.BuildConfig

/**
 * 作用描述：首页Application
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeModelApplication : BaseModelApplication() {

    override fun attachBaseContext(context: Context) {
    }

    override fun onCreate(application: Application) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(application)
        //初始化
        DialogX.init(application)
        //开启调试模式，在部分情况下会使用 Log 输出日志信息
        DialogX.DEBUGMODE = true
        //设置主题样式
        DialogX.globalStyle = MaterialStyle.style()
        //是否自动在主线程执行
        DialogX.autoRunOnUIThread = true
        //设置亮色/暗色（在启动下一个对话框时生效）
        DialogX.globalTheme = DialogX.THEME.AUTO
        //设置 BottomDialog 导航栏背景颜色
        DialogX.bottomDialogNavbarColor = Color.TRANSPARENT
    }

    override fun onLowMemory() {
    }

    override fun onTerminate() {
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
    }
}