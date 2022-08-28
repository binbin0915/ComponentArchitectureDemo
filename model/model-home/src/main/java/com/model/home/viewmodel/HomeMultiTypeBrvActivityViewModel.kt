package com.model.home.viewmodel

import com.library.base.viewmodel.BaseViewModel
import com.model.home.model.MultiModel1
import com.model.home.model.MultiModel2

/**
 * 作用描述：
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeMultiTypeBrvActivityViewModel : BaseViewModel(){

    public fun getData(): MutableList<Any> {
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