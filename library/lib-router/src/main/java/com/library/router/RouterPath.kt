package com.library.router

/**
 * 作用描述：路由页面管理
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class RouterPath {
    companion object {
        //组-首页
        const val GROUP_HOME = "home"

        //组-个人中心
        const val GROUP_CENTER = "center"

        //组-登录
        const val GROUP_LOGIN = "login"

        //组-kotlin
        const val GROUP_KOTLIN = "mykotlin"

        //组-airpods
        const val GROUP_AIRPODS = "airpods"


        //组-BRV示例
        const val GROUP_BRV = "group_brv"

        //首页模块主页
        const val PAGE_HOME_MAIN_ACTIVITY = "/$GROUP_HOME/HomeMainActivity"

        //个人中心
        const val PAGE_CENTER_MAIN_ACTIVITY = "/$GROUP_CENTER/CenterMainActivity"

        //登录页面
        const val PAGE_LOGIN_ACTIVITY = "/$GROUP_LOGIN/LoginActivity"

        //kotlin基础学习页面
        const val PAGE_KOTLIN_BASE_ACTIVITY = "/$GROUP_KOTLIN/KotlinBaseActivity"
        const val PAGE_KOTLIN_FILE_OPERATOR_ACTIVITY = "/$GROUP_KOTLIN/KotlinFileOperatorActivity"
        const val PAGE_NET_NORMAL_ACTIVITY = "/$GROUP_KOTLIN/NetNormalActivity"
        const val PAGE_ARCORE_ACTIVITY = "/$GROUP_KOTLIN/ARCoreActivity"

        //airpods界面
        const val PAGE_AIRPODS_MAIN_ACTIVITY = "/$GROUP_AIRPODS/AirpodsMainActivity"



        //BRV示例
        const val PAGE_BRV_SIMPLE = "/$GROUP_BRV/HomeSimpleBrvActivity"
        const val PAGE_BRV_MULTITYPE = "/$GROUP_BRV/HomeMultiTypeBrvActivity"

        /**
         * # 服务
         * 登录模块对外提供的服务
         */
        const val SERVICE_LOGIN = "/$GROUP_LOGIN/LoginServiceImp"

        /**
         * 首页模块对外提供的服务
         */
        const val SERVICE_HOME = "/$GROUP_HOME/HomeServiceImp"
    }
}