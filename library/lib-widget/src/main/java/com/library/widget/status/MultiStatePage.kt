package com.library.widget.status

import android.app.Activity
import android.view.View
import android.view.ViewGroup

/**
 * 作用描述：多状态页
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
object MultiStatePage {

    /**
     * 替换目标view
     *
     * 实现原理：
     * 1. 根据目标view在父view中的位置索引,移除原目标view,
     * 2. 将MultiStateContainer添加到原view的索引处
     * 3. MultiStateContainer 的 layoutParams 是原目标View的 layoutParams
     */
    @JvmStatic
    @JvmOverloads
    fun bindMultiState(
        targetView: View,
        onRetryEventListener: OnRetryEventListener? = null
    ): MultiStateContainer {
        val parent = targetView.parent as ViewGroup?
        var targetViewIndex = 0
        val multiStateContainer =
            MultiStateContainer(targetView.context, targetView, onRetryEventListener)
        parent?.let { targetViewParent ->
            for (i in 0 until targetViewParent.childCount) {
                if (targetViewParent.getChildAt(i) == targetView) {
                    targetViewIndex = i
                    break
                }
            }
            targetViewParent.removeView(targetView)
            targetViewParent.addView(multiStateContainer, targetViewIndex, targetView.layoutParams)
        }
        multiStateContainer.initialization()
        return multiStateContainer
    }

    /**
     * 在Activity上添加
     *
     * 实现原理：
     * 1. android.R.id.content 是Activity setContentView 内容的父view
     * 2. 在这个view中移除原本要添加的contentView
     * 3. 将MultiStateContainer设置为 content的子View  MultiStateContainer中持有原有的Activity setContentView
     */
    @JvmStatic
    @JvmOverloads
    fun bindMultiState(
        activity: Activity,
        onRetryEventListener: OnRetryEventListener? = null
    ): MultiStateContainer {
        val targetView = activity.findViewById<ViewGroup>(android.R.id.content)
        val targetViewIndex = 0
        val oldContent: View = targetView.getChildAt(targetViewIndex)
        targetView.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val multiStateContainer =
            MultiStateContainer(oldContent.context, oldContent, onRetryEventListener)
        targetView.addView(multiStateContainer, targetViewIndex, oldLayoutParams)
        multiStateContainer.initialization()
        return multiStateContainer
    }


    var config: MultiStateConfig = MultiStateConfig()

    @JvmStatic
    fun config(config: MultiStateConfig): MultiStatePage {
        MultiStatePage.config = config
        return this
    }

}