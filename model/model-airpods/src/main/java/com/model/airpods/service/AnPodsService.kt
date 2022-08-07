package com.model.airpods.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.model.airpods.R
import com.model.airpods.model.BatteryState
import com.model.airpods.model.ConnectionState
import com.model.airpods.ui.widget.AnPodsDialog
import com.model.airpods.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * 关于flow流的启动主要有两种
 * 1. 一种是作用域.launch启动一个流，用collect操作符接收数据；
 * 2. 一种是launchIn操作符启动流，官方不建议用这种，可能出于数据安全考虑，一般建议在onResume方法调用后启动协程，因为怕数据接收了，但View还没创建出来。
 *
 * 但链式调用真的很好用!!用onEach操作符接收数据:
 * ```kotlin
 * flow.onEach {}.launchIn(lifecycleScope)
 * ```
 *
 * 步骤：
 * 1. 创建通知渠道
 * 2. 检查连接状态并更新
 * 3. 创建蓝牙连接状态变化的广播
 * 4. 监听livedata变化
 * 5.
 */
class AnPodsService : LifecycleService(), CoroutineScope by MainScope() {
    private val simpleDateFormat by lazy { SimpleDateFormat("HH:mm:ss", Locale.CANADA) }

    /**
     * 耳机弹窗
     */
    private val anPodsDialog by lazy { AnPodsDialog(this) }

    /**
     * 通知管理
     */
    private val notifyManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * 创建通知
     */
    private val notification: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setOngoing(true)
            priority = NotificationCompat.PRIORITY_MAX
            setSmallIcon(R.drawable.airpods_ic_airpods)
        }
    }

    override fun onCreate() {
        super.onCreate()
        //创建通知渠道
        createNotification()
        //检查连接状态
        checkConnection()
        //蓝牙连接广播
        blueToothBroadcastFlow()
        //livedata状态变化
        livedataObserve()
    }

    /**
     * 创建通知
     */
    private fun createNotification() {
        //设置渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    enableVibration(false)
                    enableLights(false)
                    setShowBadge(true)
                    lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                }
            notifyManager.createNotificationChannel(channel)
        }
    }

    /**
     * 检查蓝牙连接状态获取设备名称
     */
    private lateinit var connectionJob: Job
    private fun checkConnection() {
        if (::connectionJob.isInitialized) {
            if (connectionJob.isActive) {
                return
            }
            connectionJob.cancel()
        }
        connectionJob = lifecycleScope.launch {
            getConnected().also {
                if (it.isConnected) {
                    airPodsConnectionState.value = it
                }
            }
        }
    }

    /**
     * 获取设备电量
     */
    private lateinit var detectJob: Job
    private fun detectBattery() {
        if (::detectJob.isInitialized) {
            if (detectJob.isActive) {
                return
            }
            detectJob.cancel()
        }
        @OptIn(ExperimentalCoroutinesApi::class)
        detectJob = batteryState().map {
            it.parse("auto")
        }.catch {
//            warn { "detectBattery: onError=${it.message}" }
        }.onEach {
//            info { "detectBattery: result=$it" }
            airPodsBatteryState.value = it
        }.onStart {
//            info { "detectBattery: onStart..." }
        }.onCompletion {
//            info { "detectBattery: onCompletion..." }
        }.launchIn(lifecycleScope)
    }

    /**
     * 蓝牙连接状态变化广播
     */
    private fun blueToothBroadcastFlow() {
        @OptIn(ExperimentalCoroutinesApi::class)
        val flow = fromBroadCast()
        lifecycleScope.launch(Dispatchers.Main) {
            flow.collect {
                airPodsConnectionState.value = it
                //蓝牙连接时弹窗，断开时取消弹窗
                if (it.isConnected) {
                    anPodsDialog.show()
                } else {
                    anPodsDialog.onBackPressed()
                }
            }
        }
    }

    private fun livedataObserve() {
        //连接状态变化的livedata
        airPodsConnectionState.observe({ lifecycle }) {
            connectionJob.cancel()
            anPodsDialog.updateConnectedDevice(it)
            Log.d("AAAAAAAAAAAAAAAAAAAAAAA", "连接状态变化：" + it.isConnected)
            updateWidgetUI(notification, it)
            //已连接--获取设备电量信息
            if (it.isConnected) {
                detectBattery()
            }
        }

        //电量状态变化的livedata
        airPodsBatteryState.observe({ lifecycle }) {
            val state = airPodsConnectionState.value
            if (state == null || !state.isConnected) return@observe
            updateWidgetUI(notification, state)
            anPodsDialog.updateUI(it)
        }
    }


    /**
     * 更新小部件的UI
     */
    private fun updateWidgetUI(
        notification: NotificationCompat.Builder,
        connectionState: ConnectionState,
        batteryState: BatteryState? = airPodsBatteryState.value
    ) {
        if (!connectionState.isConnected) {
            notification.setContentTitle("未连接")
            notification.setContentText("L: -  R: -  Case: -")
            notifyManager.notify(NOTIFICATION_ID, notification.build().apply {
                `when` = System.currentTimeMillis()
            })
            notifyManager.cancelAll()
            return
        }
        batteryState ?: return
        notification.setContentTitle(connectionState.deviceName)
        val content =
            if (batteryState.caseBattery <= 10) "L:${
                getTitle(
                    batteryState.leftBattery,
                    batteryState.isLeftCharge
                )
            }  R:${
                getTitle(
                    batteryState.rightBattery, batteryState.isLeftCharge
                )
            }  Case:${getTitle(batteryState.caseBattery, batteryState.isLeftCharge)}"
            else "L:${getTitle(batteryState.leftBattery, batteryState.isLeftCharge)}  R:${
                getTitle(
                    batteryState.rightBattery, batteryState.isLeftCharge
                )
            }"
        notification.setContentText(content)
        notifyManager.notify(NOTIFICATION_ID, notification.build().apply {
            `when` = System.currentTimeMillis()
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_POPUP) {
            anPodsDialog.show()
            checkConnection()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        throw UnsupportedOperationException("AnPodsService do not support bind!")
    }

    override fun onDestroy() {
        super.onDestroy()
        connectionJob.cancel()
    }
}
