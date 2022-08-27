package com.library.launcher.permission.dsl

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.library.launcher.permission.builder.MultiPermissionBuilder
import com.library.launcher.permission.builder.PermissionBuilder


/**
 * 权限申请DSL
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */

/*----------------------------------------activity单权限申请----------------------------------------*/
inline fun ComponentActivity.permission(
    permission: String,//权限
    builderPermission: PermissionBuilder.() -> Unit
) {
    val builder = PermissionBuilder()
    builder.builderPermission()
    requestPermission(
        permission,
        granted = builder.granted,
        denied = builder.denied,
        explained = builder.explained
    )
}

/*----------------------------------------activity多权限申请----------------------------------------*/
inline fun ComponentActivity.permissions(
    permissions: Array<String>,//权限
    builderPermission: MultiPermissionBuilder.() -> Unit
) {
    val builder =
        MultiPermissionBuilder()
    builder.builderPermission()
    requestMultiplePermissions(
        permissions,
        allGranted = builder.allGranted,
        denied = builder.denied,
        explained = builder.explained
    )
}

/*----------------------------------------fragment单权限申请----------------------------------------*/
inline fun Fragment.permission(
    permission: String,//权限
    builderPermission: PermissionBuilder.() -> Unit
) {
    val builder = PermissionBuilder()
    builder.builderPermission()
    requestPermission(
        permission,
        granted = builder.granted,
        denied = builder.denied,
        explained = builder.explained
    )
}

/*----------------------------------------fragment多权限申请----------------------------------------*/
inline fun Fragment.permissions(
    permissions: Array<String>,//权限
    builderPermission: MultiPermissionBuilder.() -> Unit
) {
    val builder =
        MultiPermissionBuilder()
    builder.builderPermission()
    requestMultiplePermissions(
        permissions,
        allGranted = builder.allGranted,
        denied = builder.denied,
        explained = builder.explained
    )
}