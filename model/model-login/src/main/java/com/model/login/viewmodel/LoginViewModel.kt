package com.model.login.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.library.base.viewmodel.BaseViewModel
import com.model.login.data.LoginData
import com.model.login.data.LoginDataSource
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseViewModel() {
    //持久化
    private val preference = PreferenceManager.getDefaultSharedPreferences(application)


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