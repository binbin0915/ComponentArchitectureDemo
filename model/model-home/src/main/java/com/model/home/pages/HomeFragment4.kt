package com.model.home.pages

import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.zackratos.ultimatebarx.ultimatebarx.statusBarHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt

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
            JumpActivity.jump(RouterPath.GROUP_LOGIN, RouterPath.PAGE_LOGIN_ACTIVITY)
        }
    }


    override fun createdObserve() {
        /*因为透明导航栏，所以需要在toolbar添加padding*/
        viewBinding.fragment4Toolbar.apply {
            setPadding(paddingLeft, statusBarHeight + paddingTop, paddingRight, paddingBottom)
        }
        val initialPadding = 80
        val layoutParams =
            viewBinding.fragment4CardView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.setMargins(initialPadding, 0, initialPadding, 0)
        viewBinding.fragment4CardView.layoutParams = layoutParams
        /*cardView跟随滑动改变margin*/
        viewBinding.fragment4Appbar.apply {
            addOnOffsetChangedListener { appBarLayout, i ->
                run {
                    /* 从1-0：1 - (abs(i * 1.0f) / appBarLayout.totalScrollRange) */
                    val a = initialPadding.toFloat() / appBarLayout.totalScrollRange
                    val side = round((a * i + initialPadding).toDouble()).toInt()
                    layoutParams.setMargins(side, 0, side, 0)
                    viewBinding.fragment4CardView.layoutParams = layoutParams
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