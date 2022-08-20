package com.model.login.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.login.data.LoginDataSource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    //livedata
    val loginLiveData = MutableLiveData<String>()
    fun queryLoginByCoroutine() {
        viewModelScope.launch {
            try {
                val info =
                    LoginDataSource.queryLoginByCoroutine("2925285800@qq.com", "Ww30550891000.")
                Log.d("AAAAAAAAAAAAAAAAA", "info:$info")
            } catch (e: Exception) {
                Log.d("AAAAAAAAAAAAAAAAA", "error:${e.message}")
            }
//            val flow =
//                LoginDataSource.queryLoginByCoroutineFlow("2925285800@qq.com", "Ww30550891000.")
//            flow.catch {
//                Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it.message}")
//            }.collect {
//                Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it}")
////                //持久化登录信息
////                val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(it)
////                if (isSuccess) {
////                    //业务执行成功
////                    Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it}")
////                    loginLiveData.value = it.data.nickname
////                } else {
////                    //业务执行异常
////                    Log.d("AAAAAAAAAAAAAAAAAAAAA", "CODE:${it.code}   MESSAGE:${it.message}")
////                }
//            }


        }
    }


    fun queryLoginByCoroutineFlow() {
        viewModelScope.launch {
            val flow =
                LoginDataSource.queryLoginByCoroutineFlow("2925285800@qq.com", "Ww30550891000.")
            flow.catch {
                Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it.message}")
            }.collect {
                Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it}")
//                //持久化登录信息
//                val isSuccess = GlobalHttpResponseProcessor.preHandleHttpResponse(it)
//                if (isSuccess) {
//                    //业务执行成功
//                    Log.d("AAAAAAAAAAAAAAAAAAAAA", "1111:${it}")
//                    loginLiveData.value = it.data.nickname
//                } else {
//                    //业务执行异常
//                    Log.d("AAAAAAAAAAAAAAAAAAAAA", "CODE:${it.code}   MESSAGE:${it.message}")
//                }
            }
        }
    }
}