package com.wangkai.upload.activity

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.wangkai.upload.interfaces.AppDownloadListener
import com.wangkai.upload.interfaces.AppUpdateInfoListener
import com.wangkai.upload.interfaces.MD5CheckListener
import com.wangkai.upload.model.DownloadInfo
import com.wangkai.upload.utils.RootActivity

/**
 * 后台更新静默下载
 * @author wangkai
 */
class UpdateBackgroundActivity : RootActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setGravity(Gravity.START or Gravity.TOP)
        val attributes = window.attributes
        attributes.x = 0
        attributes.y = 0
        attributes.height = 1
        attributes.width = 1
        download()
        finish()
    }

    override fun obtainDownloadListener(): AppDownloadListener? {
        return null
    }

    override fun obtainMd5CheckListener(): MD5CheckListener? {
        return null
    }

    override fun obtainAppUpdateInfoListener(): AppUpdateInfoListener? {
        return null
    }

    companion object {
        /**
         * 启动Activity
         *
         * @param context
         * @param info
         */
        fun launch(context: Context, info: DownloadInfo) {
            launchActivity(context, info, UpdateBackgroundActivity::class.java)
        }
    }
}