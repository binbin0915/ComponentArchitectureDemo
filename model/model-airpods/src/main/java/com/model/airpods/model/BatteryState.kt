package com.model.airpods.model

import androidx.annotation.Keep

/**
 * airpods电量信息
 *
 * 使用@Keep注解保证不被混淆
 */
@Keep
data class BatteryState(
    /**
     * 当前时间戳
     */
    val timestamp: Long,
    /**
     * 当前电量
     */
    val leftBattery: Int,
    val rightBattery: Int,
    val caseBattery: Int,
    /**
     * 是否在充电
     */
    val isLeftCharge: Boolean,
    val isRightCharge: Boolean,
    val isCaseCharge: Boolean,
    /**
     * 模式
     */
    val model: String
)

/**
 * airpods连接状态
 */
@Keep
data class ConnectionState(val isConnected: Boolean = true, val deviceName: String = "")
