package com.wangkai.upload.utils

import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * MD5加密工具类
 *
 */
class Md5Utils private constructor() {
    init {
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {
        /**
         * 获取文件的MD5值
         *
         * @param file
         * @return
         */
        @Throws(Exception::class)
        fun getFileMD5(file: File): String {
            if (!file.exists()) {
                return ""
            }
            val fis = FileInputStream(file)
            val digest = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(8192)
            var len: Int
            while (fis.read(buffer).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            fis.close()
            return bytes2Hex(digest.digest())
        }

        /**
         * 一个byte转为2个hex字符
         *
         * @param src byte数组
         * @return 16进制大写字符串
         */
        private fun bytes2Hex(src: ByteArray): String {
            val res = CharArray(src.size shl 1)
            val hexDigits = charArrayOf(
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
            )
            var i = 0
            var j = 0
            while (i < src.size) {
                res[j++] = hexDigits[src[i].toInt() ushr 4 and 0x0F]
                res[j++] = hexDigits[src[i].toInt() and 0x0F]
                i++
            }
            return String(res)
        }
    }
}