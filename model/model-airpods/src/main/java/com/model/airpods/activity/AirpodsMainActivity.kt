package com.model.airpods.activity

import android.content.Intent
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.airpods.databinding.AirpodsActivityMainBinding
import com.model.airpods.service.AnPodsService
import com.model.airpods.viewmodel.AirpodsMainActivityViewModel

@Route(path = RouterPath.PAGE_AIRPODS_MAIN_ACTIVITY, group = RouterPath.GROUP_AIRPODS)
class AirpodsMainActivity :
    BaseActivity<AirpodsMainActivityViewModel, AirpodsActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //启动service
        startService(Intent(this, AnPodsService::class.java))
    }

    override fun initData() {

    }

    override fun createdObserve() {

    }
}