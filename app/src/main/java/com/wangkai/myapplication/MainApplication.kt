package com.wangkai.myapplication

import ando.file.core.FileOperator
import android.content.Intent
import android.text.TextUtils
import com.drake.statelayout.StateConfig
import com.library.base.application.BaseApplication
import com.library.logcat.Logcat
import com.library.logcat.LogcatLevel
import com.library.logcat.LogcatTag
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsListener
import com.tencent.smtt.sdk.WebView
import com.wangkai.myapplication.bean.UpdateModel
import com.wangkai.remote.tools.handler.GlobalHttpResponseProcessor
import com.wangkai.upload.interfaces.AppUpdateInfoListener
import com.wangkai.upload.model.TypeConfig
import com.wangkai.upload.model.UpdateConfig
import com.wangkai.upload.utils.AppUpdateUtils
import org.acra.ACRA
import org.acra.config.httpSender
import org.acra.data.StringFormat
import org.acra.ktx.initAcra
import org.acra.sender.HttpSender
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


        /*---------------------------------------崩溃日志系统初始化--------------------------------------*/

        ACRA.DEV_LOGGING = BuildConfig.DEBUG
        initAcra {
            //core configuration:
            buildConfigClass = BuildConfig::class.java
            reportFormat = StringFormat.JSON
            httpSender {
                uri = App.ACRA_HTTP
                basicAuthLogin = App.ACRA_AuthLogin
                basicAuthPassword = App.ACRA_AuthPassword
                httpMethod = HttpSender.Method.POST
            }
        }

        //以下部分详细介绍了崩溃报告的可能目标：服务器后端、电子邮件或您可以想象的任何其他目标（如果您实现发件人）。您甚至可以将报告发送到多个目标位置。
        //所有官方报告发件人都支持两种类型的报告格式：和 （http 的表单数据兼容）。选择您的后端需要或您最喜欢的任何一个：StringFormat.JSON | StringFormat.KEY_VALUE_LIST
        //无需用户交互即可发送报告的最便捷方式是通过 HTTP。


        /*---------------------------------------文件操作系统初始化--------------------------------------*/
        FileOperator.init(instance, BuildConfig.DEBUG)

        /*----------------------------------------tbs相关------------------------------------------*/

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
                    Logcat.log(LogcatTag.APP_TBS_TAG, "腾讯X5内核 预初始化结束:$arg0")
                }

                /**
                 * 内核初始化完成，可能为系统内核，也可能为系统内核
                 */
                override fun onCoreInitFinished() {
                    Logcat.log(LogcatTag.APP_TBS_TAG, "腾讯X5内核 内核初始化完成")
                }
            }
            QbSdk.setTbsListener(object : TbsListener {
                /**
                 * @param stateCode 用户可处理错误码
                 */
                override fun onDownloadFinish(stateCode: Int) {
                    Logcat.log(LogcatTag.APP_TBS_TAG, "腾讯X5内核 下载结束：$stateCode")
                }

                /**
                 * @param stateCode
                 */
                override fun onInstallFinish(stateCode: Int) {
                    Logcat.log(LogcatTag.APP_TBS_TAG, "腾讯X5内核 安装完成：$stateCode")
                }

                /**
                 * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
                 * @param progress 0 - 100
                 */
                override fun onDownloadProgress(progress: Int) {
                    Logcat.log(LogcatTag.APP_TBS_TAG, "腾讯X5内核 下载进度:$progress%")
                }
            })
            if (!QbSdk.isTbsCoreInited()) {
                // preInit只需要调用一次，如果已经完成了初始化，那么就直接构造view
                Logcat.log(LogcatLevel.ERROR, LogcatTag.APP_TBS_TAG, "预加载中...preInitX5WebCore")
                QbSdk.preInit(applicationContext, cb)// 设置X5初始化完成的回调接口
            }
            QbSdk.initX5Environment(appContext, cb)
        }/*------------------------------------设置全局http响应-----------------------------------*/
        GlobalHttpResponseProcessor.setResponseHandler(GlobalResponseHandler())

        /*-------------------------------------BRV相关------------------------------------------*/

        StateConfig.apply {
            // 推荐在Application中进行全局配置缺省页, 当然同样每个页面可以单独指定缺省页.
            // 具体查看 https://github.com/liangjingkanji/StateLayout
            emptyLayout = com.library.widget.R.layout.widget_mult_state_empty_retry
            errorLayout = com.library.widget.R.layout.widget_mult_network_state_error
            loadingLayout = com.library.widget.R.layout.widget_mult_state_loading
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
        }/*-------------------------------------更新相关------------------------------------------*/
        //设置请求头
        val headers: MutableMap<String, Any> = HashMap()
        headers["APP-Key"] = "APP-Secret"
        headers["APP-Secret"] = "APP-Secret"
        //设置请求体
        val params: MutableMap<String, Any> = HashMap()
        params["channel"] = "beisu100"
        params["cv"] = "1"
        val updateConfig = UpdateConfig(
            baseUrl = App.Update_URL,
            requestHeaders = headers,
            requestParams = params,
            modelClass = UpdateModel(),
        ).apply {
            isDebug = true
            isAutoDownloadBackground = false
            notificationIconRes = R.mipmap.app_ic_launcher
            isShowNotification = true
            dataSourceType = TypeConfig.DATA_SOURCE_TYPE_URL
            uiThemeType = TypeConfig.UI_THEME_A
        }
        AppUpdateUtils.init(this, updateConfig)

        AppUpdateUtils.getInstance().addAppUpdateInfoListener(object : AppUpdateInfoListener {
            override fun isLatestVersion(isLatest: Boolean) {
                Logcat.log(LogcatTag.APP_UPLOAD_TAG, "isLatest:$isLatest")
            }
        })
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
        Logcat.log(LogcatTag.APP_TBS_TAG, "进程名：$currentProcessName")
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