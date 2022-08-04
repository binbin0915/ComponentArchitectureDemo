package com.model.airpods

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.model.airpods.service.AnPodsService

/**
 * 自启动广播，启动后打开蓝牙service
 */
class AutoStarter : BroadcastReceiver() {
    private val TAG = "AutoStarterTAG"
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "onReceive:${intent.action}")
        //启动service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(intent.setClass(context, AnPodsService::class.java))
            context.startService(intent.setClass(context, AnPodsService::class.java))
        } else {
            context.startService(intent.setClass(context, AnPodsService::class.java))
        }
    }
}