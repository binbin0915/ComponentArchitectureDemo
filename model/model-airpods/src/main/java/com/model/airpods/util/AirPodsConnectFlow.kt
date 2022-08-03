package com.model.airpods.util

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.CheckResult
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.lifecycle.MutableLiveData
import com.model.airpods.model.ConnectionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

val airPodsConnectionState = MutableLiveData<ConnectionState>()

/**
 * 注册蓝牙监听
 */
@CheckResult
@ExperimentalCoroutinesApi
fun Context.fromBroadCast(): Flow<ConnectionState> = callbackFlow {
    checkMainThread()
    val filter: IntentFilter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        //addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)//和ACL状态重复,接受消息:连接时更慢一点,断开时快一点
    }
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent ?: return
            val event = intent.parseIntent()
            safeOffer(event)
        }
    }
    registerReceiver(receiver, filter)
    awaitClose { unregisterReceiver(receiver) }
}.conflate()

@RequiresApi(Build.VERSION_CODES.S)
@RequiresPermission(value = Manifest.permission.BLUETOOTH_CONNECT)
suspend fun Context.getConnected() = suspendCancellableCoroutine { continuation ->
    checkMainThread()
    val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val headset: Int = manager.adapter.getProfileConnectionState(BluetoothProfile.HEADSET)
    if (headset != BluetoothProfile.STATE_CONNECTED) {
        continuation.resume(ConnectionState(isConnected = false))
        return@suspendCancellableCoroutine
    }
    val listener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            if (profile != BluetoothProfile.HEADSET) return
            proxy?.connectedDevices ?: return
            for (device in proxy.connectedDevices) {
                if (device.checkUUID()) {
                    try {
                        continuation.resume(ConnectionState(deviceName = device.name))
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }

        override fun onServiceDisconnected(profile: Int) {
        }
    }
    manager.adapter.getProfileProxy(this, listener, BluetoothProfile.HEADSET)
    continuation.invokeOnCancellation {
        manager.adapter.getProfileProxy(
            this, null, BluetoothProfile.HEADSET
        )
    }
}

fun Intent.parseIntent(): ConnectionState {
    when (action) {
        BluetoothAdapter.ACTION_STATE_CHANGED -> {
            val state = getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            if (state == BluetoothAdapter.STATE_OFF || state == BluetoothAdapter.STATE_TURNING_OFF) { //bluetooth turned off, stop scanner and remove notification
                return ConnectionState(isConnected = false)
            }
        }
        BluetoothDevice.ACTION_ACL_CONNECTED, BluetoothDevice.ACTION_ACL_DISCONNECTED, BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED -> {
            val device = getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (device != null && device.checkUUID()) {
                if (action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                    try {
                        return ConnectionState(deviceName = device.name)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                } else if (action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                    return ConnectionState(isConnected = false)
                }
            }
        }
    }
    return ConnectionState(isConnected = false)
}