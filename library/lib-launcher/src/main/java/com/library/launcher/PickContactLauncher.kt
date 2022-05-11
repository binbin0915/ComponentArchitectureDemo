package com.library.launcher

import android.Manifest
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.library.launcher.permission.PermissionLauncher

/**
 * 描述：
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class PickContactLauncher : BaseLauncher<Void?, Uri?>(ActivityResultContracts.PickContact()) {
    var onError: (message: String) -> Unit = {}
    var onSuccess: (uri: Uri?) -> Unit = {}
    val permission by lazy { PermissionLauncher() }

    override fun onCreate(owner: LifecycleOwner) {
        if (owner is ComponentActivity) {
            owner.lifecycle.addObserver(permission)
        }
        super.onCreate(owner)
    }

    fun lunch(
        onError: (message: String) -> Unit = {},
        onSuccess: (uri: Uri?) -> Unit = {}
    ) {
        this.onError = onError
        this.onSuccess = onSuccess
        permission.lunch(Manifest.permission.READ_CONTACTS) {
            granted = {
                launcher.launch(null)
            }
            denied = { onError.invoke("read contacts permission denied") }
            explained = { onError.invoke("read contacts permission denied") }
        }
    }

    override fun onActivityResult(result: Uri?) = onSuccess.invoke(result)
}