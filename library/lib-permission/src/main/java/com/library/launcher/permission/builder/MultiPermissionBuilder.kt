package com.library.launcher.permission.builder

/**
 * 多权限申请
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */

class MultiPermissionBuilder {
    var allGranted: () -> Unit = {}
    var denied: (List<String>) -> Unit = {}
    var explained: (List<String>) -> Unit = {}
}