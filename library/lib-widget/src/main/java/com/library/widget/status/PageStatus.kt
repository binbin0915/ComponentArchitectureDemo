package com.library.widget.status

/**
 * 作用描述：页面状态
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
enum class PageStatus {
    /**
     * 页面空状态
     */
    STATUS_EMPTY,

    /**
     * 页面空状态-带重试按钮
     */
    STATUS_EMPTY_RETRY,

    /**
     * 页面加载中状态
     */
    STATUS_LOADING,

    /**
     * 页面成功状态
     */
    STATUS_SUCCEED,

    /**
     * 页面未知错误状态
     */
    STATUS_ERROR,

    /**
     * 页面未知状态带重试按钮
     */
    STATUS_ERROR_RETRY,

    /**
     * 网络错误状态
     */
    STATUS_NET_ERROR,

    /**
     * 网络错误状态-带重试按钮
     */
    STATUS_NET_ERROR_RETRY,
}