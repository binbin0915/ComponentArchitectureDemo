package com.model.airpods.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.airpods.databinding.AirpodsActivityMainBinding
import com.model.airpods.service.AnPodsService
import com.model.airpods.util.ACTION_POPUP
import com.model.airpods.viewmodel.AirpodsMainActivityViewModel

@Route(path = RouterPath.PAGE_AIRPODS_MAIN_ACTIVITY, group = RouterPath.GROUP_AIRPODS)
class AirpodsMainActivity :
    BaseActivity<AirpodsMainActivityViewModel, AirpodsActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //启动service，获取蓝牙连接状态
        val intent = Intent(this, AnPodsService::class.java).apply {
            action = ACTION_POPUP
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun initData() {

    }

    override fun createdObserve() {

    }
}