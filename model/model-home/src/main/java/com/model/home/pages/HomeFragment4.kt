package com.model.home.pages

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.library.base.datastore.Constrains
import com.library.base.datastore.EasyDataStore
import com.library.base.observermanager.ObserverListener
import com.library.base.observermanager.ObserverManager
import com.library.base.view.fragment.BaseFragment
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.library.router.service.LoginService
import com.library.widget.status.PageStatus
import com.model.home.databinding.HomeFragmentPage4Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment4 : BaseFragment<BaseViewModel, HomeFragmentPage4Binding>(), ObserverListener {

    @Autowired(name = RouterPath.SERVICE_LOGIN)
    lateinit var loginService: LoginService

    override fun lazyInit() {
        //模拟页面第一次加载
        lifecycleScope.launch(Dispatchers.IO) {
            delay(500)
            lifecycleScope.launch(Dispatchers.Main) {
                changePageStatus(PageStatus.STATUS_SUCCEED)
            }
        }
    }

    override fun initData() {
        val userInfo = EasyDataStore.getData(Constrains.LOGIN_NICKNAME, "")
        if (userInfo.isNotEmpty()) {
            viewBinding.userInfo.text = userInfo
        }

        viewBinding.loginBtn.setOnClickListener {
            //清除以前的登录信息
            EasyDataStore.clearData()
            JumpActivity.jump(RouterPath.GROUP_LOGIN, RouterPath.PAGE_LOGIN_ACTIVITY)
        }
    }


    override fun createdObserve() {
        ObserverManager.getInstance().add(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ObserverManager.getInstance().remove(this)
    }

    override fun defaultLoadingStatus(): Boolean = true


    companion object {
        fun newInstance(): HomeFragment4 {
            val fragment = HomeFragment4()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun observerUpData(content: String?) {
        //更新内容
        viewBinding.userInfo.text = content
    }
}