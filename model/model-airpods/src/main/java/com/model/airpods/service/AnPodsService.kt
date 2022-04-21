package com.model.airpods.service

import android.content.Intent
import android.os.IBinder
import android.util.Log
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
            val state = getConnected()
            Log.e("AAAAAAAAAAA","connection state = $state")
            if (state.isConnected) {
                airPodsConnectionState.value = state
            }
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("AAAAAAAAAAA", "onStartCommand: flags=$flags")
        if (intent?.action == "com.model.airpods.ACTION_POPUP") {
            checkConnection()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
