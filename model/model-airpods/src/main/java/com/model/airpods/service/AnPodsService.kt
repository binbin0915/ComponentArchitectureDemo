package com.model.airpods.service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.model.airpods.util.airPodsConnectionState
import com.model.airpods.util.getConnected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AnPodsService : LifecycleService(), CoroutineScope by MainScope() {

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        throw UnsupportedOperationException("AnPodsService do not support bind!")
    }

    override fun onCreate() {
        super.onCreate()
        //1. 创建通知栏


        //2. 检查连接状态
        checkConnection()
    }

    private var connectionJob: Job? = null
    private fun checkConnection() {
        connectionJob = lifecycleScope.launch {
            //检查权限
            if (ActivityCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.BLUETOOTH_CONNECT
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val state = getConnected()
                if (state.isConnected) {
                    airPodsConnectionState.value = state
                }
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "com.model.airpods.ACTION_POPUP") {
            checkConnection()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
