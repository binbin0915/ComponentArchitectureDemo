package com.model.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 作用描述：
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeMainActivityShareViewModel : BaseViewModel() {
    var isOpen = MutableLiveData<Boolean>()

    fun setIsOpen(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) {
                isOpen.value = boolean
            }
        }
    }
}