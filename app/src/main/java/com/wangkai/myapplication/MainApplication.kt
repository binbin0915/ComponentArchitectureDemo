package com.wangkai.myapplication

import com.library.base.application.BaseApplication
import com.library.network.NetworkManage

/**
 * 作用描述：壳项目MainApplication
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class MainApplication : BaseApplication() {

    override fun appInit() {
        NetworkManage.config().build()
    }
}