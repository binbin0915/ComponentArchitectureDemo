package com.model.home.viewmodel

import com.library.base.viewmodel.BaseViewModel
import com.model.home.model.SimpleModel

/**
 * 作用描述：
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeSimpleBrvActivityViewModel : BaseViewModel(){
    public fun getData(): MutableList<Any> {
        val url =
            "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni7w0LiIWS5IckQE9TG0t1ftC89uRDmF.vB14O6fOc2FZphzCrtsdqH6GAsbLCpfsG5wov8Ozz7TyS45UyAVf6WI!/b&bo=ngL2AZ4C9gEFFzQ!&rf=viewer_4"
        // 在Model中也可以绑定数据
        return mutableListOf<Any>().apply {
            for (i in 0..9) add(SimpleModel("BRV:" + (i + 1), url))
        }
    }
}