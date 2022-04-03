package com.wangkai.myapplication

import android.util.Log
import com.library.base.application.BaseApplication
import com.library.common.GlobalResponseHandler
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor


/**
 * 作用描述：壳项目MainApplication
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class MainApplication : BaseApplication() {

    override fun appInit() {
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
                Log.e("AAAAAAAAAAAAAAA", "内核加载成功：$arg0")
            }

            /**
             * 内核初始化完成，可能为系统内核，也可能为系统内核
             */
            override fun onCoreInitFinished() {
                Log.e("AAAAAAAAAAAAAAA", "内核初始化完成")
            }
        }
        QbSdk.initX5Environment(appContext, cb)
        //设置全局http响应
        GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())
    }
}