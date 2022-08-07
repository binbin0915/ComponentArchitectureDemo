package com.model.airpods.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.res.Resources
import android.hardware.display.DisplayManager
import android.os.Looper
import android.os.ParcelUuid
import androidx.annotation.CheckResult
import androidx.annotation.RestrictTo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

const val TAG = "AIRPODS_TAG"


const val NOTIFICATION_ID = 9083150

//渠道id
const val CHANNEL_ID = "airpods"
const val ACTION_POPUP = "com.model.airpods.ACTION_POPUP"

private val AIR_PODS_UUID_ARRAY = arrayOf(
    ParcelUuid.fromString("74ec2172-0bad-4d01-8f77-997b2be0722a"),
    ParcelUuid.fromString("2a72e02b-7b99-778f-014d-ad0b7221ec74")
)

/**
 * 检查主线程
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun checkMainThread() = check(Looper.myLooper() == Looper.getMainLooper()) {
    "Expected to be called on the main thread but was " + Thread.currentThread().name
}

@SuppressLint("MissingPermission")
fun BluetoothDevice.checkUUID(): Boolean {
    val uuidArray = uuids ?: return false
    for (u in uuidArray) {
        if (AIR_PODS_UUID_ARRAY.contains(u)) return true
    }
    return false
}


@CheckResult
@ExperimentalCoroutinesApi
fun Context.displayChange(): Flow<Int> = callbackFlow {
    val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }
    val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) {
            safeOffer(displayId)
        }
    }
    displayManager.registerDisplayListener(displayListener, null)
    awaitClose { displayManager.unregisterDisplayListener(displayListener) }
}.conflate()

/**
 * 正常编码中一般只会用到 [dp]/[sp] ;
 * 其中[dp]/[sp] 会根据系统分辨率将输入的dp/sp值转换为对应的px
 */
val Float.dp: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()


val Float.sp: Float                 // [xxhdpi](360 -> 1080)
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )


val Int.sp: Int
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()