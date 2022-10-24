package com.model.home.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.logcat.LogU
import com.library.router.RouterPath
import com.library.router.service.HomeService

/**
 * 作用描述：
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
@Route(path = RouterPath.SERVICE_HOME, name = "首页模块服务")
class HomeServiceImp : HomeService {

    override fun loginSucceed() {
//        ObserverManager.getInstance().notifyObserver()
    }

    override fun init(context: Context?) {
        LogU.log("HomeServiceImp init")
    }
}