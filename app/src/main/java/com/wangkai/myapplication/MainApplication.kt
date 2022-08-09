package com.wangkai.myapplication

import ando.file.core.FileOperator
import android.content.Context
import android.os.Environment
import android.view.View
import com.drake.statelayout.StateConfig
import com.library.base.application.BaseApplication
import com.library.common.netconfig.GlobalResponseHandler
import com.model.home.HomeMainActivity
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.ui.UILifecycleListener
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

        // 设置全局http响应
        GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())

        /*-------------------------------------BRV相关------------------------------------------*/
        /**
         *  推荐在Application中进行全局配置缺省页, 当然同样每个页面可以单独指定缺省页.
         *  具体查看 https://github.com/liangjingkanji/StateLayout
         */
        StateConfig.apply {
            emptyLayout = com.library.widget.R.layout.mult_state_empty_retry
            errorLayout = com.library.widget.R.layout.mult_network_state_error
            loadingLayout = com.library.widget.R.layout.mult_state_loading
            setRetryIds(com.library.widget.R.id.retryBtn)
            onLoading {
                // 此生命周期可以拿到LoadingLayout创建的视图对象, 可以进行动画设置或点击事件.
            }
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }


        /*--------------------------------------Bugly相关----------------------------------------*/
        /**
         * 参数1：上下文对象
         * 参数2：注册时申请的APPID
         * 参数3：是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
         */
        Bugly.init(applicationContext, "eb89618c65", true)
//        /**
//         * 参数1：isManual 用户手动点击检查，非用户点击操作请传false
//         * 参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
//         */
//        Beta.checkUpgrade(false, false)
//        /*Bugly Android 应用升级 SDK 高级配置*/
//
//        // 1.true表示app启动自动初始化升级模块; false不会自动初始化; 开发者如果担心sdk初始化影响app启动速度，可以设置为false，在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
//        Beta.autoInit = true
//        // 2.设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
//        // Beta.initDelay = 1 * 1000;
//        // 3.true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
//        Beta.autoCheckUpgrade = true
//        // 4.设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
//        Beta.upgradeCheckPeriod = 60 * 1000
//        // 5.设置通知栏大图标
//        Beta.largeIconId = R.mipmap.app_ic_launcher
//        // 6.设置状态栏小图标
//        Beta.smallIconId = R.mipmap.app_ic_launcher
//        // 7.设置更新弹窗默认展示的banner(defaultBannerId为项目中的图片资源Id; 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading...“;)
//        Beta.defaultBannerId = R.mipmap.app_ic_launcher
//        // 8.设置sd卡的Download为更新资源存储目录（后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;）
//        Beta.storageDir =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        // 9.设置开启显示打断策略（设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。）
//        Beta.showInterruptedStrategy = true
//        // 10.添加可显示弹窗的Activity(例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。)
//        Beta.canShowUpgradeActs.add(HomeMainActivity::class.java)
//        // 11.设置自定义升级对话框UI布局
//        /**
//         * upgrade_dialog为项目的布局资源。 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用： - 特性图片：beta_upgrade_banner，如：android:tag="beta_upgrade_banner"
//         * 标题：beta_title，如：android:tag="beta_title"
//         * 升级信息：beta_upgrade_info 如： android:tag="beta_upgrade_info"
//         * 更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
//         * 取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
//         * 确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
//         */
////        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog
//        // 12.设置自定义tip弹窗UI布局
//        /**
//         * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
//         * 标题：beta_title，如：android:tag="beta_title"
//         * 提示信息：beta_tip_message 如： android:tag="beta_tip_message"
//         * 取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
//         * 确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
//         */
////        Beta.tipsDialogLayoutId = R.layout.tips_dialog
//
//        //13.设置升级对话框生命周期回调接口
//        Beta.upgradeDialogLifecycleListener = object : UILifecycleListener<UpgradeInfo?> {
//            override fun onCreate(context: Context, view: View, upgradeInfo: UpgradeInfo?) {
//
//            }
//
//            override fun onStart(context: Context, view: View, upgradeInfo: UpgradeInfo?) {
//
//            }
//
//            override fun onResume(context: Context, view: View, upgradeInfo: UpgradeInfo?) {
//                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:
////                // 通过id方式获取控件，并更改imageview图片
////                val imageView: ImageView = view.findViewById(R.id.icon) as ImageView
////                imageView.setImageResource(R.mipmap.ic_launcher)
////
////                // 通过tag方式获取控件，并更改布局内容
////                view.findViewWithTag("textview").text = "my custom text"
////
////                // 更多的操作：比如设置控件的点击事件
////                imageView.setOnClickListener(object : OnClickListener() {
////                    fun onClick(v: View?) {
////                        val intent = Intent(applicationContext, OtherActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
////                        startActivity(intent)
////                    }
////                })
//            }
//
//            override fun onPause(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
//
//            }
//
//            override fun onStop(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
//
//            }
//
//            override fun onDestroy(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
//
//            }
//        }
//        // 14. 设置是否显示消息通知(如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。)
//        Beta.enableNotification = true
//        // 15.设置Wifi下自动下载
//        Beta.autoDownloadOnWifi = false
//        // 16.设置是否显示弹窗中的apk信息（如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。）
//        Beta.canShowApkInfo = true
//        // 17.关闭热更新能力（升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。）
//        Beta.enableHotfix = true
    }
}