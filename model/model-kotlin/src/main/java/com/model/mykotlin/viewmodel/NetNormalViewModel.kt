package com.model.mykotlin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.library.base.viewmodel.BaseViewModel
import com.wangkai.remote.download.DownLoadUtil
import com.wangkai.remote.download.DownloadListener
import com.wangkai.remote.download.entity.FileDownloadBean
import com.wangkai.remote.download.producer.FileDownloadProducer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class NetNormalViewModel : BaseViewModel() {
    //livedata
    val articleJsonStringLiveData = MutableLiveData<String>()
    fun downLoad(appFileDirPath: String) {
        /*下载链接的集合*/
        val urlArrayList: ArrayList<String> = ArrayList()
        urlArrayList.add("https://ceshiaidiandu.oss-cn-beijing.aliyuncs.com//storage/ceshi_uploads/androidapk/202108/beisuketang_202207200851.apk")
        urlArrayList.add("https://ceshiaidiandu.oss-cn-beijing.aliyuncs.com//storage/ceshi_uploads/androidapk/202108/beisuketang_202207200851.apk")
        for (item in urlArrayList.indices) {
            viewModelScope.launch {
                val listener = object : DownloadListener {
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
                            "开始下载item:" + fileDownloadBean.item
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
                            "完成下载item:" + fileDownloadBean.item
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
                DownLoadUtil.download(listener)
            }

        }
    }
}