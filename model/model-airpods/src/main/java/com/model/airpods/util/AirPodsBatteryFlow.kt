package com.model.airpods.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.annotation.CheckResult
import androidx.lifecycle.MutableLiveData
import com.library.logcat.AppLog
import com.model.airpods.model.BatteryState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter

val airPodsBatteryState = MutableLiveData<BatteryState>()

/**
 *
 * 蓝牙扫描
 *
 * 要查找 BLE 设备，请使用 startScan（） 方法。此方法将 ScanCallback 作为参数。您必须实现此回调，因为这就是返回扫描结果的方式。由于扫描需要大量使用电池，因此应遵守以下准则：
 * * 找到所需设备后，立即停止扫描。
 * * 切勿循环扫描，并始终为扫描设置时间限制。以前可用的设备可能已移出范围，继续扫描会耗尽电池电量。
 *
 * 若要仅扫描特定类型的外围设备，可以改为调用 startScan（List<ScanFilter>、ScanSettings、ScanCallback），提供限制扫描查找的设备的 ScanFilter 对象列表和指定扫描参数的 ScanSettings 对象。

 * Ble发现设备api：(扫描会在屏幕关闭时停止以节省电量。 再次打开屏幕时，将恢复扫描。 为避免这种情况，请使用下面两个)
 * * [BluetoothAdapter.getBluetoothLeScanner()]
 * * BluetoothLeScanner.startScan(ScanCallback callback)
 * * BluetoothLeScanner.startScan(List<ScanFilter> filters, ScanSettings settings, ScanCallback callback)
 * * BluetoothLeScanner.startScan(List<ScanFilter> filters, ScanSettings settings, PendingIntent callbackIntent)
 */
@SuppressLint("MissingPermission")
@CheckResult
@ExperimentalCoroutinesApi
fun Context.batteryState(): Flow<ScanResult> = callbackFlow {
    AppLog.log(TAG, "获取设备电量信息333333.....")
    checkMainThread()
    AppLog.log(TAG, "获取设备电量信息444444.....")
    val manager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val scanCallback = object : ScanCallback() {
        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            super.onBatchScanResults(results)
            results.forEach {
                onScanResult(-1, it)
            }
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            safeOffer(result)
        }

        override fun onScanFailed(errorCode: Int) {
            //扫描失败
            AppLog.log(TAG, "扫描失败.....errorCode:$errorCode")
            super.onScanFailed(errorCode)
        }
    }
    val manufacturerData = ByteArray(27).apply {
        this[0] = 7
        this[1] = 25
    }
    val manufacturerDataMask = ByteArray(27).apply {
        this[0] = -1
        this[1] = -1
    }
    val scanFilter = ScanFilter.Builder()
        .setManufacturerData(76, manufacturerData, manufacturerDataMask)
        .build()
    val filters: List<ScanFilter> = listOf(scanFilter)
    val settings = ScanSettings.Builder()
        //设置蓝牙LE扫描的扫描模式。
        //使用最高占空比进行扫描。建议只在应用程序处于此模式时使用此模式在前台运行
        .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
        //设置蓝牙LE扫描滤波器硬件匹配的匹配模式
        //在主动模式下，即使信号强度较弱，hw也会更快地确定匹配.在一段时间内很少有目击/匹配。
//        .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
//        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
        .setReportDelay(2)
        .build()
//    val flushJob = launch {
//        while (isActive) {
//            // Can undercut the minimum setReportDelay(), e.g. 5000ms on a Pixel5@12
//            manager.adapter.bluetoothLeScanner.flushPendingScanResults(scanCallback)
//            break
//        }
//    }
    manager.adapter.bluetoothLeScanner.startScan(filters, settings, scanCallback)
//    manager.adapter.bluetoothLeScanner.startScan(scanCallback)
    //等待关闭
    awaitClose {
        manager.adapter.bluetoothLeScanner.stopScan(scanCallback)
    }
}.conflate()
    .filter {
        it.rssi > -60
    }
    .filter {
        val data = it.scanRecord?.getManufacturerSpecificData(76)
        data != null && data.size == 27 && data.decodeHex().isNotEmpty()
    }


fun ScanResult.parse(overrideModel: String): BatteryState {
    val signal = scanRecord!!.getManufacturerSpecificData(76)!!.decodeHex()
    //left and right airpod (0-10 batt; 15=disconnected)
    val leftBattery =
        if (signal.isFlipped()) signal[12].toString().toInt(16) else signal[13].toString().toInt(
            16
        )

    val rightBattery =
        if (signal.isFlipped()) signal[13].toString().toInt(16) else signal[12].toString().toInt(
            16
        )
    //case (0-10 batt; 15=disconnected)
    val caseBattery = signal[15].toString().toInt(16)

    //charge status (bit 0=left; bit 1=right; bit 2=case)
    val chargeStatus = signal[14].toString().toInt(16)

    val isLeftCharge = chargeStatus and 1 != 0
    val isRightCharge = chargeStatus and 2 != 0
    val isCaseCharge = chargeStatus and 4 != 0

    val model = if (overrideModel == "auto") {
        //detect if these are AirPods pro or regular ones
        if (signal[7] == 'E') MODEL_AIR_PODS_PRO else MODEL_AIR_PODS_NORMAL
    } else {
        overrideModel
    }
    return BatteryState(
        System.currentTimeMillis(),
        leftBattery,
        rightBattery,
        caseBattery,
        isLeftCharge,
        isRightCharge,
        isCaseCharge,
        model
    )
}

const val MODEL_AIR_PODS_NORMAL = "airpods12"
const val MODEL_AIR_PODS_1 = "airpods1"
const val MODEL_AIR_PODS_2 = "airpods2"
const val MODEL_AIR_PODS_PRO = "airpodspro"


private val hexCharset =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

fun ByteArray.decodeHex(): String {
    val ret = CharArray(size * 2)
    for (i in indices) {
        val b: Int = this[i].toInt() and 0xFF
        ret[i * 2] = hexCharset[b ushr 4]
        ret[i * 2 + 1] = hexCharset[b and 0x0F]
    }
    return String(ret)
}

fun String.isFlipped(): Boolean {
    return (this[10].toString().toInt(16) + 0x10).toString(2)[3] == '0'
}