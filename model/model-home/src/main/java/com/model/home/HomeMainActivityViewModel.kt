package com.model.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.home.api.HomeApiService
import com.model.home.api.MyData
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
    //获取service

//    private val apiService = getApiService(HomeApiService::class.java)

    fun getPageData() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.Main) {
                pageData.value = arrayListOf("第一个页面", "第二个页面", "第三个页面", "第四个页面")
            }
        }

        //dsl请求

//        safeApiRequest<MyData> {
//            api = { apiService.getData() }
//            onSuccess {
//                Log.e("AAAAAAAAAAAAAAAAAAAAA", "onSuccess")
//            }
//            onFailed { errorMsg, code ->
//                Log.e("AAAAAAAAAAAAAAAAAAAAA", "onFailed:$errorMsg   onFailed:$code")
//            }
//        }

    }
}