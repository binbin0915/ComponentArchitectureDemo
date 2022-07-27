package com.model.mykotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.model.mykotlin.data.remote.RemoteDataSource
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import kotlinx.coroutines.launch
import java.io.File

class NetNormalViewModel : BaseViewModel() {
    //livedata
    val articleJsonStringLiveData = MutableLiveData<String>()


    fun queryWanAndroidArticleByCoroutine() {
        viewModelScope.launch {
            //TODO 仅作测试，后续使用flow优化
            try {
                val responseEntity = RemoteDataSource.queryWanAndroidArticleByCoroutine(0)
                articleJsonStringLiveData.value =
                    "query WanAndroid Article from Coroutine \n $responseEntity"
            } catch (e: Exception) {
                GlobalHttpResponseProcessor.handleHttpError(e)
                articleJsonStringLiveData.value = "query WanAndroid Article from Coroutine $e"
            }
        }
    }


    fun downLoad(appFileDirPath: String) {
        /*下载链接的集合*/
        val urlArrayList: ArrayList<String> = ArrayList()
        urlArrayList.add("https://ceshiaidiandu.oss-cn-beijing.aliyuncs.com//storage/ceshi_uploads/androidapk/202108/beisuketang_202207200851.apk")
        urlArrayList.add("https://ceshiaidiandu.oss-cn-beijing.aliyuncs.com//storage/ceshi_uploads/androidapk/202108/beisuketang_202207200851.apk")
        for (item in urlArrayList.indices) {
            /*文件名*/
            val downloadFileName = appFileDirPath + File.separator + "testDownload_$item.apk"
            RemoteDataSource.download(
                item = item,
                downloadUrl = urlArrayList[item],
                savePath = downloadFileName,
            )
        }
    }
}