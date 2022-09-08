package com.model.login

import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.expand.onClickListener
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.library.router.service.HomeService
import com.model.login.databinding.LoginActivityLoginBinding
import com.model.login.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

/**
 * 登录页面
 */
@Route(path = RouterPath.PAGE_LOGIN_ACTIVITY, group = RouterPath.GROUP_LOGIN)
class LoginActivity : BaseActivity<LoginViewModel, LoginActivityLoginBinding>() {

    @Autowired(name = RouterPath.SERVICE_HOME)
    lateinit var homeService: HomeService

    override fun initData() {
        viewBinding.loginBtn.onClickListener(lifecycleScope) {
            //登录接口 -- url
//            viewModel.queryLoginByCoroutine()
            //登录接口 -- body
//            viewModel.queryLoginBodyByCoroutine()
            //登录接口 -- flow
            viewModel.queryLoginByCoroutineFlow()
        }

        viewBinding.getUser.onClickListener(lifecycleScope) {
            viewModel.queryUsersByCoroutine()
        }


        viewBinding.loginBtn.background = ResourcesCompat.getDrawable(
            resources, com.library.common.R.drawable.shape_rectangle_solid, null
        )
        viewBinding.getUser.background = ResourcesCompat.getDrawable(
            resources, com.library.common.R.drawable.shape_rectangle_solid, null
        )
    }

    override fun createdObserve() {
        //获取到登录数据
        lifecycleScope.launch {
            viewModel.userDataSharedFlow.collect {
                //保存登录信息
                Log.e("AAAAAAAWAWAAAAAAAAAAAA", "管理员获取所有用户信息:$it")
            }
        }

        lifecycleScope.launch {
            viewModel.loginDataSharedFlow.collect {
                //保存登录信息
                Log.e("AAAAAAAWAWAAAAAAAAAAAA", "登录成功:$it")
            }
        }
    }
}