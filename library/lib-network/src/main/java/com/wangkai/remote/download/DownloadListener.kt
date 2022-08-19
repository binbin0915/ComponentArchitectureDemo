package com.wangkai.remote.download

import com.wangkai.remote.download.entity.FileDownloadBean

/**
 * 下载监听
 * @author 王凯
 * @date 2020/07/19
 */
interface DownloadListener {
    var fileDownloadBean: FileDownloadBean

    /**
     * 开始下载
     */
    fun onStartDownload()

    /**
     * 下载进度回调
     */
    fun onProgress(progress: Int)

    /**
     * 完成下载的回调
     */
    fun onFinishDownload()

    /**
     * 下载失败的回调
     */
    fun onFail(errorInfo: String?)

    /**
     * 继续下载的回调
     */
    fun onContinue()

    /**
     * 暂停下载的回调
     */
    fun onPause(path: String)

    /**
     * 取消下载的回调
     */
    fun onCancel(path: String)
}