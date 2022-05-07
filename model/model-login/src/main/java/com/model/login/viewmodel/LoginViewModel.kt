package com.model.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.login.data.LoginData
import com.model.login.data.LoginDataSource
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    //livedata
    val loginLiveData = MutableLiveData<LoginData>()
    fun queryLoginByCoroutine() {
        viewModelScope.launch {
            val responseEntity =
                LoginDataSource.queryLoginByCoroutine("2925285800@qq.com", "Ww30550891000.")
            //持久化登录信息

            loginLiveData.value = responseEntity
        }
    }
}