package com.library.logcat

/**
 * 作用描述：暂时没有想法 先用最简单的方式显示
 *  后续可能会考虑实现:1.日志保存到本地 2.日志文件上传服务器 3.日志分块储存等功能 4.打包时去除打印日志的调用的代码
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
object AppLog {
    /**
     * 日志的Tag
     */
    var logTag = "AppLog"

    /**
     * 是否开启日志打印
     */
    var isOpenLogcat = true

    /**
     * 日志实现类
     */
    private var logcatImpl = LogcatImpl()


    /**
     * 普通的日志打印
     */
    fun log(msg: String) {
        logcatImpl.logI(msg = msg)
    }

    fun log(tag: String, msg: String) {
        logcatImpl.logI(tag = tag, msg = msg)
    }


    /**
     * log打印 带等级
     */
    fun log(level: LogcatLevel, msg: String) {
        logcatImpl.log(level = level, msg = msg)
    }

    /**
     * log打印 带等级 带tag
     */
    fun log(level: LogcatLevel, tag: String, msg: String) {
        logcatImpl.log(level = level, tag = tag, msg = msg)
    }

}