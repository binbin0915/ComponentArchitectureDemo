package com.model.mykotlin.activity

import ando.file.core.FileDirectory
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.mykotlin.databinding.MykotlinActivityNetNormalBinding
import com.model.mykotlin.viewmodel.NetNormalViewModel
import com.wangkai.remote.download.producer.FileDownloadProducer

@Route(path = RouterPath.PAGE_NET_NORMAL_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class NetNormalActivity : BaseActivity<NetNormalViewModel, MykotlinActivityNetNormalBinding>() {
    override fun createdObserve() {
        viewBinding.btnNormalUseDownload.setOnClickListener {
            Log.d(FileDownloadProducer.TAG, "点击下载按钮")

            FileDirectory.getExternalFilesDirDOWNLOADS()
                ?.let { it1 -> viewModel.downLoad(it1.path) }
        }
        viewBinding.btnNormalUseCoroutine.setOnClickListener {
            viewModel.queryWanAndroidArticleByCoroutine()
        }
    }

    override fun initData() {
        viewModel.articleJsonStringLiveData.observe(this) { jsonString ->
            viewBinding.tvNormalUseResultContent.text = jsonString
        }
    }
}