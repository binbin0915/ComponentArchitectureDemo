package com.library.launcher.permission.base

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 启动器基类
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
abstract class BaseLauncher<I, O>(private val contract: ActivityResultContract<I, O>) :
    DefaultLifecycleObserver, ActivityResultCallback<O> {
    lateinit var launcher: ActivityResultLauncher<I>
    lateinit var activity: Activity

    @CallSuper
    override fun onCreate(owner: LifecycleOwner) {
        if (owner is ComponentActivity) {
            activity = owner
            launcher = owner.registerForActivityResult(contract, this)
        }
    }

    override fun onActivityResult(result: O?) {

    }
}