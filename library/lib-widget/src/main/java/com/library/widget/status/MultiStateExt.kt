package com.library.widget.status

import android.app.Activity
import android.view.View

/**
 * 作用描述：多状态页的拓展方法
 *
 * 1. view绑定、fragment绑定（局部状态）
 * 2. activity绑定（页面状态）
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
fun View.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener { }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)

fun Activity.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener { }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)