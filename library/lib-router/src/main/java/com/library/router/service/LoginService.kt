package com.library.router.service

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.facade.template.IProvider
import com.library.logcat.AppLog

/**
 * 作用描述：登录模块对外提供的服务
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
interface LoginService : IProvider {
    fun loginStatusChange(): MutableLiveData<String>
}