package com.youjingjiaoyu.upload.net

import android.content.Context
import android.os.Handler
import android.os.Message
import com.youjingjiaoyu.upload.utils.LogUtils

class ResponseCall<T>(context: Context, listener: HttpCallbackModelListener<Any>) {
    /**
     * 用于在子线程和主线程的数据交换
     */
    private val mHandler: Handler

    init {
        val looper = context.mainLooper
        mHandler = object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 0) {
                    //成功
                    LogUtils.log("buffer:${msg.obj}")
                    listener.onFinish(msg.obj)
                } else if (msg.what == 1) {
                    //失败
                    listener.onError(msg.obj as Exception)
                }
            }
        }
    }

    fun doSuccess(response: T) {
        val message = Message.obtain()
        message.obj = response
        message.what = 0
        mHandler.sendMessage(message)
    }

    fun doFail(e: Exception) {
        val message = Message.obtain()
        message.obj = e
        message.what = 1
        mHandler.sendMessage(message)
    }
}