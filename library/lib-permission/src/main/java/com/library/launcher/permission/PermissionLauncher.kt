package com.library.launcher.permission

import androidx.activity.result.contract.ActivityResultContracts
import com.library.launcher.BaseLauncher
import com.library.launcher.permission.builder.PermissionBuilder

/**
 * 单个权限申请
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class PermissionLauncher :
    BaseLauncher<String, Boolean>(ActivityResultContracts.RequestPermission()) {
    var granted: (permission: String) -> Unit = {}
    var denied: (permission: String) -> Unit = {}
    var explained: (permission: String) -> Unit = {}
    var permission: String = ""

    fun lunch(permission: String, builderPermission: PermissionBuilder.() -> Unit) {
        val builder = PermissionBuilder()
        builder.builderPermission()
        this.granted = builder.granted
        this.explained = builder.explained
        this.denied = builder.denied
        this.permission = permission
        launcher.launch(permission)
    }

    override fun onActivityResult(result: Boolean?) {
        when (result) {
            true -> granted.invoke(permission)
            else -> explained.invoke(permission)
        }
    }
}