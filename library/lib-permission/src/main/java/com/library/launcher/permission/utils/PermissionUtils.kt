package com.library.launcher.permission.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * ### 检查单个权限是否已被同意
 *
 * @param context 上下文
 * @param permission 单个权限
 * @return boolean 通过为true，不通过为false
 */
fun checkPermission(context: Context, permission: String): Boolean {
    return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        context, permission
    )
}


/**
 * ### 检查权限组是否已被同意
 *
 * @param context 上下文
 * @param permissions 待判断的权限组
 * @return ArrayList 返回未通过权限的集合，通过判断size确定
 */
fun checkPermissions(context: Context, permissions: Array<String>): ArrayList<String> {

    val result: ArrayList<String> = arrayListOf()
    for (permission in permissions) {
        if (!checkPermission(context, permission)) {
            //没有开启权限
            result.add(permission)
        }
    }
    return result
}