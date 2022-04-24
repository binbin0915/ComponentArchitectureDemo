package com.model.airpods.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid

private val AIR_PODS_UUID_ARRAY = arrayOf(
    ParcelUuid.fromString("74ec2172-0bad-4d01-8f77-997b2be0722a"),
    ParcelUuid.fromString("2a72e02b-7b99-778f-014d-ad0b7221ec74")
)
@SuppressLint("MissingPermission")
fun BluetoothDevice.checkUUID(): Boolean {
    val uuidArray = uuids ?: return false
    for (u in uuidArray) {
        if (AIR_PODS_UUID_ARRAY.contains(u)) return true
    }
    return false
}