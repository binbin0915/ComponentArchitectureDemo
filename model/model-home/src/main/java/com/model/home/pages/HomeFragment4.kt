package com.model.home.pages

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.library.base.observermanager.ObserverListener
import com.library.base.observermanager.ObserverManager
import com.library.base.view.fragment.BaseFragment
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.library.router.service.LoginService
import com.library.widget.status.PageStatus
import com.model.home.databinding.HomeFragmentPage4Binding
import com.scwang.smart.refresh.layout.util.SmartUtil
import com.zackratos.ultimatebarx.ultimatebarx.statusBarHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

class HomeFragment4 : BaseFragment<BaseViewModel, HomeFragmentPage4Binding>(), ObserverListener {

    @Autowired(name = RouterPath.SERVICE_LOGIN)
    lateinit var loginService: LoginService

    override fun lazyInit() {
        //模拟页面第一次加载
        lifecycleScope.launch(Dispatchers.IO) {
            delay(500)
            lifecycleScope.launch(Dispatchers.Main) {
                viewModel.changePageStatus(PageStatus.STATUS_SUCCEED)
            }
        }
    }

    override fun initData() {
        viewBinding.loginBtn.setOnClickListener {
            JumpActivity.jump(RouterPath.GROUP_CENTER, RouterPath.PAGE_CENTER_MAIN_ACTIVITY)
        }
    }


    override fun createdObserve() {
        /*因为透明导航栏，所以需要在toolbar添加padding*/
        viewBinding.fragment4Toolbar.apply {
            setPadding(paddingLeft, statusBarHeight + paddingTop, paddingRight, paddingBottom)
        }
        val initialPadding = 80
        val initialElevation = SmartUtil.dp2px(6f).toFloat()
        val layoutParams =
            viewBinding.fragment4CardView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(initialPadding, 0, initialPadding, 0)
        viewBinding.fragment4CardView.layoutParams = layoutParams
        viewBinding.fragment4CardView.elevation = initialElevation
        /*cardView跟随滑动改变margin*/
        viewBinding.fragment4Appbar.apply {
            addOnOffsetChangedListener { appBarLayout, i ->
                run {
                    /* 从1-0*/
                    val a = 1.0f - (abs(i * 1.0f) / appBarLayout.totalScrollRange)
                    val margin = initialPadding * a
                    layoutParams.setMargins(margin.toInt(), 0, margin.toInt(), 0)
                    viewBinding.fragment4CardView.layoutParams = layoutParams
                    val elevation = initialElevation * a
                    viewBinding.fragment4CardView.elevation = elevation
                }
            }
        }
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