package com.model.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.application.BaseApplication
import com.library.base.datastore.DataStoreUtils
import com.library.base.viewmodel.BaseViewModel
import com.model.login.data.LoginDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    val loginLiveData = MutableLiveData<String>()
    fun queryLoginByCoroutine() {
        viewModelScope.launch {
            try {
                val token: String = DataStoreUtils.get(BaseApplication.appContext, "Authorization")
                Log.d("AAAAAAAAAAAAAAAAA", "token:$token")
                val info =
                    LoginDataSource.queryLoginByCoroutine(token, "admin@qq.com", "Ww30550891000.")
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }

    fun queryLoginBodyByCoroutine() {
        viewModelScope.launch {
            try {
                val token: String = DataStoreUtils.get(BaseApplication.appContext, "Authorization")
                Log.d("AAAAAAAAAAAAAAAAA", "token:$token")
                val info = LoginDataSource.queryLoginBodyByCoroutine(
                    token, "admin@qq.com", "Ww30550891000."
                )
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }

    fun queryLoginByCoroutineFlow() {
        viewModelScope.launch {
            val flow =
                LoginDataSource.queryLoginByCoroutineFlow("2925285800@qq.com", "Ww30550891000.")
            flow.catch {}.collect {

            }
        }
    }

    fun queryUsersByCoroutine() {
        viewModelScope.launch {
            try {
                val info = LoginDataSource.queryUsersByCoroutine()
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
        }
    }
}