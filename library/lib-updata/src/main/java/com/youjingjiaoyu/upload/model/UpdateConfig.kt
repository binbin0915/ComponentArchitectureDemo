package com.youjingjiaoyu.upload.model

import com.liulishuo.filedownloader.util.FileDownloadHelper.ConnectionCreator

class UpdateConfig {
    /**
     * 设置使用sdk请求的时候的请求链接地址
     */
    var baseUrl: String? = null
        private set

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
        private set

    /**
     * 是否在通知栏显示进度 默认显示 显示的好处在于 如果因为网络原因或者其他原因导致下载失败的时候，可以点击通知栏重新下载
     */
    var isShowNotification = true
        private set

    /**
     * 通知栏下载进度提醒的Icon图标 默认为0 就是app的logo
     */
    var notificationIconRes = 0
        private set

    //请求头信息
    var requestHeaders: Map<String, Any>
        private set

    //请求参数信息
    var requestParams: Map<String, Any>
        private set

    //自定义Bean类 此类必须实现LibraryUpdateEntity接口
    var modelClass: Any? = null
        private set

    //是否需要进行文件的MD5校验
    var isNeedFileMD5Check = false
        private set

    //是否静默下载
    var isAutoDownloadBackground = false
        private set

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

    fun setShowNotification(showNotification: Boolean): UpdateConfig {
        isShowNotification = showNotification
        return this
    }

    fun getCustomActivityClass(info: DownloadInfo?) {
        customActivityClass!!.startActivity(info)
    }

    fun setCustomActivityClass(customActivityClass: CustomActivityClass?): UpdateConfig {
        this.customActivityClass = customActivityClass
        return this
    }

    fun setNeedFileMD5Check(needFileMD5Check: Boolean): UpdateConfig {
        isNeedFileMD5Check = needFileMD5Check
        return this
    }

    fun setNotificationIconRes(notificationIconRes: Int): UpdateConfig {
        this.notificationIconRes = notificationIconRes
        return this
    }

    fun setDataSourceType(dataSourceType: Int): UpdateConfig {
        this.dataSourceType = dataSourceType
        return this
    }

    fun setRequestHeaders(requestHeaders: Map<String, Any>?): UpdateConfig {
        this.requestHeaders = requestHeaders
        return this
    }

    fun setRequestParams(requestParams: Map<String, Any>?): UpdateConfig {
        this.requestParams = requestParams
        return this
    }

    fun setModelClass(modelClass: Any?): UpdateConfig {
        this.modelClass = modelClass
        return this
    }

    fun setDebug(debug: Boolean): UpdateConfig {
        isDebug = debug
        return this
    }

    fun setBaseUrl(baseUrl: String?): UpdateConfig {
        this.baseUrl = baseUrl
        return this
    }

    fun setMethodType(methodType: Int): UpdateConfig {
        this.methodType = methodType
        return this
    }

    fun setUiThemeType(uiThemeType: Int): UpdateConfig {
        this.uiThemeType = uiThemeType
        return this
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