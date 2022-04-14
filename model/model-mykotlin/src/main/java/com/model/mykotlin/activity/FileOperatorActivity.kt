package com.model.mykotlin.activity

import android.Manifest
import android.os.Build
import com.library.common.commonutils.permission.PermissionManager
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.common.commonutils.toastShort
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityFileOperatorBinding

@Route(path = RouterPath.PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class FileOperatorActivity : BaseActivity<BaseViewModel, MykotlinActivityFileOperatorBinding>() {
    /**
     * 相应的清单文件中配置 (The corresponding listing file configuration):
     *
     * <!-- Apps on devices running Android 4.4 (API level 19) or higher cannot
     *      access external storage outside their own "sandboxed" directory, so
     *      the READ_EXTERNAL_STORAGE (and WRITE_EXTERNAL_STORAGE) permissions
     *      aren't necessary. -->
     *
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *
     * <uses-permission
     *      android:name="android.permission.WRITE_EXTERNAL_STORAGE"
     *      tools:ignore="ScopedStorage" />
     */
    private val STORAGE_PERMISSION = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.btnSelectSingleImg.setOnClickListener {
            selectSingleImg()
        }
    }

    /**
     *  选择单张图片
     */
    private fun selectSingleImg() {
        PermissionManager.requestStoragePermission(
            this, STORAGE_PERMISSION
        ) { allGranted: Boolean, grantedList: List<String>, deniedList: List<String> ->
            if (allGranted) {
                toastShort("已授予所有权限")
            } else {
                Log.e("AAAAAAAAAAAAAAAAAA", "grantedList:$grantedList")
                Log.e("AAAAAAAAAAAAAAAAAA", "deniedList:$deniedList")
            }
        }
    }

    private fun chooseFile() {

    }


    override fun initData() {

    }

    override fun createdObserve() {

    }
}