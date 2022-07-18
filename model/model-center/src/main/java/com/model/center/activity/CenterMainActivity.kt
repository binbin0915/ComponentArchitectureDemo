package com.model.center.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.RouterPath
import com.model.center.databinding.CenterActivityCenterMainBinding

@Route(path = RouterPath.PAGE_CENTER_MAIN_ACTIVITY, group = RouterPath.GROUP_CENTER)
class CenterMainActivity : BaseActivity<BaseViewModel, CenterActivityCenterMainBinding>() {
    override fun initData() {

    }

    override fun createdObserve() {

    }
}