package com.library.base.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.alibaba.android.arouter.launcher.ARouter
import com.library.base.expand.ToastType
import com.library.base.expand.*
import com.library.base.expand.toast
import com.library.base.utils.*
import com.library.base.viewmodel.BaseViewModel
import com.library.base.viewmodel.EventType
import com.library.widget.status.MultiStateContainer
import com.library.widget.status.PageStatus
import com.library.widget.status.bindMultiState

/**
 * 作用描述：Fragment 基类  MVVM架构 使用ViewBind查找控件
 * Androidx 下的Fragment 新增了setMaxLifecycle 所以一般情况下以及使用ViewPager2 能够正确的回调 onResume
 * 对于使用add+show+hide的方式 请使用ActivityExpand.kt 里面的loadFragmentsTransaction 和 showHideFragmentTransaction
 *
 * 创建时间：2022/03/17
 * @author：WangKai
 */
abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {
    /**
     * 是否已加载数据
     */
    private var isLoadData = false

    /**
     * ViewModel
     */
    lateinit var viewModel: VM

    /**
     * ViewBinding
     */
    lateinit var viewBinding: VB


    /**
     * 页面状态显示View
     */
    private lateinit var pageStatus: MultiStateContainer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflateBindingWithGeneric(layoutInflater, container, false)
        pageStatus = viewBinding.root.bindMultiState {
            //重试处理
            onRetry()
        }
        if (defaultLoadingStatus()) {
            pageStatus.changePageStatus(PageStatus.STATUS_LOADING)
        }
        viewModel = createViewModel()
        handlerViewModelNotice()
        //初始化路由
        ARouter.getInstance().inject(this)
        createdObserve()
        return pageStatus
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }


    override fun onResume() {
        super.onResume()
        if (!isLoadData && !isHidden) {
            lazyInit()
            isLoadData = true
        } else {
            onVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        onInvisible()
    }

    /**
     * 懒加载数据
     */
    abstract fun lazyInit()

    /**
     * 初始化数据
     */
    abstract fun initData()

    /**
     * 创建订阅
     */
    abstract fun createdObserve()

    /**
     * 界面可见时回调
     */
    open fun onVisible() {

    }

    /**
     * 界面不可见时回调
     */
    open fun onInvisible() {

    }

    /**
     * 页面重试回调
     */
    open fun onRetry() {

    }

    /**
     * 默认是否是加载状态
     */
    open fun defaultLoadingStatus(): Boolean {
        return false
    }

    /**
     * 获取协程作用范围
     */
    fun getLifecycleScope() = viewLifecycleOwner.lifecycleScope

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[getVmClazz(this)]
    }


    /**
     * 处理ViewModel的通知
     */
    private fun handlerViewModelNotice() {
        viewModel.eventNoticeData.observe(this) {
            it?.let {
                when (it.type) {
                    EventType.EVENT_TOAST -> {
                        //显示Toast
                        if (isAdded) {
                            showToast(it.toastType, it.desc)
                        }
                    }
                    EventType.EVENT_DIALOG -> {
                        showDialog(it.title, it.desc)
                    }
                    EventType.EVENT_SHOW_LOADING_DIALOG -> {
                        showLoading(it.desc)
                    }
                    EventType.EVENT_DISMISS_LOADING_DIALOG -> {
                        dismissLoading()
                    }
                    EventType.EVENT_CHANGE_PAGE_STATUS -> {
                        //改变页面状态
                        changePageStatus(it.pageStatus)
                    }
                    else -> {
                        //无需处理
                    }
                }
            }
        }
    }

    /**
     * 改变页面状态
     */
    private fun changePageStatus(status: PageStatus) {
        pageStatus.changePageStatus(status)
    }

    /**
     * 显示Toast
     */
    private fun showToast(type: ToastType = ToastType.INFO, msg: String) {
        requireActivity().toast(type, msg)
    }

    /**
     * 显示加载框
     */
    private fun showLoading(msg: String) {

    }

    /**
     * 关闭加载框
     */
    private fun dismissLoading() {

    }

    /**
     * 显示dialog
     */
    private fun showDialog(title: String = "", msg: String = "") {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        isLoadData = false
    }
}