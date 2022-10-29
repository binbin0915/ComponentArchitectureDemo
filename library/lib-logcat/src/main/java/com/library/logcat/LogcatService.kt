package com.library.logcat

/**
 * 作用描述：Logcat各个功能抽象
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
interface LogcatService {
    /**
     * 打印Info日志
     */
    fun logI(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印Debug日志
     */
    fun logD(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印错误日志
     */
    fun logE(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印Verbose日志
     */
    fun logV(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印警告日志
     */
    fun logW(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印Json日志
     */
    fun logJson(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印对象日志
     */
    fun logObj(tag: String? = Logcat.logTag, msg: String?)

    /**
     * 打印实体类日志
     */
    fun logData(tag: String? = Logcat.logTag, msg: String?)
}