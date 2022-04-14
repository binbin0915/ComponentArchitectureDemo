package com.wangkai.myapplication

import ando.file.core.FileOperator
import com.library.base.application.BaseApplication
import com.library.common.netconfig.GlobalResponseHandler
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.umeng.commonsdk.UMConfigure
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor


/**
 * 作用描述：壳项目MainApplication
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class MainApplication : BaseApplication() {

    override fun appInit() {
        /*文件系统初始化*/
        FileOperator.init(instance, BuildConfig.DEBUG)

        /*---------------------------------------tbs相关---------------------------------------*/
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        //（可选）为了提高内核占比，在初始化前可配置允许移动网络下载内核（大小 40-50 MB）。默认移动网络不下载
        QbSdk.setDownloadWithoutWifi(true)
        // tbs初始化回调
        val cb: PreInitCallback = object : PreInitCallback {
            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param arg0 是否使用X5内核，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
             */
            override fun onViewInitFinished(arg0: Boolean) {

            }

            /**
             * 内核初始化完成，可能为系统内核，也可能为系统内核
             */
            override fun onCoreInitFinished() {

            }
        }
        QbSdk.initX5Environment(appContext, cb)

        /*---------------------------------------友盟相关---------------------------------------*/
        // 友盟的预初始化
        UMConfigure.preInit(this, "624aef686adb343c47f143a4", "test")
        // 友盟的初始化
        // 注意:
        //  1. 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，
        //  2. UMConfigure.init调用中appkey和channel参数请置为null）。
        UMConfigure.init(
            appContext, "624aef686adb343c47f143a4", "test", UMConfigure.DEVICE_TYPE_PHONE, ""
        )
        // 友盟日志
        UMConfigure.setLogEnabled(true)

        // 设置全局http响应
        GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())
    }
}