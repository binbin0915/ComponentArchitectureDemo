package com.model.home.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.datastore.Constrains
import com.library.base.datastore.EasyDataStore
import com.library.logcat.AppLog
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
        AppLog.log("登录成功昵称:" + EasyDataStore.getData(Constrains.LOGIN_NICKNAME, ""))
        //清除数据
        EasyDataStore.clearData()

    }

    override fun init(context: Context?) {
        AppLog.log("HomeServiceImp init")
    }
}