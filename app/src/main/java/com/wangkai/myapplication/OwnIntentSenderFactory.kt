package com.wangkai.myapplication

import android.content.Context
import com.google.auto.service.AutoService
import org.acra.config.CoreConfiguration
import org.acra.sender.ReportSender
import org.acra.sender.ReportSenderFactory

@AutoService(ReportSenderFactory::class)
class OwnIntentSenderFactory : ReportSenderFactory {
    // requires a no arg constructor.
    override fun create(context: Context, config: CoreConfiguration): ReportSender {
        return OwnIntentSender()
    }

    //optional implementation in case you want to disable your sender in certain cases
    override fun enabled(config: CoreConfiguration): Boolean {
        return true
    }
}