package com.library.router

/**
 * 作用描述：路由页面管理
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class RouterPath {
    companion object {

        /***********分组***********/

        /**
         * 组-开屏页
         */
        const val GROUP_SPLASH = "splash"

        /**
         * 组-首页
         */
        const val GROUP_HOME = "home"

        /**
         * 组-登录页面
         */
        const val GROUP_LOGIN = "login"

        /**
         * 组-kotlin学习页面
         */
        const val GROUP_KOTLIN = "mykotlin"


        /***********页面***********/

        /**
         * 首页模块主页
         */
        const val PAGE_HOME_MAIN_ACTIVITY = "/home/HomeMainActivity"

        /**
         * 登录页面
         */
        const val PAGE_LOGIN_ACTIVITY = "/login/LoginActivity"

        /**
         * kotlin基础学习页面
         */
        const val PAGE_KOTLIN_BASE_ACTIVITY = "/mykotlin/KotlinBaseActivity"


        /***********服务***********/

        /**
         * 登录模块对外提供的服务
         */
        const val SERVICE_LOGIN = "/login/LoginServiceImp"

        /**
         * 首页模块对外提供的服务
         */
        const val SERVICE_HOME = "/home/HomeServiceImp"
    }
}