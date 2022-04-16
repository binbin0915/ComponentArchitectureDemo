package com.flywith24.activityresult

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.library.lib_activityresult.BaseLauncher

/**
 * 描述：启动activity
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class ActivityResultLauncher :
    BaseLauncher<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) {
    var onActivityResult: (result: ActivityResult?) -> Unit = {}

//    inline fun <reified T : Activity> lunchActivity(
//        crossinline setIntent: (intent: Intent) -> Unit = {},
//        noinline onError: (resultCode: Int) -> Unit = {},
//        noinline onSuccess: (intent: Intent?) -> Unit = {}
//    ) {
//        this.onError = onError
//        this.onSuccess = onSuccess
//        val intent = Intent(activity, T::class.java)
//        //根据配置设置 intent
//        setIntent.invoke(intent)
//        launcher.launch(intent)
//    }

    inline fun lunchAction(
        crossinline setIntent: (intent: Intent) -> Unit = {},
        noinline onActivityResult: (result: ActivityResult?) -> Unit = {},
    ) {
        this.onActivityResult = onActivityResult
        val intent = Intent()
        //根据配置设置 intent
        setIntent.invoke(intent)
        launcher.launch(intent)
    }

    override fun onActivityResult(result: ActivityResult?) {
        onActivityResult.invoke(result)
    }
}