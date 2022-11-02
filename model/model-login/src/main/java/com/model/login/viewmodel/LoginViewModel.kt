package com.model.login.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.library.base.application.BaseApplication
import com.library.base.database.DataStoreUtils
import com.library.base.viewmodel.BaseViewModel
import com.library.common.network.tools.coroutine.preHandleHttpResponse
import com.model.login.data.LoginData
import com.model.login.data.LoginDataSource
import com.model.login.data.UsersData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    /*当暴露 UI 的状态给视图时，应该使用 StateFlow。这是一种安全和高效的观察者，专门用于容纳 UI 状态。*/
    val userDataSharedFlow = MutableSharedFlow<UsersData>()
    val loginDataSharedFlow = MutableSharedFlow<LoginData>()


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
            LoginDataSource.queryLoginByCoroutineFlow("admin@qq.com", "Ww30550891000.")
                .preHandleHttpResponse {
                    Log.d("AAAAAAAAAAXXWDAC", "success：$it")
                    loginDataSharedFlow.emit(it)
                }
        }
    }

    fun queryUsersByCoroutine() {
        viewModelScope.launch {
            try {
                val token: String = DataStoreUtils.get(BaseApplication.appContext, "Authorization")
                val info = LoginDataSource.queryUsersByCoroutine(token)
                userDataSharedFlow.emit(info)
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }
}