package com.wangkai.remote.log

/**
 * 描述：网络请求日志输出接口声明
 * @author wangkai
 * @date 2022/07/02
 */
interface HttpLogPrinter {
    /**是否开启输出日志操作*/
    val isPrintLog : Boolean

    /**输出debug日志*/
    fun printDebugLog(tag : String, content : Any)

    /**输出info日志*/
    fun printInfoLog(tag: String,content: Any)

    /**输出warning日志*/
    fun printWarningLog(tag : String,content : Any)

    /**输出error日志*/
    fun printErrorLog(tag : String,content : Any)
}