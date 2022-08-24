package com.model.login

import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.library.router.service.HomeService
import com.model.login.databinding.LoginActivityLoginBinding
import com.model.login.viewmodel.LoginViewModel

/**
 * 登录页面
 */
@Route(path = RouterPath.PAGE_LOGIN_ACTIVITY, group = RouterPath.GROUP_LOGIN)
class LoginActivity : BaseActivity<LoginViewModel, LoginActivityLoginBinding>() {

    @Autowired(name = RouterPath.SERVICE_HOME)
    lateinit var homeService: HomeService

    override fun initData() {
        viewBinding.loginBtn.setOnClickListener {
            //登录接口 -- url
//            viewModel.queryLoginByCoroutine()
            //登录接口 -- body
//            viewModel.queryLoginBodyByCoroutine()
            //登录接口 -- flow
            viewModel.queryLoginByCoroutineFlow()
        }

        viewBinding.getUser.setOnClickListener {
            viewModel.queryUsersByCoroutine()
        }
        //获取到登录数据
        viewModel.loginLiveData.observe(this) {
            //保存登录信息
            Log.e("AAAAAAAWAWAAAAAAAAAAAA", "获取所有用户信息:$it")
        }
    }

    override fun createdObserve() {
        viewBinding.loginBtn.background =
            AppCompatResources.getDrawable(
                this,
                com.library.common.R.drawable.shape_rectangle_solid
            )
        viewBinding.getUser.background =
            AppCompatResources.getDrawable(
                this,
                com.library.common.R.drawable.shape_rectangle_solid
            )
    }
}