package com.library.base.viewmodel

import com.library.base.expand.ToastType
import com.library.widget.status.PageStatus

data class ViewModelEventData(
    val type: EventType = EventType.EVENT_NONE,
    val pageStatus: PageStatus = PageStatus.STATUS_SUCCEED,
    val title: String = "",
    val desc: String = "",
    val toastType: ToastType = ToastType.INFO
)

/**
 * 事件类型
 * 1.显示Toast
 * 2.显示弹窗
 * 3.显示加载弹窗
 * 4.关闭弹窗
 * 5.关闭当前页面
 * 6.改变页面状态
 * 7.默认状态，不做任何事
 */

enum class EventType {
    EVENT_TOAST,
    EVENT_DIALOG,
    EVENT_SHOW_LOADING_DIALOG,
    EVENT_DISMISS_LOADING_DIALOG,
    EVENT_FINISH_PAGE,
    EVENT_CHANGE_PAGE_STATUS,
    EVENT_NONE
}
