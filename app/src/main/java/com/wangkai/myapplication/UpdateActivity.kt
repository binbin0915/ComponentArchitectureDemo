package com.wangkai.myapplication

import android.view.KeyEvent
import android.view.View
import android.widget.SeekBar
import com.library.base.view.activity.BaseActivity
import com.model.home.viewmodel.HomeMultiTypeBrvActivityViewModel
import com.wangkai.myapplication.databinding.AppActivityUpdateBinding


class UpdateActivity : BaseActivity<HomeMultiTypeBrvActivityViewModel, AppActivityUpdateBinding>() {

    override fun initData() {

    }

    override fun createdObserve() {
        viewBinding.liUpdateEekBar.visibility = View.GONE
        viewBinding.liUpdateBtn.visibility = View.VISIBLE
        /*获取下载任务，初始化界面信息*/

        /*获取策略信息，初始化界面信息*/
        viewBinding.sbEvaluationSseekBar.isClickable = false
        viewBinding.sbEvaluationSseekBar.isEnabled = false
        viewBinding.sbEvaluationSseekBar.isFocusable = false
        /*为下载按钮设置监听*/
        viewBinding.betaConfirmButton.setOnClickListener {


            viewBinding.liUpdateEekBar.visibility = View.VISIBLE
            viewBinding.liUpdateBtn.visibility = View.GONE
        }

        /*为取消按钮设置监听*/
        viewBinding.betaCancelButton.setOnClickListener {
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

    }
}