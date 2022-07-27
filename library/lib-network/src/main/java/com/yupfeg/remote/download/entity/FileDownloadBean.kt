package com.yupfeg.remote.download.entity

/**
 * 创建bean用于下载文件
 */
data class FileDownloadBean(
    val item: Int,
    /**
     * 文件下载链接
     */
    var url: String,
    /**
     * 文件下载进度
     */
    var progress: Int = 0,
    /**
     * 文件下载当前状态
     *
     * -1待下载 0下载中 1暂停 2断点下载中 3下载完成
     * */
    var downloadState: Int = -1,
    /**
     * 是否显示没下载完的状态
     */
    var isResume: Boolean = false
)
