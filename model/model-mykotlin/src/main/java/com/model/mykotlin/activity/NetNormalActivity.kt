package com.model.mykotlin.activity

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityNetNormalBinding
import com.model.mykotlin.viewmodel.NetNormalViewModel

@Route(path = RouterPath.PAGE_NET_NORMAL_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class NetNormalActivity : BaseActivity<NetNormalViewModel, MykotlinActivityNetNormalBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.btnNormalUseRxjava3.setOnClickListener {
            viewModel.queryWanAndroidArticlesByRxJava3()
        }

        viewBinding.btnNormalUseCoroutine.setOnClickListener {
            viewModel.queryWanAndroidArticleByCoroutine()
        }

        viewModel.articleJsonStringLiveData.observe(this){jsonString->
            viewBinding.tvNormalUseResultContent.text = jsonString
        }

    }

    override fun initData() {

    }

    override fun createdObserve() {

    }
}