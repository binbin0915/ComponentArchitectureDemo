package com.library.launcher.permission.builder

/**
 * 单权限申请
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class PermissionBuilder {
    var granted: (permission: String) -> Unit = {}
    var denied: (permission: String) -> Unit = {}
    var explained: (permission: String) -> Unit = {}
}