package com.library.lib_activityresult.permission.dsl

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment


/**
 * 描述：
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */

const val DENIED = "DENIED"         //拒绝
const val EXPLAINED = "EXPLAINED"   //解释

/**
 * [permission] 权限名称
 * [granted] 申请成功
 * [denied] 被拒绝且未勾选不再询问
 * [explained] 被拒绝且勾选不再询问
 */
inline fun ComponentActivity.requestPermission(
    permission: String,
    crossinline denied: (permission: String) -> Unit = {},
    crossinline explained: (permission: String) -> Unit = {},
    crossinline granted: (permission: String) -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                result -> granted.invoke(permission)
                shouldShowRequestPermissionRationale(permission) -> denied.invoke(permission)
                else -> explained.invoke(permission)
            }
        } else {
            granted.invoke(permission)
        }
    }.launch(permission)
}

/**
 * [permissions] 权限数组
 * [allGranted] 所有权限均申请成功
 * [denied] 被拒绝且未勾选不再询问，同时被拒绝且未勾选不再询问的权限列表
 * [explained] 被拒绝且勾选不再询问，同时被拒绝且勾选不再询问的权限列表
 */
inline fun ComponentActivity.requestMultiplePermissions(
    vararg permissions: String,
    crossinline denied: (List<String>) -> Unit = {},
    crossinline explained: (List<String>) -> Unit = {},
    crossinline allGranted: () -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: MutableMap<String, Boolean> ->
        //过滤 value 为 false 的元素并转换为 list
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val deniedList = result.filter { !it.value }.map { it.key }
            when {
                //拒绝权限
                deniedList.isNotEmpty() -> {
                    //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                    val map = deniedList.groupBy { permission ->
                        if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                    }
                    //被拒接且没勾选不再询问
                    map[DENIED]?.let { denied.invoke(it) }
                    //被拒接且勾选不再询问
                    map[EXPLAINED]?.let { explained.invoke(it) }
                }
                //全部通过
                else -> allGranted.invoke()
            }
        } else {
            allGranted.invoke()
        }

    }.launch(permissions)
}


/**
 * [permission] 权限名称
 * [granted] 申请成功
 * [denied] 被拒绝且未勾选不再询问
 * [explained] 被拒绝且勾选不再询问
 */
inline fun Fragment.requestPermission(
    permission: String,
    crossinline denied: (permission: String) -> Unit = {},
    crossinline explained: (permission: String) -> Unit = {},
    crossinline granted: (permission: String) -> Unit = {}

) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        when {
            result -> granted.invoke(permission)
            shouldShowRequestPermissionRationale(permission) -> denied.invoke(permission)
            else -> explained.invoke(permission)
        }
    }.launch(permission)
}

/**
 * [permissions] 权限数组
 * [allGranted] 所有权限均申请成功
 * [denied] 被拒绝且未勾选不再询问，同时被拒绝且未勾选不再询问的权限列表
 * [explained] 被拒绝且勾选不再询问，同时被拒绝且勾选不再询问的权限列表
 */
inline fun Fragment.requestMultiplePermissions(
    vararg permissions: String,
    crossinline denied: (List<String>) -> Unit = {},
    crossinline explained: (List<String>) -> Unit = {},
    crossinline allGranted: () -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result: MutableMap<String, Boolean> ->
        //过滤 value 为 false 的元素并转换为 list
        val deniedList = result.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                val map = deniedList.groupBy { permission ->
                    if (shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                //被拒接且没勾选不再询问
                map[DENIED]?.let { denied.invoke(it) }
                //被拒接且勾选不再询问
                map[EXPLAINED]?.let { explained.invoke(it) }
            }
            else -> allGranted.invoke()
        }
    }.launch(permissions)
}

