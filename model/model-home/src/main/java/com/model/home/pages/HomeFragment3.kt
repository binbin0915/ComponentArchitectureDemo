package com.model.home.pages

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.library.base.view.fragment.BaseFragment
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.library.widget.status.PageStatus
import com.model.home.R
import com.model.home.databinding.HomeFragmentPage3Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment3 : BaseFragment<BaseViewModel, HomeFragmentPage3Binding>(),
    View.OnClickListener {


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
        viewBinding.btKotlinBase.setOnClickListener(this)
        viewBinding.btKotlinFunction.setOnClickListener(this)
        viewBinding.btNetworkBase.setOnClickListener(this)
        viewBinding.btNetworkReplaceUrl.setOnClickListener(this)
        viewBinding.btNetworkDownload.setOnClickListener(this)
    }

    /**
     * 创建订阅
     */
    override fun createdObserve() {

    }

    //初始加载loading
    override fun defaultLoadingStatus(): Boolean = true

    companion object {
        fun newInstance(): HomeFragment3 {
            val fragment = HomeFragment3()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_kotlin_base -> {
                JumpActivity.jump(RouterPath.GROUP_KOTLIN, RouterPath.PAGE_KOTLIN_BASE_ACTIVITY)
            }

            R.id.bt_kotlin_function -> {
                JumpActivity.jump(
                    RouterPath.GROUP_KOTLIN, RouterPath.PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY
                )
            }

            R.id.bt_network_base -> {
                JumpActivity.jump(RouterPath.GROUP_KOTLIN, RouterPath.PAGE_NET_NORMAL_ACTIVITY)
            }

            R.id.bt_network_replace_url -> {
                JumpActivity.jump(RouterPath.GROUP_AIRPODS, RouterPath.PAGE_AIRPODS_MAIN_ACTIVITY)
            }

            R.id.bt_network_download -> {

            }


        }
    }
}