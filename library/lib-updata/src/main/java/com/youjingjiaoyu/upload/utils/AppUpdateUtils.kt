package com.youjingjiaoyu.upload.utils

import android.app.Application
import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.library.common.network.tools.json.JsonUtils
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadLargeFileListener
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.liulishuo.filedownloader.util.FileDownloadHelper.ConnectionCreator
import com.liulishuo.filedownloader.util.FileDownloadUtils
import com.youjingjiaoyu.upload.activity.UpdateBackgroundActivity
import com.youjingjiaoyu.upload.activity.UpdateType3Activity
import com.youjingjiaoyu.upload.interfaces.AppDownloadListener
import com.youjingjiaoyu.upload.interfaces.AppUpdateInfoListener
import com.youjingjiaoyu.upload.interfaces.MD5CheckListener
import com.youjingjiaoyu.upload.model.DownloadInfo
import com.youjingjiaoyu.upload.model.LibraryUpdateEntity
import com.youjingjiaoyu.upload.model.TypeConfig
import com.youjingjiaoyu.upload.model.UpdateConfig
import com.youjingjiaoyu.upload.net.HttpCallbackModelListener
import com.youjingjiaoyu.upload.net.HttpUtils.doGet
import com.youjingjiaoyu.upload.net.HttpUtils.doPost
import com.youjingjiaoyu.upload.service.UpdateReceiver.Companion.cancelDownload
import com.youjingjiaoyu.upload.service.UpdateReceiver.Companion.send
import com.youjingjiaoyu.upload.utils.AppUtils.delAllFile
import com.youjingjiaoyu.upload.utils.AppUtils.deleteFile
import com.youjingjiaoyu.upload.utils.AppUtils.getAppLocalPath
import com.youjingjiaoyu.upload.utils.AppUtils.getAppRootPath
import com.youjingjiaoyu.upload.utils.AppUtils.getVersionCode
import com.youjingjiaoyu.upload.utils.AppUtils.installApkFile
import java.io.File

/**
 * app更新的工具类
 * @author wangkai
 */
class AppUpdateUtils private constructor() {
    companion object {
        //下载任务
        private lateinit var downloadTask: BaseDownloadTask

        //本地保留下载信息
        private lateinit var downloadInfo: DownloadInfo

        //给框架提供上下文对象
        private lateinit var mContext: Application

        //更新工具类
        private lateinit var updateUtils: AppUpdateUtils

        //更新框架配置
        private lateinit var updateConfig: UpdateConfig

        //是否初始化
        private var isInit = false

        //是否开始下载
        private var isDownloading = false

        //apk下载的路径
        private var downloadUpdateApkFilePath = ""

        //AppDownloadListener的集合
        private val appDownloadListenerList: MutableList<AppDownloadListener> = ArrayList()

        //MD5校验监听
        private val md5CheckListenerList: MutableList<MD5CheckListener> = ArrayList()

        //更新信息回调
        private val appUpdateInfoListenerList: MutableList<AppUpdateInfoListener> = ArrayList()

        fun getInstance(): AppUpdateUtils {
            return updateUtils
        }

        fun isDownloading(): Boolean {
            checkInit()
            return isDownloading
        }

        /**
         * 初始化检测
         *
         * @return
         */
        private fun checkInit() {
            if (!isInit) {
                throw RuntimeException("AppUpdateUtils需要先调用init方法进行初始化才能使用")
            }
        }

        /**
         * 全局初始化，必须调用
         *
         * @param context 上下文对象
         * @param config 更新的配置
         */
        fun init(context: Application, config: UpdateConfig) {
            if (isInit) return
            isInit = true
            mContext = context
            updateConfig = config
            updateUtils = AppUpdateUtils()
            ResUtils.init(context)
            //初始化文件下载库
            val fileDownloadConnection: ConnectionCreator =
                updateConfig.customDownloadConnectionCreator ?: FileDownloadUrlConnection.Creator(
                    FileDownloadUrlConnection.Configuration()
                        .connectTimeout(30000) // set connection timeout.
                        .readTimeout(30000) // set read timeout.
                )
            FileDownloader.setupOnApplicationOnCreate(mContext)
                .connectionCreator(fileDownloadConnection).commit()
        }


        /**
         * 移除所有监听
         */
        fun clearAllListener() {
            md5CheckListenerList.clear()
            appDownloadListenerList.clear()
        }
    }

    /**
     * 检查更新 sdk自助请求接口
     */
    fun checkUpdate() {
        checkInit()
        if (getUpdateConfig().dataSourceType != TypeConfig.DATA_SOURCE_TYPE_URL) {
            LogUtils.log("使用 DATA_SOURCE_TYPE_URL 这种模式的时候，必须要配置UpdateConfig中的dataSourceType参数才为 DATA_SOURCE_TYPE_URL ")
            return
        }
        if (TextUtils.isEmpty(getUpdateConfig().baseUrl)) {
            LogUtils.log("使用 DATA_SOURCE_TYPE_URL 这种模式的时候，必须要配置UpdateConfig中的baseUrl参数不为空才可使用")
            return
        }
        getData()
    }

