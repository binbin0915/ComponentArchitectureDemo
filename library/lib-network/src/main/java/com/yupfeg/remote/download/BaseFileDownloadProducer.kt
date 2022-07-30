package com.yupfeg.remote.download

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Http文件下载的基类
 * * 1.0.5版本
 * @author 王凯
 * @date 2020/07/19
 */
abstract class BaseFileDownloadProducer {

    /**
     * 保存网络返回body内容到本地路径文件
     * @param responseBody 网络返回body
     * @param listener 下载监听
     * */
    @Throws(IOException::class)
    protected open suspend fun writeResponseBodyToDiskFile(
        responseBody: ResponseBody,
        listener: DownloadListener
    ) {
        val downloadFile = File(listener.fileDownloadBean.savePath)
        /*文件存在的话，先删除*/
        if (downloadFile.exists()) {
            downloadFile.delete()
        }
        val inputStream = responseBody.byteStream()
        var fos: FileOutputStream? = null
        try {
            withContext(Dispatchers.IO) {
                downloadFile.createNewFile()
            }
            val buffer = ByteArray(2048)

            /**
             * 已下载长度
             */
            var len: Int
            fos = withContext(Dispatchers.IO) {
                //开始下载
                listener.fileDownloadBean.downloadState = 0
                listener.onStartDownload()
                //获取要下载文件总长度
                listener.fileDownloadBean.totalLength = responseBody.contentLength()
                delay(500)
                FileOutputStream(downloadFile, listener.fileDownloadBean.canSuspend)
            }
            do {
                listener.fileDownloadBean.downloadState = 0
                len = withContext(Dispatchers.IO) {
                    inputStream.read(buffer)
                }
                //没有更多数据则跳出循环
                if (len == -1) break
                withContext(Dispatchers.IO) {
                    fos.write(buffer, 0, len)
                }
                listener.run {
                    fileDownloadBean.filePointer += len
                    if (fileDownloadBean.downloadState == 5) {
                        onCancel(downloadFile.path)
                        return
                    }
                    if (fileDownloadBean.downloadState == 1) {
                        onPause(downloadFile.path)
                    }
                    while (fileDownloadBean.downloadState == 1) {
                        delay(100)
                    }
                    val progress =
                        100 - (fileDownloadBean.totalLength / fileDownloadBean.filePointer.toLong()).toInt()
                    if (progress != fileDownloadBean.progress) {
                        fileDownloadBean.progress = progress
                        onProgress(
                            progress = fileDownloadBean.progress
                        )
                    }
                }

            } while (true)
            listener.fileDownloadBean.progress = 100
            listener.fileDownloadBean.downloadState = 3
            listener.onFinishDownload()
        } catch (e: IOException) {
            listener.fileDownloadBean.downloadState = 5
            listener.onFail("IOException")
        } catch (e: FileNotFoundException) {
            listener.fileDownloadBean.downloadState = 5
            listener.onFail("FileNotFoundException")
        } finally {
            withContext(Dispatchers.IO) {
                fos?.flush()
                fos?.close()
                inputStream.close()
            }
        }
    }
}