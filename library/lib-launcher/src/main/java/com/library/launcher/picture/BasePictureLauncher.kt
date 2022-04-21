package com.library.launcher.picture

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.LifecycleOwner
import com.library.launcher.BaseLauncher
import com.library.launcher.permission.PermissionLauncher

/**
 * 描述：拍照
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
abstract class BasePictureLauncher<I, O>(contract: ActivityResultContract<I, O>) :
    BaseLauncher<I, O>(contract) {
    val camera by lazy { PermissionLauncher() }
    lateinit var context: Context

    override fun onCreate(owner: LifecycleOwner) {
        if (owner is ComponentActivity) {
            context = owner
            owner.lifecycle.addObserver(camera)
        }
        super.onCreate(owner)
    }
}