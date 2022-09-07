package com.model.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.application.BaseApplication
import com.library.base.datastore.DataStoreUtils
import com.library.base.viewmodel.BaseViewModel
import com.library.common.network.tools.coroutine.preHandleHttpResponse
import com.model.login.data.LoginDataSource
import com.model.login.data.UsersData
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    /*通过livedata暴露数据给view*/
    val loginLiveData = MutableLiveData<UsersData>()

    /*当暴露 UI 的状态给视图时，应该使用 StateFlow。这是一种安全和高效的观察者，专门用于容纳 UI 状态。*/


    fun queryLoginByCoroutine() {
        viewModelScope.launch {
            try {
                val info = LoginDataSource.queryLoginByCoroutine("admin@qq.com", "Ww30550891000.")
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }

    fun queryLoginBodyByCoroutine() {
        viewModelScope.launch {
            try {
                val info = LoginDataSource.queryLoginBodyByCoroutine(
                    "admin@qq.com", "Ww30550891000."
                )
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }

    fun queryLoginByCoroutineFlow() {
        viewModelScope.launch {
            LoginDataSource.queryLoginByCoroutineFlow("admin@qq.com", "Ww305508911000.")
                .preHandleHttpResponse({
                    Log.d("AAAAAAAAAAXXWDAC", "success：$this")
                }, {
                    Log.d("AAAAAAAAAAXXWDAC", "fail：$it")
                })
        }
    }

    fun queryUsersByCoroutine() {
        viewModelScope.launch {
            try {
                val token: String = DataStoreUtils.get(BaseApplication.appContext, "Authorization")
                val info = LoginDataSource.queryUsersByCoroutine(token)
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
                loginLiveData.value = info
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }
}