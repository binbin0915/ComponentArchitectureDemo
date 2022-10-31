package com.library.launcher.permission

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.library.launcher.permission.base.BaseLauncher

/**
 * 启动activity，前往设置手动开启权限（后期可封装成方法）
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class ActivityResultLauncher :
    BaseLauncher<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) {
    var onActivityResult: (result: ActivityResult?) -> Unit = {}

    inline fun lunch(
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