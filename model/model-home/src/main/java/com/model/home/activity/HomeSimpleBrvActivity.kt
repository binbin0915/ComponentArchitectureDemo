package com.model.home.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.brv.BindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.library.base.expand.ToastType
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.home.R
import com.model.home.databinding.HomeActivitySimpleBrvActivityBinding
import com.model.home.model.SimpleModel
import com.model.home.viewmodel.HomeSimpleBrvActivityViewModel


@Route(group = RouterPath.GROUP_BRV, path = RouterPath.PAGE_BRV_SIMPLE)
class HomeSimpleBrvActivity :
    BaseActivity<HomeSimpleBrvActivityViewModel, HomeActivitySimpleBrvActivityBinding>() {

    private lateinit var adapter: BindingAdapter
    override fun initData() {
        adapter.models = viewModel.getData()
    }

    override fun createdObserve() {
        adapter = viewBinding.rv1.linear().setup {
            addType<SimpleModel>(R.layout.home_item_simple)
            R.id.tv_simple.onClick {
                showToast(
                    ToastType.INFO,
                    "点击第" + (this.modelPosition + 1) + "项:" + getModel<SimpleModel>().name
                )
            }
        }
    }
}