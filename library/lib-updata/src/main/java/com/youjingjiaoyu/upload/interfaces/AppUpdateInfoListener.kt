package com.youjingjiaoyu.upload.interfaces

/**
 * 检查版本更新的监听
 *
 * @author wangk
 */
interface AppUpdateInfoListener {
    /**
     * 是否有版本更新的回调
     *
     * @param isLatest 是否有版本更新
     */
    fun isLatestVersion(isLatest: Boolean)
}