    /**
     * 检查更新 sdk自主解析json为modelClass 实现自动更新
     *
     * @param jsonData
     */
    fun checkUpdate(jsonData: String) {
        val updateConfig = getUpdateConfig()
        if (updateConfig.dataSourceType != TypeConfig.DATA_SOURCE_TYPE_JSON) {
            LogUtils.log("使用 DATA_SOURCE_TYPE_JSON 这种模式的时候，必须要配置UpdateConfig中的dataSourceType参数为 DATA_SOURCE_TYPE_JSON ")
            return
        }
        if (updateConfig.modelClass !is LibraryUpdateEntity) {
            LogUtils.log("使用 DATA_SOURCE_TYPE_JSON 这种模式的时候，必须要配置UpdateConfig中的modelClass参数，并且modelClass必须实现LibraryUpdateEntity接口")
            return
        }
        try {
            //反序列化
            JsonUtils.fromJson(
                jsonData, (updateConfig.modelClass as LibraryUpdateEntity).javaClass
            )?.let { requestSuccess(it) }
        } catch (e: Exception) {
            LogUtils.log("JSON解析异常，您提供的json数据无法正常解析成为modelClass")
        }
    }

    /**
     * 检查更新 调用者配置数据 最终三种方式都会到这里来 所以要做静默下载 在这里做就好了
     */
    private fun checkUpdate(info: DownloadInfo) {
        checkInit()
        //检查当前版本是否需要更新 如果app当前的版本号大于等于线上最新的版本号 不需要升级版本
        val versionCode = getVersionCode(mContext)
        if (versionCode >= info.prodVersionCode) {
            listenToUpdateInfo(true)
            return
        }

        //通知当前版本不是最新版本
        listenToUpdateInfo(false)
        val updateConfig = getUpdateConfig()
        //如果用户开启了静默下载 其实是否开启强制更新已经没有意义了
        if (!updateConfig.isAutoDownloadBackground) {
            //检查是否强制更新
            if (info.forceUpdateFlag != 0) {
                //需要强制更新
                if (info.forceUpdateFlag == 2) {
                    //hasAffectCodes拥有字段强制更新
                    val hasAffectCodes = info.hasAffectCodes
                    if (!TextUtils.isEmpty(hasAffectCodes)) {
                        val codes = listOf(*hasAffectCodes.split("\\|".toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()
                        )
                        if (codes.contains(versionCode.toString() + "")) {
                            //包含这个版本 所以需要强制更新
                            info.forceUpdateFlag = 2
                        } else {
                            //不包含这个版本 所以此版本不需要强制更新
                            info.forceUpdateFlag = 0
                        }
                    }
                } else {
                    //所有拥有字段强制更新
                    info.forceUpdateFlag = 1
                }
            }
        }


        //检查sdk的挂载 未挂载直接阻断
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            LogUtils.log("sdk卡未加载")
            return
        }
        var type = updateConfig.uiThemeType
        if (type == TypeConfig.UI_THEME_AUTO) {
            //随机样式
            type = TypeConfig.UI_THEME_A
        } else if (type == TypeConfig.UI_THEME_CUSTOM) {
            //回调接口
            updateConfig.customActivityClass?.startActivity(info)
            return
        }

        //如果用户开启了静默下载 就不需要展示更新页面了
        if (!updateConfig.isAutoDownloadBackground) {
            //根据类型选择对应的样式
            when (type) {
                TypeConfig.UI_THEME_A -> UpdateType3Activity.launch(mContext, info)
                TypeConfig.UI_THEME_B -> {
                    UpdateBackgroundActivity.launch(mContext, info)
                    //移除掉之前的事件监听 因为用不到了根本就
                    clearAllListener()
                }

                else -> {
                    UpdateBackgroundActivity.launch(mContext, info)
                    clearAllListener()
                }
            }
        } else {
            //直接下载
            UpdateBackgroundActivity.launch(mContext, info)
            //移除掉之前的事件监听 因为用不到了根本就
            clearAllListener()
        }
    }

