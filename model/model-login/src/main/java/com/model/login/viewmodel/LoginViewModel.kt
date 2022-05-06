package com.model.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.login.data.LoginDataSource
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {

    //livedata
    val loginLiveData = MutableLiveData<String>()

    fun queryLoginByCoroutine() {
        viewModelScope.launch {
            val responseEntity =
                LoginDataSource.queryLoginByCoroutine("2925285800@qq.com", "Ww30550891000.")
            loginLiveData.value = responseEntity.data.nickname
        }
    }
}