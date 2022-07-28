package com.model.mykotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.library.common.netconfig.tools.download.FileDownloadProducer
import com.model.mykotlin.data.remote.RemoteDataSource
import com.yupfeg.remote.download.DownloadListener
import com.yupfeg.remote.download.entity.FileDownloadBean
import com.yupfeg.remote.tools.handler.GlobalHttpResponseProcessor
import kotlinx.coroutines.cancel
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
            viewModelScope.launch {
                /*文件名*/
                val listener = object : DownloadListener {
                    /**
                     * 文件bean
                     */
                    override var fileDownloadBean = FileDownloadBean(
                        item = item,
                        url = urlArrayList[item],
                        savePath = appFileDirPath + File.separator + "testDownload_$item.apk",
                        progress = 0,
                        downloadState = -1,
                        canSuspend = true,
                        filePointer = 0,
                        totalLength = 0
                    )

                    override fun onStartDownload() {
                        Log.d(
                            FileDownloadProducer.TAG,
                            "开始下载status:" + fileDownloadBean.downloadState
                        )
                    }

                    override fun onProgress(progress: Int) {
                        //文件的下载进度
                        Log.d(
                            FileDownloadProducer.TAG,
                            "item:" + fileDownloadBean.item + "  progress:$progress" + "正在下载status:" + fileDownloadBean.downloadState
                        )
                    }

                    override fun onFinishDownload() {
                        Log.d(
                            FileDownloadProducer.TAG,
                            "完成下载status:" + fileDownloadBean.downloadState
                        )
                    }

                    override fun onFail(errorInfo: String?) {
                        cancel()
                        Log.d(FileDownloadProducer.TAG, "下载失败:$errorInfo")
                    }

                    override fun onContinue() {

                    }

                    override fun onPause(path: String) {

                    }

                    override fun onCancel(path: String) {

                    }
                }

                RemoteDataSource.download(
                    listener = listener,
                )
            }

        }
    }
}