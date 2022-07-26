package com.model.mykotlin.activity

import ando.file.core.FileDirectory
import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.application.BaseApplication
import com.library.base.view.activity.BaseActivity
import com.library.common.netconfig.tools.download.FileDownloadProducer
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityNetNormalBinding
import com.model.mykotlin.viewmodel.NetNormalViewModel

@Route(path = RouterPath.PAGE_NET_NORMAL_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class NetNormalActivity : BaseActivity<NetNormalViewModel, MykotlinActivityNetNormalBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*下载文件*/
        viewBinding.btnNormalUseDownload.setOnClickListener {
            Log.d(FileDownloadProducer.TAG, "点击下载按钮")

            FileDirectory.getExternalFilesDirDOWNLOADS()
                ?.let { it1 -> viewModel.downLoad(it1.path) }
        }
        /*协程请求接口*/
        viewBinding.btnNormalUseCoroutine.setOnClickListener {
            viewModel.queryWanAndroidArticleByCoroutine()
        }
        /*展示结果*/
        viewModel.articleJsonStringLiveData.observe(this) { jsonString ->
            viewBinding.tvNormalUseResultContent.text = jsonString
        }

    }

    override fun initData() {
//        crashInJava()
    }

    /**
     * 测试友盟异常捕获
     */
    private fun crashInJava() {
        throw NullPointerException("Player cannot juggle swords")
    }

    override fun createdObserve() {

    }
}