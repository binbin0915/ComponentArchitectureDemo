package com.youjingjiaoyu.upload.model

import com.liulishuo.filedownloader.util.FileDownloadHelper.ConnectionCreator

data class UpdateConfig(
    //设置使用sdk请求的时候的请求链接地址
    var baseUrl: String,
    //是否是debug状态 打印log
    var isDebug: Boolean = true,
    //设置样式类型 默认是随意一个样式类型
    var uiThemeType: Int = TypeConfig.UI_THEME_AUTO,
    //请求方式 默认GET请求
    var methodType: Int = TypeConfig.METHOD_GET,
    //更新信息的数据来源方式 默认用户自己提供更新信息
    var dataSourceType: Int = TypeConfig.DATA_SOURCE_TYPE_MODEL,
    //是否在通知栏显示进度 默认显示 显示的好处在于 如果因为网络原因或者其他原因导致下载失败的时候，可以点击通知栏重新下载
    var isShowNotification: Boolean = true,
    //通知栏下载进度提醒的Icon图标 默认为0 就是app的logo
    var notificationIconRes: Int = 0,
    //请求头信息
    var requestHeaders: Map<String, Any>,
    //请求参数信息
    var requestParams: Map<String, Any>,
    //自定义Bean类 此类必须实现LibraryUpdateEntity接口
    var modelClass: Any,
    //是否需要进行文件的MD5校验
    var isNeedFileMD5Check: Boolean = false,
    //是否静默下载
    var isAutoDownloadBackground: Boolean = false,
    //展示的自定义activity
    var customActivityClass: CustomActivityClass? = null,
    //自定义下载 -- 使用okhttp
    var customDownloadConnectionCreator: ConnectionCreator? = null,
) {

    /**
     * 自定义的Activity类
     */
    interface CustomActivityClass {
        /**
         * 启动activity并获取下载信息
         * @param info 下载信息
         */
        fun startActivity(info: DownloadInfo)
    }
}