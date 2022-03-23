package com.library.widget.status

import android.content.Context
import android.view.LayoutInflater
import android.view.View

/**
 * 作用描述：多状态页
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
abstract class MultiState {

    /**
     * 创建stateView
     */
    abstract fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View

    /**
     * stateView创建完成
     */
    abstract fun onMultiStateViewCreate(view: View)

    /**
     * 是否允许重新加载 点击事件
     * 默认false 不允许
     */
    open fun enableReload(): Boolean = false

    /**
     * 绑定重试view
     * 默认null为整个state view
     */
    open fun bindRetryView(): View? = null

}