package com.model.airpods.util

import androidx.annotation.RestrictTo
import kotlinx.coroutines.channels.SendChannel

/**
 * 限制api在其他模块使用
 * * @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    return runCatching { trySend(value).isSuccess }.getOrDefault(false)
}