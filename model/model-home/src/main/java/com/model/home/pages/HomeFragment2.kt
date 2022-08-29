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
import com.model.home.databinding.HomeFragmentPage2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment2 : BaseFragment<BaseViewModel, HomeFragmentPage2Binding>(),
    View.OnClickListener {


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
        viewBinding.btSimpleBrv.setOnClickListener(this)
        viewBinding.btMultiType.setOnClickListener(this)
    }

    override fun createdObserve() {

    }


    override fun defaultLoadingStatus(): Boolean = true

    companion object {
        fun newInstance(): HomeFragment2 {
            val fragment = HomeFragment2()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.bt_simple_brv -> JumpActivity.jump(
                RouterPath.GROUP_BRV,
                RouterPath.PAGE_BRV_SIMPLE
            )
            R.id.bt_multi_type -> JumpActivity.jump(
                RouterPath.GROUP_BRV,
                RouterPath.PAGE_BRV_MULTITYPE
            )
        }
    }
}