package com.library.base.expand

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.zackratos.ultimatebarx.ultimatebarx.UltimateBarX

/**
 * 作用描述：状态栏管理
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */

fun FragmentActivity.defaultActivityBar() {//Activity默认处理
    UltimateBarX.with(this).transparent().light(true).fitWindow(true).applyStatusBar()
    UltimateBarX.with(this).transparent().light(true).fitWindow(false).applyNavigationBar()
}

/**
 * 沉浸模式
 */
fun FragmentActivity.immersionActivityBar() {
    UltimateBarX.with(this).transparent().light(true).fitWindow(false).applyStatusBar()
    UltimateBarX.with(this).transparent().light(true).fitWindow(false).applyNavigationBar()
}


/**
 * 改变状态栏颜色
 * 透明状态栏
 */
fun FragmentActivity.transparentBar() {
    UltimateBarX.with(this).transparent().applyStatusBar()
}

/**
 * 改变状态栏颜色
 * @param color 状态栏颜色
 * @param fontIsDark 状态栏字体是否是深色
 */
fun FragmentActivity.changeStatusBarColor(color: Int, fontIsDark: Boolean) {
    UltimateBarX.with(this).colorRes(color).light(!fontIsDark).applyStatusBar()
}


/**
 * 改变状态栏样式
 */
fun FragmentActivity.setBarStyle(style: StatusBarStyle) {
    when (style) {
        StatusBarStyle.DEFAULT -> defaultActivityBar()
        StatusBarStyle.IMMERSION -> immersionActivityBar()
    }
}


enum class StatusBarStyle {
    /**
     * 默认
     */
    DEFAULT,

    /**
     * 沉浸式
     */
    IMMERSION
}

