package com.library.base.expand

import androidx.fragment.app.FragmentActivity
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * 作用描述：状态栏管理
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */

enum class StatusBarStyle {
    /**
     * 默认
     */
    DEFAULT,

    /**
     * 沉浸式
     */
    IMMERSION,

    /**
     * statusBar
     */
    ONLY_STATUS
}

/**
 * 默认模式：仅状态栏透明
 * 不侵入
 */
fun FragmentActivity.defaultActivityBar(fontIsDark: Boolean) {
    statusBarOnly {
        //只适配状态栏的文字颜色
        light = fontIsDark        // 沉浸式
        fitWindow = true    // 布局不侵入
    }
}

/**
 * 默认模式：仅状态栏透明且侵入
 */

fun FragmentActivity.onlyStatusActivityBar(fontIsDark: Boolean) {
    statusBarOnly {
        //只适配状态栏的文字颜色
        light = fontIsDark
        transparent()
    }
}

/**
 * 沉浸模式：状态栏和导航栏透明
 */
fun FragmentActivity.immersionActivityBar(fontIsDark: Boolean) {
    statusBar {
        light = fontIsDark
        transparent()
    }
    navigationBar { transparent() }
}

/**
 * 改变状态栏样式
 */
fun FragmentActivity.setBarStyle(style: StatusBarStyle, fontIsDark: Boolean) {
    when (style) {
        StatusBarStyle.DEFAULT -> defaultActivityBar(fontIsDark)
        StatusBarStyle.IMMERSION -> immersionActivityBar(fontIsDark)
        StatusBarStyle.ONLY_STATUS -> onlyStatusActivityBar(fontIsDark)
    }
}

