package com.model.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.home.api.HomeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作用描述：
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeMainActivityViewModel : BaseViewModel() {
    val pageData = MutableLiveData<ArrayList<String>>()
    val apiService = getApiService(HomeApiService::class.java)

    fun getPageData() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) {
                pageData.value = arrayListOf("第一个页面", "第二个页面", "第三个页面", "第四个页面")
            }
        }

        safeApiRequest<String> {
            api = { apiService.getData() }
            onSuccess {

            }
            onFailed { errorMsg, code ->

            }
        }

    }
}