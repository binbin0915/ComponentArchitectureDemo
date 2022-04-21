package com.library.launcher.permission

import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.library.launcher.BaseLauncher

/**
 * 描述：使用ActivityResults多权限申请
 *
 * 创建日期： 2022/04/16
 * @author WangKai
 */
class MultiPermissionLauncher :
    BaseLauncher<Array<String>, Map<String, Boolean>>(ActivityResultContracts.RequestMultiplePermissions()) {

    var denied: (List<String>) -> Unit = {}
    var explained: (List<String>) -> Unit = {}
    var allGranted: () -> Unit = {}


    fun lunch(
        permissions: Array<String>,
        denied: (List<String>) -> Unit = {},
        explained: (List<String>) -> Unit = {},
        allGranted: () -> Unit = {}
    ) {
        this.denied = denied
        this.explained = explained
        this.allGranted = allGranted

        launcher.launch(permissions)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(result: Map<String, Boolean>?) {
        //过滤 value 为 false 的元素并转换为 list
        val deniedList = result!!.filter { !it.value }.map { it.key }
        when {
            deniedList.isNotEmpty() -> {
                //对被拒绝全选列表进行分组，分组条件为是否勾选不再询问
                val map = deniedList.groupBy { permission ->
                    if (activity.shouldShowRequestPermissionRationale(permission)) DENIED else EXPLAINED
                }
                //被拒接且没勾选不再询问
                map[DENIED]?.let { denied.invoke(it) }
                //被拒接且勾选不再询问
                map[EXPLAINED]?.let { explained.invoke(it) }
            }
            else -> allGranted.invoke()
        }
    }

    companion object {
        const val DENIED = "DENIED"
        const val EXPLAINED = "EXPLAINED"
    }
}
