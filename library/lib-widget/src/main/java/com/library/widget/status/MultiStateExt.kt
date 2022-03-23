package com.library.widget.status

import android.app.Activity
import android.view.View

/**
 * 作用描述：多状态页
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
fun View.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener {  }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)

fun Activity.bindMultiState(onRetryEventListener: OnRetryEventListener = OnRetryEventListener {  }) =
    MultiStatePage.bindMultiState(this, onRetryEventListener)