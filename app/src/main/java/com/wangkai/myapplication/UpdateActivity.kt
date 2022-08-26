package com.wangkai.myapplication

import android.view.KeyEvent
import android.view.View
import android.widget.SeekBar
import com.library.base.view.activity.BaseActivity
import com.model.home.viewmodel.HomeMultiTypeBrvActivityViewModel
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.download.DownloadListener
import com.tencent.bugly.beta.download.DownloadTask
import com.wangkai.myapplication.databinding.AppActivityUpdateBinding


class UpdateActivity : BaseActivity<HomeMultiTypeBrvActivityViewModel, AppActivityUpdateBinding>() {
    fun updateBtn(task: DownloadTask) {
        /*根据下载任务状态设置按钮*/
        when (task.status) {
            DownloadTask.INIT, DownloadTask.DELETED, DownloadTask.FAILED -> {
                viewBinding.liUpdateEekBar.visibility = View.GONE
                viewBinding.liUpdateBtn.visibility = View.VISIBLE
                viewBinding.betaConfirmButton.text = "立即更新"
            }
            DownloadTask.COMPLETE -> {
                viewBinding.liUpdateEekBar.visibility = View.GONE
                viewBinding.liUpdateBtn.visibility = View.VISIBLE
                viewBinding.betaConfirmButton.text = "安装"
            }
            DownloadTask.DOWNLOADING -> {
                viewBinding.betaConfirmButton.text = "暂停"
            }
            DownloadTask.PAUSED -> {
                viewBinding.betaConfirmButton.text = "继续下载"
            }
        }
    }

    override fun initData() {

    }

    override fun createdObserve() {
        viewBinding.liUpdateEekBar.visibility = View.GONE
        viewBinding.liUpdateBtn.visibility = View.VISIBLE
        /*获取下载任务，初始化界面信息*/
        updateBtn(Beta.getStrategyTask())
        /*获取策略信息，初始化界面信息*/
        viewBinding.betaTitle.text = Beta.getUpgradeInfo().title
        viewBinding.betaUpgradeFeature.text = Beta.getUpgradeInfo().newFeature
        viewBinding.sbEvaluationSseekBar.isClickable = false
        viewBinding.sbEvaluationSseekBar.isEnabled = false
        viewBinding.sbEvaluationSseekBar.isFocusable = false
        /*为下载按钮设置监听*/
        viewBinding.betaConfirmButton.setOnClickListener {
            val task = Beta.startDownload()
            viewBinding.liUpdateEekBar.visibility = View.VISIBLE
            viewBinding.liUpdateBtn.visibility = View.GONE
            updateBtn(task)
        }

        /*为取消按钮设置监听*/
        viewBinding.betaCancelButton.setOnClickListener {
            Beta.cancelDownload()
            finish()
        }
        viewBinding.sbEvaluationSseekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val text = "更新进度%f%%"
                viewBinding.seekText2.text = String.format(text, (progress.toFloat() / 10))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        /*注册下载监听，监听下载事件*/
        Beta.registerDownloadListener(object : DownloadListener {
            override fun onReceive(task: DownloadTask) {
                updateBtn(task)
                viewBinding.seekText2.visibility = View.VISIBLE
                viewBinding.sbEvaluationSseekBar.progress =
                    (task.savedLength * 1000 / Beta.getUpgradeInfo().fileSize).toInt()
            }

            override fun onCompleted(task: DownloadTask) {
                updateBtn(task)
            }

            override fun onFailed(task: DownloadTask, code: Int, extMsg: String) {
                updateBtn(task)
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        //屏蔽返回按钮
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        /*注销下载监听*/
        Beta.unregisterDownloadListener()
    }
}