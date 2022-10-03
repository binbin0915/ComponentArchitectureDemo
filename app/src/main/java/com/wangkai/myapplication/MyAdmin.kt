package com.wangkai.myapplication

import android.util.Log
import com.google.auto.service.AutoService
import org.acra.config.ReportingAdministrator

/**
 * @author wangkai
 */
@AutoService(MyAdmin::class)
class MyAdmin : ReportingAdministrator {
    init {
        Log.d("MyAdmin", "MyAdmin was loaded")
    }
}