package com.wangkai.upload.net

/**
 * HttpURLConnection网络请求返回监听器
 * @author wangkai
 */
interface HttpCallbackModelListener<T> {
    /**
     * 网络请求成功
     * @param response 返回响应体
     */
    fun onFinish(response: T)

    /**
     * 网络请求失败
     * @param e 异常信息
     */
    fun onError(e: Exception?)
}