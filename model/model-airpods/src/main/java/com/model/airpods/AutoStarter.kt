package com.model.airpods

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.model.airpods.service.AnPodsService

/**
 * 自启动广播
 */
class AutoStarter : BroadcastReceiver() {
    private val TAG = "AutoStarterTAG"
    override fun onReceive(context: Context, intent: Intent) {
        Log.e(TAG, "onReceive:${intent.action}")
        //启动service
        context.startService(intent.setClass(context, AnPodsService::class.java))
    }
}