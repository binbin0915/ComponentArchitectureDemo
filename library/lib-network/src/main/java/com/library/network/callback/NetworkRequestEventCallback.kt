package com.library.network.callback

/**
 * 作用描述：网络请求过程中 事件状态回调
 *
 * 创建时间：2022/703/18
 * @author：WangKai
 */
interface NetworkRequestEventCallback {
    /**
     * 隐藏Loading框
     */
    fun onDismissLoading()

    /**
     * 服务器返回的code
     * 自行处理相关操作
     */
    fun onResponseCode(code: Int)

}