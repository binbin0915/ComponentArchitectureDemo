package com.model.airpods.activity

import android.os.Build
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.airpods.databinding.AirpodsActivityMainBinding
import com.model.airpods.service.AnPodsService
import com.model.airpods.util.airPodsConnectionState
import com.model.airpods.viewmodel.AirpodsMainActivityViewModel

@Route(path = RouterPath.PAGE_AIRPODS_MAIN_ACTIVITY, group = RouterPath.GROUP_AIRPODS)
class AirpodsMainActivity :
    BaseActivity<AirpodsMainActivityViewModel, AirpodsActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //启动service，获取蓝牙连接状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(intent.setClass(this, AnPodsService::class.java))
            startService(intent.setClass(this, AnPodsService::class.java))
        } else {
            startService(intent.setClass(this, AnPodsService::class.java))
        }
        //蓝牙连接状态改变
        airPodsConnectionState.observe(this) {
            viewBinding.airpodsState.text = "是否连接：" + it.isConnected + "设备名称：" + it.deviceName
        }
    }

    override fun initData() {

    }

    override fun createdObserve() {

    }
}