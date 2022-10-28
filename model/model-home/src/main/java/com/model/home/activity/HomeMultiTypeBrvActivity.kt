package com.model.home.activity

import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.alibaba.android.arouter.facade.annotation.Route
import com.drake.brv.BindingAdapter
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

/**
 * 多类型和单一类型的区别就是getModel()或者getBinding()等获取针对某个类型数据或者视图之前请先判断下itemViewType避免在错误的类型中取不到正确的值
 *
 * 1.不同数据的多类型
 * 2.不同字段的多类型
 * 3.区分类型
 * 4.接口类型
 */


@Route(group = RouterPath.GROUP_BRV, path = RouterPath.PAGE_BRV_MULTITYPE)
class HomeMultiTypeBrvActivity :
    BaseActivity<HomeMultiTypeBrvActivityViewModel, HomeActivityMultiTypeBrvBinding>() {

    /*recyclerview布局适配器*/
    private lateinit var adapter: BindingAdapter

    override fun initData() {
        adapter.models = viewModel.getData()
    }

    override fun createdObserve() {
        adapter = viewBinding.rvMulti.linear().setup {
            addType<MultiModel1>(R.layout.home_item_multi_type1)
            addType<MultiModel2>(R.layout.home_item_multi_type2)
            onBind {
                when (itemViewType) {
                    R.layout.home_item_multi_type1 -> {
                        val simpleModel1: MultiModel1 = getModel()
                        findView<TextView>(R.id.tv_multi1).text = simpleModel1.type
                        findView<ImageView>(R.id.iv_multi1).load(simpleModel1.url)
                    }

                    R.layout.home_item_multi_type2 -> {
                        val simpleModel2: MultiModel2 = getModel()
                        findView<TextView>(R.id.tv_multi2).text = simpleModel2.type
                        findView<ImageView>(R.id.iv_multi2).load(simpleModel2.url)
                    }
                }
            }
        }
        // 点击事件
        adapter.onClick(R.id.rv_parent) {
            when (itemViewType) {
                R.layout.home_item_multi_type1 -> {
                    val simpleModel1: MultiModel1 = getModel()
                    showToast(ToastType.INFO, simpleModel1.type)
                }

                R.layout.home_item_multi_type2 -> {
                    val simpleModel2: MultiModel2 = getModel()
                    showToast(ToastType.INFO, simpleModel2.type)
                }
            }
        }
    }
}