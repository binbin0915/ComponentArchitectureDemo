package com.yupfeg.remote.download

import android.util.Log
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.jvm.Throws

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
        fileUrl: String,
        fileBody : ResponseBody,
        filePath : String,
        listener: DownloadListener?=null
    ){
        val downloadFile = File(filePath)
        if (downloadFile.exists()){
            downloadFile.delete()
        }
        val inputStream = fileBody.byteStream()
        var fos : FileOutputStream ?= null
        try {
            downloadFile.createNewFile()
            val buffer = ByteArray(2048)
            var len: Int
            fos = FileOutputStream(downloadFile,listener != null && listener.isResume)
            do {
                len = inputStream.read(buffer)
                //没有更多数据则跳出循环
                if (len == -1) break
                fos.write(buffer, 0, len)
                listener?.run {
                    filePointer += len
                    Log.d("FILE_DOWNLOAD_TAG", "filePointer:$filePointer")
                    if (isCancel) {
                        onCancel(downloadFile.path)
                        return
                    }
                    if (isPause) {
                        onPause(downloadFile.path)
                    }
                    while (isPause) {
//                        delay(100)
                    }
                }

            }while (true)
        }catch (e : IOException){
            throw e
        }finally {
            fos?.flush()
            fos?.close()
            inputStream.close()
        }
    }
}