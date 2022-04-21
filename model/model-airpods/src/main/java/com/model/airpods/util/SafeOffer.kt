package com.model.airpods.util

import androidx.annotation.RestrictTo
import kotlinx.coroutines.channels.SendChannel

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun <E> SendChannel<E>.safeOffer(value: E): Boolean {
    return runCatching { trySend(value).isSuccess }.getOrDefault(false)
}