package com.library.base.expand

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * 利用 CoroutineScope 防抖
 *
 * 若block中是 CoroutineScope.{} 这样的，在每次调用 CoroutineScope 实例对象时就会触发。
 * 会在最后一次松开超时后，执行end
 *
 * @author wangkai
 */


private val jobUuid = UUID.randomUUID().hashCode()

private var <T : View> T.mDebounceSuspendJob: Job?
    get() = if (getTag(jobUuid) != null) getTag(jobUuid) as Job? else null
    set(value) {
        setTag(jobUuid, value)
    }

fun <T : View> T.debounceClick(
    coroutineScope: CoroutineScope, delayMs: Long = 600L, block: suspend (T) -> Unit
) {
    setOnClickListener {
        mDebounceSuspendJob?.cancel()
        mDebounceSuspendJob = coroutineScope.launch {
            delay(delayMs)
            block(this@debounceClick)
            mDebounceSuspendJob = null
        }

    }
}

fun <T : View> T.debounceClick(
    owner: LifecycleOwner, delayMs: Long = 600L, block: suspend (T) -> Unit
) {
    this.debounceClick(owner.lifecycle.coroutineScope, delayMs, block)
}

fun <T : View> T.debounceClick(
    coroutineScope: CoroutineScope, delayMs: Long = 600L, originBlock: View.OnClickListener?
) {
    originBlock ?: return
    debounceClick(coroutineScope, delayMs) {
        originBlock.onClick(this)
    }
}


fun <T : View> T.debounceClick(
    owner: LifecycleOwner, delayMs: Long = 600L, originBlock: View.OnClickListener?
) {
    originBlock ?: return
    debounceClick(owner.lifecycle.coroutineScope, delayMs) {
        originBlock.onClick(this)
    }
}