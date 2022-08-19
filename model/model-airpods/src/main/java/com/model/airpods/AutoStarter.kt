package com.model.airpods

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.model.airpods.service.AnPodsService
import com.model.airpods.util.ACTION_POPUP


/**
 * 自启动广播，启动后打开蓝牙service
 */
class AutoStarter : BroadcastReceiver() {
    private val TAG = "AutoStarterTAG"
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "onReceive:${intent.action}")
        //启动service
        intent.setClass(context, AnPodsService::class.java).apply { action = ACTION_POPUP }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}