package com.wangkai.myapplication

import android.content.Context
import android.util.Log
import org.acra.data.CrashReportData
import org.acra.sender.ReportSender


/**
 * 您可以实现自己的ReportSender并配置 ACRA 以使用它来代替其他发件人或与其他发件人一起使用。
 */
class OwnIntentSender : ReportSender {

    override fun send(context: Context, errorContent: CrashReportData) {
        //遍历CrashReportData实例并执行任何操作
        //每对ReportField键/String值都需要
        val map = errorContent.toMap()
        map.forEach {
            Log.d("AAAAAXXXX", "捕获到错误:${it.value}")
        }
    }
}