    /**
     * 开始下载
     *
     * @param info 传入下载包信息
     */
    fun download(info: DownloadInfo) {
        checkInit()
        downloadInfo = info
        FileDownloader.setup(mContext)
        downloadUpdateApkFilePath = getAppLocalPath(
            mContext, info.prodVersionName
        )

        //检查下本地文件的大小 如果大小和信息中的文件大小一样的就可以直接安装 否则就删除掉
        val tempFile = File(downloadUpdateApkFilePath)
        if (tempFile.exists()) {
            //也可以根据版本号判断
            if (tempFile.length() != info.fileSize) {
                deleteFile(downloadUpdateApkFilePath)
                deleteFile(FileDownloadUtils.getTempPath(downloadUpdateApkFilePath))
            }
        }
        downloadTask = FileDownloader.getImpl().create(info.apkUrl).setPath(
            downloadUpdateApkFilePath
        )
        downloadTask.addHeader("Accept-Encoding", "identity").addHeader(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36"
        ).setListener(fileDownloadListener).setAutoRetryTimes(3).start()
    }

    /**
     * 结束任务
     */
    fun cancelTask() {
        isDownloading = false
        downloadTask.pause()
        cancelDownload(mContext)
        clearAllListener()
    }

    /**
     * 暂停任务
     */
    fun pauseTask() {
        isDownloading = false
        downloadTask.pause()
        cancelDownload(mContext)
    }

    private val fileDownloadListener: FileDownloadListener =
        object : FileDownloadLargeFileListener() {
            override fun pending(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                downloadStart()
                if (totalBytes < 0) {
                    downloadTask.pause()
                }
            }

            override fun progress(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                downloading(soFarBytes, totalBytes)
                if (totalBytes < 0) {
                    downloadTask.pause()
                }
            }

            override fun paused(task: BaseDownloadTask, soFarBytes: Long, totalBytes: Long) {
                for (appDownloadListener in allAppDownloadListener) {
                    appDownloadListener.pause()
                }
            }

            override fun completed(task: BaseDownloadTask) {
                downloadComplete(task.path)
            }

            override fun error(task: BaseDownloadTask, e: Throwable) {
                deleteFile(downloadUpdateApkFilePath)
                deleteFile(FileDownloadUtils.getTempPath(downloadUpdateApkFilePath))
                downloadError(e)
            }

            override fun warn(task: BaseDownloadTask) {}
        }


    /**
     * @param e
     */
    private fun downloadError(e: Throwable) {
        isDownloading = false
        deleteFile(downloadUpdateApkFilePath)
        send(mContext, -1)
        for (appDownloadListener in allAppDownloadListener) {
            appDownloadListener.downloadFail(e.message)
        }
        LogUtils.log("文件下载出错，异常信息为：" + e.message)
    }

    /**
     * 下载完成
     *
     * @param path
     */
    private fun downloadComplete(path: String) {
        isDownloading = false
        send(mContext, 100)
        for (appDownloadListener in allAppDownloadListener) {
            appDownloadListener.downloadComplete(path)
        }
        LogUtils.log("文件下载完成，准备安装，文件地址：$downloadUpdateApkFilePath")
        //校验MD5
        val newFile = File(path)
        if (newFile.exists()) {
            //如果需要进行MD5校验
            if (getUpdateConfig().isNeedFileMD5Check && !TextUtils.isEmpty(
                    downloadInfo.md5Check
                )
            ) {
                try {
                    val md5 = Md5Utils.getFileMD5(newFile)
                    if (!TextUtils.isEmpty(md5) && md5 == downloadInfo.md5Check) {
                        //校验成功
                        for (md5CheckListener in allMd5CheckListener) {
                            md5CheckListener.fileMd5CheckSuccess()
                        }
                        installApkFile(mContext, newFile)
                        LogUtils.log("文件MD5校验成功")
                    } else {
                        //校验失败
                        for (md5CheckListener in allMd5CheckListener) {
                            md5CheckListener.fileMd5CheckFail(downloadInfo.md5Check, md5)
                        }
                    }
                } catch (e: Exception) {
                    LogUtils.log("文件MD5解析失败，抛出异常：" + e.message)
                    //安装文件
                    installApkFile(mContext, newFile)
                }
            } else {
                //安装文件
                installApkFile(mContext, newFile)
            }
        }
    }

    /**
     * 正在下载
     *
     * @param soFarBytes
     * @param totalBytes
     */
    private fun downloading(soFarBytes: Long, totalBytes: Long) {
        isDownloading = true
        var progress = (soFarBytes * 100.0 / totalBytes).toInt()
        if (progress < 0) {
            progress = 0
        }
        send(mContext, progress)
        for (appDownloadListener in allAppDownloadListener) {
            appDownloadListener.downloading(progress)
        }
        LogUtils.log("文件正在下载中，进度为$progress%")
    }

    /**
     * 开始下载
     */
    private fun downloadStart() {
        LogUtils.log("文件开始下载")
        isDownloading = true
        send(mContext, 0)
        for (appDownloadListener in allAppDownloadListener) {
            appDownloadListener.downloadStart()
        }
    }

