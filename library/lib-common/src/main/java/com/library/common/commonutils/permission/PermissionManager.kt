package com.library.common.commonutils.permission


import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

/**
 * 权限框架的封装
 */
object PermissionManager {

    /**
     * fragment申请权限
     */
    fun requestStoragePermission(
        fragment: Fragment,/*fragment*/
        vararg permissions: String,/*可变参数权限组*/
        block: (allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) -> Unit
    ) {
        requestStoragePermission(fragment.requireActivity(), permissions, block)
    }

    /**
     * activity申请权限
     */
    fun requestStoragePermission(
        activity: FragmentActivity,/*activity*/
        permissions: Array<out String>,/*可变参数权限组*/
        block: (allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) -> Unit
    ) {
        PermissionX.init(activity).permissions(*permissions)
            //用户拒绝一次权限后
//            .onExplainRequestReason { scope, deniedList, beforeRequest ->
//                val message = "PermissionX需要您同意以下权限才能正常使用"
//                val dialog = PermissionDialog(activity, message, deniedList)
//                scope.showRequestReasonDialog(dialog)
//            }
            //永久拒绝权限
            .onForwardToSettings { scope, deniedList ->
                val message = "您需要去设置中手动开启以下权限"
//                val dialog = PermissionDialog(activity, message, deniedList)
//                scope.showForwardToSettingsDialog(dialog)
                scope.showForwardToSettingsDialog(deniedList, "请在设置中手动开启以下权限", "允许", "取消")
            }
            //权限申请成功
            .request { allGranted, grantedList, deniedList ->
                block.invoke(allGranted, grantedList, deniedList)
            }
    }
}