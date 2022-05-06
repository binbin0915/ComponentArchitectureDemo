package com.model.login

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.RouterPath
import com.library.router.service.HomeService
import com.model.login.databinding.LoginActivityLoginBinding
import com.model.login.service.LoginServiceImp
import com.model.login.viewmodel.LoginViewModel

/**
 * 登录页面
 */
@Route(path = RouterPath.PAGE_LOGIN_ACTIVITY, group = RouterPath.GROUP_LOGIN)
class LoginActivity : BaseActivity<LoginViewModel, LoginActivityLoginBinding>() {

    @Autowired(name = RouterPath.SERVICE_HOME)
    lateinit var loginService: HomeService

    override fun initData() {
        viewBinding.loginBtn.setOnClickListener {
            //登录接口
            viewModel.queryLoginByCoroutine()


        }

        viewModel.loginLiveData.observe(this) { jsonString ->
            loginService.loginSucceed(jsonString)
            finish()
        }
    }

    override fun createdObserve() {

    }
}