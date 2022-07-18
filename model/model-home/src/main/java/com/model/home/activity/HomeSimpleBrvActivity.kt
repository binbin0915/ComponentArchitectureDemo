package com.model.home.activity

import com.alibaba.android.arouter.facade.annotation.Route
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

    override fun initData() {
        viewBinding.rv1.linear().setup {
            addType<SimpleModel>(R.layout.home_item_simple)
//            onBind {
//                val simpleModel: SimpleModel = getModel()
//                findView<TextView>(R.id.tv_simple).text = simpleModel.name
//                Glide.with(context).load(simpleModel.url).into(findView(R.id.iv_simple))
//            }
            R.id.tv_simple.onClick {
                showToast(
                    ToastType.INFO,
                    "点击第" + (this.modelPosition + 1) + "项:" + getModel<SimpleModel>().name
                )
            }


        }.models = getData()
    }

    private fun getData(): MutableList<Any> {
        val url =
            "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni7w0LiIWS5IckQE9TG0t1ftC89uRDmF.vB14O6fOc2FZphzCrtsdqH6GAsbLCpfsG5wov8Ozz7TyS45UyAVf6WI!/b&bo=ngL2AZ4C9gEFFzQ!&rf=viewer_4"
        // 在Model中也可以绑定数据
        return mutableListOf<Any>().apply {
            for (i in 0..9) add(SimpleModel("BRV:" + (i + 1), url))
        }
    }

    override fun createdObserve() {

    }
}