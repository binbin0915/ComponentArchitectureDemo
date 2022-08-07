package com.model.airpods

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent


/**
 * AppWidgetProvider是Android中提供的用于实现桌面小工具的类，其本质是一个广播，即BroadcastReceiver，在实际的使用中，把AppWidgetProvider当成一个BroadcastReceiver即可
 */
class AnPodsWidget : AppWidgetProvider() {

    /**
     * 每接收一次广播消息就调用一次，使用频繁
     */
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

    /**
     * 每次更新都调用一次该方法，使用频繁
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    /**
     * 每删除一个就调用一次
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
    }


    /**
     * 当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用
     */
    override fun onEnabled(context: Context) {

    }

    /**
     * 当最后一个该Widget删除是调用该方法，注意是最后一个
     */
    override fun onDisabled(context: Context) {

    }

}
