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
     * @param fileUrl 文件下载路径url,作为文件唯一表示符
     * @param fileBody 网络返回body
     * @param filePath 文件本地保存路径
     * */
    @Throws(IOException::class)
    protected open suspend fun writeResponseBodyToDiskFile(
        fileBody: ResponseBody,
        filePath: String,
        listener: DownloadListener? = null
    ) {
        val downloadFile = File(filePath)
        /*文件存在的话，先删除*/
        if (downloadFile.exists()) {
            downloadFile.delete()
        }
        val inputStream = fileBody.byteStream()
        var fos: FileOutputStream? = null
        try {
            withContext(Dispatchers.IO) {
                downloadFile.createNewFile()
            }
            val buffer = ByteArray(2048)
            var len: Int
            fos = withContext(Dispatchers.IO) {
                FileOutputStream(downloadFile, listener != null && listener.isResume)
            }
            do {
                len = withContext(Dispatchers.IO) {
                    inputStream.read(buffer)
                }
                //没有更多数据则跳出循环
                if (len == -1) break
                withContext(Dispatchers.IO) {
                    fos.write(buffer, 0, len)
                }
                listener?.run {
                    filePointer += len
                    if (isCancel) {
                        onCancel(downloadFile.path)
                        return
                    }
                    if (isPause) {
                        onPause(downloadFile.path)
                    }
                    while (isPause) {
                        delay(100)
                    }
                }

            } while (true)
            listener?.onFinishDownload()
        } catch (e: IOException) {
            e.printStackTrace()
            listener?.onFail("IOException")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            listener?.onFail("FileNotFoundException")
        } finally {
            withContext(Dispatchers.IO) {
                fos?.flush()
                fos?.close()
                inputStream.close()
            }
        }
    }
}