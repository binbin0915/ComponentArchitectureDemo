package com.model.mykotlin.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.alibaba.android.arouter.facade.annotation.Route
import com.flywith24.activityresult.ActivityResultLauncher
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.lib_activityresult.permission.MultiPermissionLauncher
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityFileOperatorBinding

@Route(path = RouterPath.PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class FileOperatorActivity : BaseActivity<BaseViewModel, MykotlinActivityFileOperatorBinding>() {
    private val multiPermissionLauncher by lazy { MultiPermissionLauncher() }
    private val activityLauncher by lazy { ActivityResultLauncher() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(multiPermissionLauncher)
        lifecycle.addObserver(activityLauncher)
        viewBinding.btnSelectSingleImg.setOnClickListener {
            requestMultiPermissions()
        }
    }

    /**
     * 请求权限
     */

    private fun requestMultiPermissions() {
        multiPermissionLauncher.run {
            lunch(
                //熬申请的权限列表
                permissions = arrayOf(
                    Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS
                ),
                //全部权限均已申请成功
                allGranted = {
                    Log.e("AAAAAAAAAAAAAAAA", "全部权限均已申请成功")
                },
                //权限申请失败且未勾选不再询问，下次可继续申请
                denied = { list ->
                    Log.e("AAAAAAAAAAAAAAAA", "下次可继续申请list:$list")
                },
                //权限申请失败且已勾选不再询问，需要向用户解释原因并引导用户开启权限
                explained = { list ->
                    Log.e("AAAAAAAAAAAAAAAA", "勾选不再询问list:$list")
                    forwardToSettings()
                })
        }
    }

    private fun forwardToSettings() {
        activityLauncher.lunchAction(
            //配置请求 intent
            setIntent = {
                it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                it.data = Uri.fromParts("package", packageName, null)
            }, onActivityResult = {
                Log.e("AAAAAAAAAAAAAAAA", "resultCode:" + it?.resultCode)
            })
    }


    override fun initData() {

    }

    override fun createdObserve() {

    }
}