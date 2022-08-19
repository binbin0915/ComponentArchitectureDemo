package com.library.common.net

import com.library.logcat.AppLog
import com.library.logcat.LogcatLevel
import com.wangkai.remote.log.HttpLogPrinter

/**
 * 日志打印的实现类
 */
class LoggerHttpLogPrinterImpl : HttpLogPrinter {
    override val isPrintLog: Boolean
        get() = true

    override fun printDebugLog(tag: String, content: Any) {
        AppLog.log(LogcatLevel.DEBUG, tag, content.toString())
    }

    override fun printInfoLog(tag: String, content: Any) {
        AppLog.log(LogcatLevel.INFO, tag, content.toString())
    }

    override fun printWarningLog(tag: String, content: Any) {
        AppLog.log(LogcatLevel.WARN, tag, content.toString())
    }

    override fun printErrorLog(tag: String, content: Any) {
        AppLog.log(LogcatLevel.ERROR, tag, content.toString())
    }
}