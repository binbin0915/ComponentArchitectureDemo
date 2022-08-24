package com.wangkai.myapplication

import ando.file.core.FileOperator
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.drake.statelayout.StateConfig
import com.library.base.application.BaseApplication
import com.library.common.net.GlobalResponseHandler
import com.model.home.HomeMainActivity
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.ui.UILifecycleListener
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.tencent.smtt.sdk.WebView
import com.wangkai.remote.tools.handler.GlobalHttpResponseProcessor
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * 作用描述：壳项目MainApplication
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class MainApplication : BaseApplication() {

    override fun appInit() {
        /*---------------------------------------文件系统初始化--------------------------------------*/
        FileOperator.init(instance, BuildConfig.DEBUG)

        /*----------------------------------------tbs相关------------------------------------------*/
        /* [new] 独立Web进程 */
        if (startX5WebProcessPreInitService()) {
            val map = HashMap<String, Any>()
            map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
            map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
            // 在调用TBS初始化、创建WebView之前进行如下配置
            QbSdk.initTbsSettings(map)
            //（可选）为了提高内核占比，在初始化前可配置允许移动网络下载内核（大小 40-50 MB）。默认移动网络不下载
            QbSdk.setDownloadWithoutWifi(true)
            // tbs初始化回调
            val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
                /**
                 * 预初始化结束
                 * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
                 * @param arg0 是否使用X5内核，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                 */
                override fun onViewInitFinished(arg0: Boolean) {
                    Log.i("TBS_TAG", "腾讯X5内核 预初始化结束:$arg0")
                }

                /**
                 * 内核初始化完成，可能为系统内核，也可能为系统内核
                 */
                override fun onCoreInitFinished() {
                    Log.i("TBS_TAG", "腾讯X5内核 内核初始化完成")
                }
            }
            QbSdk.setTbsListener(object : TbsListener {
                /**
                 * @param stateCode 用户可处理错误码
                 */
                override fun onDownloadFinish(stateCode: Int) {
                    Log.i("TBS_TAG", "腾讯X5内核 下载结束：$stateCode")
                }

                /**
                 * @param stateCode
                 */
                override fun onInstallFinish(stateCode: Int) {
                    Log.i("TBS_TAG", "腾讯X5内核 安装完成：$stateCode")
                }

                /**
                 * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
                 * @param progress 0 - 100
                 */
                override fun onDownloadProgress(progress: Int) {
                    Log.i("TBS_TAG", "腾讯X5内核 下载进度:%$progress")
                }
            })
            if (!QbSdk.isTbsCoreInited()) {
                // preInit只需要调用一次，如果已经完成了初始化，那么就直接构造view
                Log.e("TBS_TAG", "预加载中...preInitX5WebCore")
                QbSdk.preInit(applicationContext, cb)// 设置X5初始化完成的回调接口

            }
            QbSdk.initX5Environment(appContext, cb)
        }
        /*------------------------------------设置全局http响应-----------------------------------*/
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
         * 手动检查升级：
         * 参数1：isManual 用户手动点击检查，非用户点击操作请传false
         * 参数2：isSilence 是否显示弹窗等交互，[true:没有弹窗和toast] [false:有弹窗或toast]
         * Beta.checkUpgrade()
         */
        /*===================Bugly Android 应用升级 SDK 高级配置===================*/
        // 1.true表示app启动自动初始化升级模块; false不会自动初始化; 开发者如果担心sdk初始化影响app启动速度，可以设置为false，在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
        Beta.autoInit = true
        // 2.设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
        // Beta.initDelay = 1 * 1000;
        // 3.true表示初始化时自动检查升级; false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
        Beta.autoCheckUpgrade = true
        // 4.设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
        // Beta.upgradeCheckPeriod = 60 * 1000
        // 5.设置通知栏大图标
        Beta.largeIconId = R.mipmap.app_ic_launcher
        // 6.设置状态栏小图标
        Beta.smallIconId = R.mipmap.app_ic_launcher
        // 7.设置更新弹窗默认展示的banner(defaultBannerId为项目中的图片资源Id; 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading...“;)
        Beta.defaultBannerId = R.mipmap.app_ic_launcher
        // 8.设置sd卡的Download为更新资源存储目录（后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;）
        // Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // 9.设置开启显示打断策略（设置点击过确认的弹窗在App下次启动自动检查更新时会再次显示。）
        Beta.showInterruptedStrategy = true
        // 10.添加可显示弹窗的Activity(例如，只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗; 如果不设置默认所有activity都可以显示弹窗。)
        Beta.canShowUpgradeActs.add(HomeMainActivity::class.java)
        // 11.自定义Activity参考，通过回调接口来跳转到你自定义的activity中。
        Beta.upgradeListener = UpgradeListener { p0, p1, p2, p3 ->
            Log.e("BUGLY_TAG", "p0:$p0   pi2:$p2   p3:$p3")
            if (p1 != null) {
                val i = Intent()
                i.setClass(
                    appContext, UpdateActivity::class.java
                )
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                appContext.startActivity(i)
            }
        }
        // 12.设置自定义tip弹窗UI布局 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：标题：beta_title，如：android:tag="beta_title",提示信息：beta_tip_message 如： android:tag="beta_tip_message",取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button",确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
        // Beta.tipsDialogLayoutId = R.layout.tips_dialog
        // 13. 设置是否显示消息通知(如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。)
        // Beta.enableNotification = true
        // 14.设置Wifi下自动下载
        // Beta.autoDownloadOnWifi = true
        // 15.设置是否显示弹窗中的apk信息（如果你使用我们默认弹窗是会显示apk信息的，如果你不想显示可以将这个接口设置为false。）
        // Beta.canShowApkInfo = true
        // 16.关闭热更新能力（升级SDK默认是开启热更新能力的，如果你不需要使用热更新，可以将这个接口设置为false。）
        // Beta.enableHotfix = true
        // 17.设置升级对话框生命周期回调接口
        Beta.upgradeDialogLifecycleListener = object : UILifecycleListener<UpgradeInfo> {
            override fun onCreate(context: Context, view: View, upgradeInfo: UpgradeInfo) {
                Log.d("BUGLY_TAG", "dialog:onCreate")
            }

            override fun onStart(context: Context, view: View, upgradeInfo: UpgradeInfo) {
                Log.d("BUGLY_TAG", "dialog:onStart")
            }

            override fun onResume(context: Context, view: View, upgradeInfo: UpgradeInfo) {
                Log.d("BUGLY_TAG", "dialog:onResume")
            }

            override fun onPause(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
                Log.d("BUGLY_TAG", "dialog:onPause")
            }

            override fun onStop(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
                Log.d("BUGLY_TAG", "dialog:onStop")
            }

            override fun onDestroy(context: Context?, view: View?, upgradeInfo: UpgradeInfo?) {
                Log.d("BUGLY_TAG", "dialog:onDestroy")
            }
        }
        // 18.更新状态监听
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgradeFailed(b: Boolean) {
                Log.d("BUGLY_TAG", "onUpgradeFailed:$b")
            }

            override fun onUpgradeSuccess(b: Boolean) {
                Log.d("BUGLY_TAG", "onUpgradeSuccess:$b")
            }

            override fun onUpgradeNoVersion(b: Boolean) {
                Log.d("BUGLY_TAG", "onUpgradeNoVersion:$b")
            }

            override fun onUpgrading(b: Boolean) {
                Log.d("BUGLY_TAG", "onUpgrading:$b")
            }

            override fun onDownloadCompleted(b: Boolean) {
                Log.d("BUGLY_TAG", "onDownloadCompleted:$b")
            }
        }
        /**
         * 参数1：上下文对象
         * 参数2：注册时申请的appId
         * 参数3：是否开启debug模式，true表示打开debug模式，false表示关闭调试模式
         */
        Bugly.init(applicationContext, "eb89618c65", true)
    }

    /**
     * 我们提供了UserStrategy类作为Bugly的初始化扩展，在这里您可以修改本次初始化Bugly数据的版本、渠道及部分初始化行为。
     * 如果通过UserStrategy设置了版本号和渠道号，则会覆盖“AndroidManifest.xml”里面配置的版本号和渠道。
     */
    private fun setBuyConfig(): CrashReport.UserStrategy {
        val strategy = CrashReport.UserStrategy(appContext)
        /* 1.设置设备id*/
        /*不再采集Android id(3.4.4及之后版本)，为了使得crash率统计更精准，建议设置业务自己的deviceId（业务唯一id）给bugly sdk。3.4.4版本前默认读取Android id作为设备id，App开发者可以在初始化bugly sdk的时候设置自定义的deviceId，为了避免合规问题，请务必升级到最新合规版本。*/
        strategy.deviceID = "userdefinedId"
        /* 2.设置设备型号*/
        strategy.deviceModel = "userdefinedModel"
        /* 3.设置App版本、渠道、包名*/
        strategy.appChannel = "myChannel"  //设置渠道
        strategy.appVersion = "1.0.1"      //App的版本
        strategy.appPackageName = "com.tencent.xx"  //App的包名
        /* 4.设置Bugly初始化延迟*/
        //strategy.appReportDelay = 20000   //改为20s
        /* 5.如果App使用了多进程且各个进程都会初始化Bugly（例如在Application类onCreate()中初始化Bugly），那么每个进程下的Bugly都会进行数据上报，造成不必要的资源浪费。因此，为了节省流量、内存等资源，建议初始化的时候对上报进程进行控制，只在主进程下上报数据：判断是否是主进程（通过进程名是否为包名来判断），并在初始化Bugly时增加一个上报进程的策略配置。*/
        val packageName = appContext.packageName
        val processName = getProcessName(android.os.Process.myPid())
        strategy.isUploadProcess = processName == null || processName == packageName

        /*TODO:更多的Bugly日志附加信息*/
        /*1.该用户本次启动后的异常日志用户ID都将是9527*/
        CrashReport.setUserId("")
        /*2、主动上报开发者Catch的异常 您可能会关注某些重要异常的Catch情况。我们提供了上报这类异常的接口。 例：统计某个重要的数据库读写问题比例。*/
//        try {
//            //...
//        } catch (thr: Throwable) {
//            CrashReport.postCatchedException(thr) // bugly会将这个throwable上报
//        }
        /*3、自定义日志功能 我们提供了自定义Log的接口，用于记录一些开发者关心的调试日志，可以更全面地反应App异常时的前后文环境。使用方式与android.util.Log一致。用户传入TAG和日志内容。该日志将在Logcat输出，并在发生异常时上报。有如下*/
//        BuglyLog.v(tag, log)
//        BuglyLog.d(tag, log)
//        BuglyLog.i(tag, log)
//        BuglyLog.w(tag, log)
//        BuglyLog.e(tag, log)
/*注意：
使用BuglyLog接口时，为了减少磁盘IO次数，我们会先将日志缓存在内存中。当缓存大于一定阈值（默认10K），会将它持久化至文件。您可以通过setCache(int byteSize)接口设置缓存大小，范围为0-30K。例：BuglyLog.setCache(12 * 1024) //将Cache设置为12K
如果您没有使用BuglyLog接口，且初始化Bugly时isDebug参数设置为false，该Log功能将不会有新的资源占用；
为了方便开发者调试，当初始化Bugly的isDebug参数为true时，异常日志同时还会记录Bugly本身的日志。请在App发布时将其设置为false；
上报Log最大30K。*/
        /* 5.设置开发设备*/
//        CrashReport.setIsDevelopmentDevice(appContext, BuildConfig.DEBUG)
        return strategy
    }

    /**
     * 启动X5 独立Web进程的预加载服务。优点：
     * 1、后台启动，用户无感进程切换
     * 2、启动进程服务后，有X5内核时，X5预加载内核
     * 3、Web进程Crash时，不会使得整个应用进程crash掉
     * 4、隔离主进程的内存，降低网页导致的App OOM概率。
     *
     * 缺点：
     * 进程的创建占用手机整体的内存，demo 约为 150 MB
     */
    private fun startX5WebProcessPreInitService(): Boolean {
        val currentProcessName = QbSdk.getCurrentProcessName(this)
        // 设置多进程数据目录隔离，不设置的话系统内核多个进程使用WebView会crash，X5下可能ANR
        WebView.setDataDirectorySuffix(QbSdk.getCurrentProcessName(this))
        Log.i("TBS_TAG", currentProcessName)
        if (currentProcessName == this.packageName) {
            startService(Intent(this, X5ProcessInitService::class.java))
            return true
        }
        return false
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }
}