package com.library.common

import com.yupfeg.remote.log.HttpLogPrinter

class LoggerHttpLogPrinterImpl : HttpLogPrinter{
    override val isPrintLog: Boolean
        get() = true

    override fun printDebugLog(tag: String, content: Any) {
//        logd(tag,content)
    }

    override fun printInfoLog(tag: String, content: Any) {
//        logi(tag,content)
    }

    override fun printWarningLog(tag: String, content: Any) {
//        logw(tag,content)
    }

    override fun printErrorLog(tag: String, content: Any) {
//        loge(tag,content)
    }
}