package com.youjingjiaoyu.upload.model

import com.liulishuo.filedownloader.util.FileDownloadHelper.ConnectionCreator

class UpdateConfig {
    /**
     * 设置使用sdk请求的时候的请求链接地址
     */
    lateinit var baseUrl: String

    /**
     * 是否是debug状态 打印log
     */
    var isDebug = true
        private set

    /**
     * 设置样式类型 默认是随意一个样式类型
     */
    var uiThemeType = TypeConfig.UI_THEME_AUTO
        private set

    /**
     * 请求方式 默认GET请求
     */
    var methodType = TypeConfig.METHOD_GET
        private set

    /**
     * 更新信息的数据来源方式 默认用户自己提供更新信息
     */
    var dataSourceType = TypeConfig.DATA_SOURCE_TYPE_MODEL

    /**
     * 是否在通知栏显示进度 默认显示 显示的好处在于 如果因为网络原因或者其他原因导致下载失败的时候，可以点击通知栏重新下载
     */
    var isShowNotification = true

    /**
     * 通知栏下载进度提醒的Icon图标 默认为0 就是app的logo
     */
    var notificationIconRes = 0

    //请求头信息
    lateinit var requestHeaders: Map<String, Any>
        private set

    //请求参数信息
    lateinit var requestParams: Map<String, Any>

    //自定义Bean类 此类必须实现LibraryUpdateEntity接口
    lateinit var modelClass: Any

    //是否需要进行文件的MD5校验
    var isNeedFileMD5Check = false

    //是否静默下载
    var isAutoDownloadBackground = false

    /**
     * 自定义的Activity类
     */
    interface CustomActivityClass {
        /**
         * 启动activity并获取下载信息
         * @param info 下载信息
         */
        fun startActivity(info: DownloadInfo?)
    }

    private var customActivityClass: CustomActivityClass? = null

    //自定义下载
    var customDownloadConnectionCreator: ConnectionCreator? = null
        private set

    fun setCustomDownloadConnectionCreator(customDownloadConnectionCreator: ConnectionCreator?): UpdateConfig {
        this.customDownloadConnectionCreator = customDownloadConnectionCreator
        return this
    }

    fun setAutoDownloadBackground(autoDownloadBackground: Boolean): UpdateConfig {
        isAutoDownloadBackground = autoDownloadBackground
        return this
    }

    fun getCustomActivityClass(info: DownloadInfo?) {
        customActivityClass!!.startActivity(info)
    }

    companion object {
        //在通知栏显示进度
        const val TYPE_NITIFICATION = 1

        //对话框显示进度
        const val TYPE_DIALOG = 2

        //对话框展示提示和下载进度
        const val TYPE_DIALOG_WITH_PROGRESS = 3

        //对话框展示提示后台下载
        const val TYPE_DIALOG_WITH_BACK_DOWN = 4

        //POST方法
        const val METHOD_POST = 3

        //GET方法
        const val METHOD_GET = 4
    }
}