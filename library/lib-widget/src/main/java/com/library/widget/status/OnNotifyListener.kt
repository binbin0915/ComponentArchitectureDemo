package com.library.widget.status

/**
 * 作用描述：刷新监听
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
fun interface OnNotifyListener<T : MultiState> {
    fun onNotify(multiState: T)
}