package com.model.mykotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.library.common.netconfig.tools.download.Bean
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import com.model.mykotlin.data.remote.RemoteDataSource
import kotlinx.coroutines.launch

class NetNormalViewModel: BaseViewModel() {
    //livedata
    val articleJsonStringLiveData = MutableLiveData<String>()

    fun queryWanAndroidArticleByCoroutine(){
        viewModelScope.launch{
            //TODO 仅作测试，后续使用flow优化
            try {
                val responseEntity = RemoteDataSource.queryWanAndroidArticleByCoroutine(0)
                articleJsonStringLiveData.value = "query WanAndroid Article from Coroutine \n $responseEntity"
            }catch (e : Exception){
                GlobalHttpResponseProcessor.handleHttpError(e)
                articleJsonStringLiveData.value = "query WanAndroid Article from Coroutine $e"
            }
        }
    }
}