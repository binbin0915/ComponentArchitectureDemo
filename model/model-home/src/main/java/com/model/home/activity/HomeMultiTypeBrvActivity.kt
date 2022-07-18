package com.model.home.activity

import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
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
    override fun initData() {

    }

    override fun createdObserve() {
        viewBinding.rvMulti.linear().setup {
            addType<MultiModel1>(R.layout.home_item_multi_type1)
            addType<MultiModel2>(R.layout.home_item_multi_type2)

            onBind {
                when (itemViewType) {
                    R.layout.home_item_multi_type1 -> {
                        val simpleModel1: MultiModel1 = getModel()
                        findView<TextView>(R.id.tv_multi1).text = simpleModel1.type
                        Glide.with(context).load(simpleModel1.url).into(findView(R.id.iv_multi1))
                    }

                    R.layout.home_item_multi_type2 -> {
                        val simpleModel2: MultiModel2 = getModel()
                        findView<TextView>(R.id.tv_multi2).text = simpleModel2.type
                        Glide.with(context).load(simpleModel2.url).into(findView(R.id.iv_multi2))
                    }
                }
            }
        }.models = getData()

        // 点击事件
        viewBinding.rvMulti.bindingAdapter.onClick(R.id.rv_parent) {
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

    private fun getData(): MutableList<Any> {
        val url1 =
            "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni7w0LiIWS5IckQE9TG0t1ftC89uRDmF.vB14O6fOc2FZphzCrtsdqH6GAsbLCpfsG5wov8Ozz7TyS45UyAVf6WI!/b&bo=ngL2AZ4C9gEFFzQ!&rf=viewer_4"
        val url2 =
            "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni0YAeqnprq8Bz*2LVpYQXcbygVY1K8xi7t8fOe6KdEK6V*hj6vlsz2CJbP5obbQKYYelaUfvptiyFC83Y9SAB84!/b&bo=CQI3AQkCNwEFFzQ!&rf=viewer_4"

        return mutableListOf(
            MultiModel1(url = url1),
            MultiModel1(url = url1),
            MultiModel2(url = url2),
            MultiModel2(url = url2),
            MultiModel2(url = url2),
            MultiModel1(url = url1),
            MultiModel2(url = url2),
            MultiModel1(url = url1),
        )
    }
}