    fun getUpdateConfig(): UpdateConfig {
        return updateConfig
    }

    /**
     * 获取Context
     *
     * @return
     */
    val context: Context
        get() {
            checkInit()
            return mContext
        }

    /**
     * 重新下载
     */
    fun reDownload() {
        for (appDownloadListener in allAppDownloadListener) {
            appDownloadListener.reDownload()
        }
        download(downloadInfo)
    }

    /**
     * 清除所有缓存的数据
     */
    fun clearAllData() {
        //删除任务中的缓存文件
        FileDownloader.getImpl().clearAllTaskData()
        //删除已经下载好的文件
        delAllFile(File(getAppRootPath(mContext)))
    }//POST请求//GET请求

    /**
     * 获取数据
     */
    private fun getData() {
        val updateConfig = updateConfig
        val modelClass = updateConfig.modelClass
        if (modelClass is LibraryUpdateEntity) {
            if (updateConfig.methodType == TypeConfig.METHOD_GET) {
                //GET请求
                doGet(context,
                    updateConfig.baseUrl,
                    updateConfig.requestHeaders,
                    updateConfig.requestParams,
                    updateConfig.modelClass.javaClass,
                    object : HttpCallbackModelListener<Any> {
                        override fun onFinish(response: Any) {
                            requestSuccess(response)
                        }

                        override fun onError(e: Exception?) {
                            listenToUpdateInfo(true)
                        }
                    })
            } else {
                //POST请求
                doPost(context,
                    updateConfig.baseUrl,
                    updateConfig.requestHeaders,
                    updateConfig.requestParams,
                    updateConfig.modelClass.javaClass,
                    object : HttpCallbackModelListener<Any> {
                        override fun onFinish(response: Any) {
                            requestSuccess(response)
                        }

                        override fun onError(e: Exception?) {
                            listenToUpdateInfo(true)
                            LogUtils.log("POST请求抛出异常：" + e!!.message)
                        }
                    })
            }
        } else {
            listenToUpdateInfo(true)
            throw RuntimeException(modelClass.javaClass.simpleName + "：未实现LibraryUpdateEntity接口")
        }
    }

    private fun requestSuccess(response: Any) {
        val libraryUpdateEntity = response as LibraryUpdateEntity
        LogUtils.log("请求成功：${libraryUpdateEntity.appVersionCode}")
        //需要根据保本号判断是否有安装包
        if (libraryUpdateEntity.appVersionCode == 0) {
            listenToUpdateInfo(true)
            return
        }
        checkUpdate(
            DownloadInfo(
                forceUpdateFlag = libraryUpdateEntity.forceAppUpdateFlag(),
                prodVersionCode = libraryUpdateEntity.appVersionCode,
                prodVersionName = libraryUpdateEntity.appVersionName,
                fileSize = libraryUpdateEntity.appApkSize.toLong(),
                apkUrl = libraryUpdateEntity.appApkUrls,
                hasAffectCodes = libraryUpdateEntity.appHasAffectCodes,
                md5Check = libraryUpdateEntity.fileMd5Check,
                updateLog = libraryUpdateEntity.appUpdateLog,
                channel = libraryUpdateEntity.channel
            )
        )
    }

    fun addMd5CheckListener(md5CheckListener: MD5CheckListener?): AppUpdateUtils {
        if (md5CheckListener != null && !md5CheckListenerList.contains(md5CheckListener)) {
            md5CheckListenerList.add(md5CheckListener)
        }
        return this
    }

    fun addAppDownloadListener(appDownloadListener: AppDownloadListener?): AppUpdateUtils {
        if (appDownloadListener != null && !appDownloadListenerList.contains(appDownloadListener)) {
            appDownloadListenerList.add(appDownloadListener)
        }
        return this
    }

    fun addAppUpdateInfoListener(appUpdateInfoListener: AppUpdateInfoListener?): AppUpdateUtils {
        if (appUpdateInfoListener != null && !appUpdateInfoListenerList.contains(
                appUpdateInfoListener
            )
        ) {
            appUpdateInfoListenerList.add(appUpdateInfoListener)
        }
        return this
    }

    private val allAppUpdateInfoListener: List<AppUpdateInfoListener>
        get() = ArrayList(appUpdateInfoListenerList)
    private val allAppDownloadListener: List<AppDownloadListener>
        get() = ArrayList(appDownloadListenerList)
    private val allMd5CheckListener: List<MD5CheckListener>
        get() = ArrayList(md5CheckListenerList)

    /**
     * 是否有新版本更新
     *
     * @param isLatest 是否更新
     */
    private fun listenToUpdateInfo(isLatest: Boolean) {
        for (appUpdateInfoListener in allAppUpdateInfoListener) {
            appUpdateInfoListener.isLatestVersion(isLatest)
        }
    }
}