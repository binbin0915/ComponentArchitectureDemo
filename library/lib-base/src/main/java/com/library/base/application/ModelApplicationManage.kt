package com.library.base.application

/**
 * 作用描述：业务模块的Application管理
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
object ModelApplicationManage {

    val applications = arrayListOf<String>()


    init {
        //开屏模块
        applications.add("com.model.splash.application.SplashModelApplication")
        //首页模块
        applications.add("com.model.home.application.HomeModelApplication")
        //登录模块
        applications.add("com.model.login.application.LoginModelApplication")
    }

}