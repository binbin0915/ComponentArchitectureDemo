package com.model.home.pages

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.library.base.expand.ToastType
import com.library.base.view.fragment.BaseFragment
import com.library.base.viewmodel.BaseViewModel
import com.library.widget.status.PageStatus
import com.model.home.viewmodel.HomeMainActivityShareViewModel
import com.model.home.R
import com.model.home.bean.BannerInfo
import com.model.home.databinding.HomeFragmentPage1Binding
import com.library.widget.banner.transformer.ZoomPageTransformer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * lifecycleScope提供了协程支持
 */

class HomeFragment1 : BaseFragment<BaseViewModel, HomeFragmentPage1Binding>(),
    View.OnClickListener {

    private lateinit var sharedViewModel: HomeMainActivityShareViewModel


    override fun lazyInit() {
        //模拟页面第一次加载
        lifecycleScope.launch(Dispatchers.IO) {
            delay(500)
            lifecycleScope.launch(Dispatchers.Main) {
                changePageStatus(PageStatus.STATUS_SUCCEED)
            }
        }
    }

    private val dataList3 = listOf(
        BannerInfo(
            imageUrl = "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni7w0LiIWS5IckQE9TG0t1ftC89uRDmF.vB14O6fOc2FZphzCrtsdqH6GAsbLCpfsG5wov8Ozz7TyS45UyAVf6WI!/b&bo=ngL2AZ4C9gEFFzQ!&rf=viewer_4",
            displayText = "title1"
        ), BannerInfo(
            imageUrl = "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni83Es8qQnOlse*pbVd1M1HZfzu7HzvPNEeBfuKoXEYcKLv1MAEx0HFHwpgoyGSM8VNqmeINMJcNRtyDKTGIKK*0!/b&bo=LAIgAywCIAMFFzQ!&rf=viewer_4",
            displayText = "title2"
        ), BannerInfo(
            imageUrl = "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni0YAeqnprq8Bz*2LVpYQXcbygVY1K8xi7t8fOe6KdEK6V*hj6vlsz2CJbP5obbQKYYelaUfvptiyFC83Y9SAB84!/b&bo=CQI3AQkCNwEFFzQ!&rf=viewer_4",
            displayText = "title3"
        ), BannerInfo(
            imageUrl = "http://photocq.photo.store.qq.com/psc?/V14Rxniv2U0S9D/cXP39dXjFtymXNK2lOGni1tyipuKGV5Qo53j5*PS*1xryaKKaT1QibzItxvf4i*fiw8m9aV86cUhG0SGknOczhjV.TQ6DgyUkyXyhIHFEIY!/b&bo=ngLOAZ4CzgEFFzQ!&rf=viewer_4",
            displayText = "title4"
        )
    )

    override fun initData() {
        sharedViewModel =
            ViewModelProvider(requireActivity())[HomeMainActivityShareViewModel::class.java]
        viewBinding.homeImageview.setOnClickListener(this)

        viewBinding.emptyBtn.setOnClickListener(this)
        viewBinding.emptyRetryBtn.setOnClickListener(this)
        viewBinding.errorBtn.setOnClickListener(this)
        viewBinding.errorRetryBtn.setOnClickListener(this)
        viewBinding.networkBtn.setOnClickListener(this)
        viewBinding.networkRetryBtn.setOnClickListener(this)
        viewBinding.loadingBtn.setOnClickListener(this)


        viewBinding.homeBanner.setData(dataList3,
            dataList3.map { it.displayText }) { itemBinding, data ->
            Glide.with(this).load(data.imageUrl).into(itemBinding.root)
            itemBinding.root.setOnClickListener {
                Toast.makeText(context, "${data.displayText}: 被点击了！", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.homeBanner.setAllowUserScrollable(true)
        viewBinding.homeBanner.setPageTransformer(ZoomPageTransformer())
    }

    override fun createdObserve() {

    }

    override fun defaultLoadingStatus(): Boolean = true

    companion object {
        fun newInstance(): HomeFragment1 {
            val fragment = HomeFragment1()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onClick(view: View) {
        when (view.id) {
            R.id.emptyBtn -> changePageStatus(PageStatus.STATUS_EMPTY)
            R.id.emptyRetryBtn -> changePageStatus(PageStatus.STATUS_EMPTY_RETRY)
            R.id.errorBtn -> changePageStatus(PageStatus.STATUS_ERROR)
            R.id.errorRetryBtn -> changePageStatus(PageStatus.STATUS_ERROR_RETRY)
            R.id.networkBtn -> changePageStatus(PageStatus.STATUS_NET_ERROR)
            R.id.networkRetryBtn -> changePageStatus(PageStatus.STATUS_NET_ERROR_RETRY)
            R.id.loadingBtn -> changePageStatus(PageStatus.STATUS_LOADING)
            R.id.home_imageview -> {
                sharedViewModel.isClick.value = true
            }
        }
        showSucceedStatus()
    }

    /**
     * 点击重试会调用
     */
    override fun onRetry() {
        //requireContext()
        showToast(ToastType.INFO, "重试监听")
    }

    private fun showSucceedStatus() {
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            lifecycleScope.launch(Dispatchers.Main) {
                changePageStatus(PageStatus.STATUS_SUCCEED)
            }
        }
    }
}