package com.library.logcat

import android.util.Log

/**
 * 作用描述：Logcat各个功能实现类
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class LogcatImpl : LogcatService {


    fun log(level: LogcatLevel, tag: String? = LogU.logTag, msg: String?) {
        when (level) {
            LogcatLevel.INFO -> logI(tag = tag, msg = msg)
            LogcatLevel.DEBUG -> logD(tag = tag, msg = msg)
            LogcatLevel.ERROR -> logE(tag = tag, msg = msg)
            LogcatLevel.JSON -> logJson(tag = tag, msg = msg)
            LogcatLevel.OBJECT -> logObj(tag = tag, msg = msg)
            LogcatLevel.VERBOSE -> logV(tag = tag, msg = msg)
            LogcatLevel.DATA -> logData(tag = tag, msg = msg)
            LogcatLevel.WARN -> logW(tag = tag, msg = msg)
        }
    }

    override fun logI(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.i(tag, msg)
        }
    }

    override fun logD(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.d(tag, msg)
        }
    }

    override fun logE(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.e(tag, msg)
        }
    }

    override fun logV(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.v(tag, msg)
        }
    }

    override fun logW(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.w(tag, msg)
        }
    }

    override fun logJson(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.i(tag, msg)
        }
    }

    override fun logObj(tag: String?, msg: String?) {
        //TODO 待实现
    }

    override fun logData(tag: String?, msg: String?) {
        if (LogU.isOpenLogcat && msg != null) {
            Log.i(tag, msg)
        }
    }
}