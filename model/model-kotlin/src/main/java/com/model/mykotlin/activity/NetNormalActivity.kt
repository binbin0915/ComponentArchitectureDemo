package com.model.mykotlin.activity

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.common.netconfig.tools.download.Bean
import com.library.router.RouterPath
import com.model.mykotlin.data.remote.RemoteDataSource
import com.model.mykotlin.databinding.MykotlinActivityNetNormalBinding
import com.model.mykotlin.viewmodel.NetNormalViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Route(path = RouterPath.PAGE_NET_NORMAL_ACTIVITY, group = RouterPath.GROUP_KOTLIN)
class NetNormalActivity : BaseActivity<NetNormalViewModel, MykotlinActivityNetNormalBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.btnNormalUseRxjava3.setOnClickListener {
            viewModel.queryWanAndroidArticlesByRxJava3()
        }

        viewBinding.btnNormalUseCoroutine.setOnClickListener {
//            viewModel.queryWanAndroidArticleByCoroutine()
            MainScope().launch{
                RemoteDataSource.download(context = applicationContext, Bean("https://ceshiaidiandu.oss-cn-beijing.aliyuncs.com//storage/ceshi_uploads/androidapk/202108/beisuketang_202207200851.apk"))

//            //TODO 仅作测试，后续使用flow优化
//            try {
//                val responseEntity = RemoteDataSource.queryWanAndroidArticleByCoroutine(0)
//                articleJsonStringLiveData.value = "query WanAndroid Article from Coroutine \n $responseEntity"
//            }catch (e : Exception){
//                GlobalHttpResponseProcessor.handleHttpError(e)
//                articleJsonStringLiveData.value = "query WanAndroid Article from Coroutine $e"
//            }
            }
        }

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