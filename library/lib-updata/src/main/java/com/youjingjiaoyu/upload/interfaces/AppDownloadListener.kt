package com.youjingjiaoyu.upload.interfaces

/**
 * @author wangk
 */
interface AppDownloadListener {
    /**
     * 下载中
     *
     * @param progress 下载进度
     */
    fun downloading(progress: Int)

    /**
     * 下载失败
     *
     * @param msg 下载失败
     */
    fun downloadFail(msg: String?)

    /**
     * 下载完成
     *
     * @param path 存储路径
     */
    fun downloadComplete(path: String?)

    /**
     * 开始下载
     */
    fun downloadStart()

    /**
     * 重新下载
     */
    fun reDownload()

    /**
     * 暂停下载
     */
    fun pause()
}