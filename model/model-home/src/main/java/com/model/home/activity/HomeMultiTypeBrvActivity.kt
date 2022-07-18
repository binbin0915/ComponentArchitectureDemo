package com.model.home.activity

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.library.base.expand.ToastType
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.home.R
import com.model.home.databinding.HomeActivityMultiTypeBrvBinding
import com.model.home.model.MultiModel1
import com.model.home.model.MultiModel2
import com.model.home.viewmodel.HomeMultiTypeBrvActivityViewModel

@Route(group = RouterPath.GROUP_BRV, path = RouterPath.PAGE_BRV_MULTITYPE)
class HomeMultiTypeBrvActivity :
    BaseActivity<HomeMultiTypeBrvActivityViewModel, HomeActivityMultiTypeBrvBinding>() {
    override fun initData() {

    }

    override fun createdObserve() {
        viewBinding.rvMulti.linear().setup {
            addType<MultiModel1>(R.layout.home_item_multi_type1)
            addType<MultiModel2>(R.layout.home_item_multi_type2)
        }.models = getData()

        // 点击事件
        viewBinding.rvMulti.bindingAdapter.onClick(R.id.ll_parent) {
            showToast(ToastType.INFO, findView<TextView>(R.id.tv_type).text.toString())
//            when (itemViewType) {
//                R.layout.home_item_multi_type1 -> toast("类型1")
//                else -> toast("类型2")
//            }
        }
    }

    private fun getData(): MutableList<Any> {
        return mutableListOf(
            MultiModel1(),
            MultiModel1(),
            MultiModel2(),
            MultiModel2(),
            MultiModel2(),
            MultiModel1(),
            MultiModel2(),
            MultiModel1(),
            MultiModel1(),
            MultiModel2()
        )
    }
}