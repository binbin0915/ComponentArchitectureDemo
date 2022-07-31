package com.model.mykotlin.activity

import android.Manifest
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.launcher.permission.PermissionLauncher
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityArcoreBinding

/**
 * 1. 检查并申请申请相机权限
 */

@Route(path = RouterPath.PAGE_ARCORE_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class ARCoreActivity : BaseActivity<BaseViewModel, MykotlinActivityArcoreBinding>() {

    private val permissionLauncher by lazy { PermissionLauncher() }
    override fun initData() {
        maybeEnableArButton()
    }

    override fun createdObserve() {
        lifecycle.addObserver(permissionLauncher)
    }

    @SuppressLint("MissingPermission")
    private fun takeCameraLauncher() {
        permissionLauncher.lunch(Manifest.permission.CAMERA) {
            //全部权限均已申请成功
            granted = {
                //处理成功后的逻辑
                createSession()
            }
        }
    }

    private lateinit var session: Session
    private fun createSession() {
        session = Session(this)
        val config = Config(session)
        session.configure(config)
    }

    override fun onResume() {
        super.onResume()
        takeCameraLauncher()
    }

    override fun onDestroy() {
        super.onDestroy()
        session.close()
    }

    private fun maybeEnableArButton() {
        val availability = ArCoreApk.getInstance().checkAvailability(this)
        if (availability.isTransient) {
            Handler(Looper.getMainLooper()).postDelayed({
                maybeEnableArButton()
            }, 200)
        }
        if (availability.isSupported) {
            viewBinding.btArcore.visibility = View.VISIBLE
            viewBinding.btArcore.isEnabled = true
        } else {
            viewBinding.btArcore.visibility = View.INVISIBLE
            viewBinding.btArcore.isEnabled = false
        }
    }
}