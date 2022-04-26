package com.model.airpods.model

import androidx.annotation.Keep

/**
 * airpods信息
 */
@Keep
data class BatteryState(
    val timestamp: Long,
    val leftBattery: Int,
    val rightBattery: Int,
    val caseBattery: Int,
    val isLeftCharge: Boolean,
    val isRightCharge: Boolean,
    val isCaseCharge: Boolean,
    val model: String
)

/**
 * airpods连接状态
 */
@Keep
data class ConnectionState(val isConnected: Boolean = true, val deviceName: String = "")