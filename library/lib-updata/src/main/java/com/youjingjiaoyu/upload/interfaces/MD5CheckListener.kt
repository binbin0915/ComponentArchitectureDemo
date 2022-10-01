package com.youjingjiaoyu.upload.interfaces

/**
 * MD5检验监听 如果开启了MD5校验才会回调
 *
 * @author wangk
 */
interface MD5CheckListener {
    /**
     * 校验失败
     *
     * @param originMD5 app
     * @param localMD5  本地文件
     */
    fun fileMd5CheckFail(originMD5: String, localMD5: String)

    /**
     * 校验成功
     */
    fun fileMd5CheckSuccess()
}