package com.library.widget.status

/**
 * 作用描述：重试监听
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
fun interface OnRetryEventListener {
    fun onRetryEvent(multiStateContainer: MultiStateContainer)
}