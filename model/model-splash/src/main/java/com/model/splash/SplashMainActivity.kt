package com.model.splash

import android.animation.Animator
import com.library.base.expand.StatusBarStyle
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.model.splash.databinding.SplashActivityMainBinding

class SplashMainActivity : BaseActivity<BaseViewModel, SplashActivityMainBinding>(),
    Animator.AnimatorListener {
    /**
     * 全屏显示
     */
    override fun statusBarStyle(): StatusBarStyle {
        return StatusBarStyle.IMMERSION
    }

    override fun initData() {

    }

    override fun createdObserve() {
        viewBinding.animView.addAnimatorListener(this)
        viewBinding.animView.playAnimation()
    }

    /**
     * 动画开始
     */
    override fun onAnimationStart(animator: Animator) {

    }

    /**
     * 动画结束
     */
    override fun onAnimationEnd(animator: Animator) {
        JumpActivity.jump(this, RouterPath.GROUP_HOME, RouterPath.PAGE_HOME_MAIN_ACTIVITY, true)
    }

    /**
     * 动画取消
     */
    override fun onAnimationCancel(animator: Animator) {

    }

    /**
     * 动画重复播放
     */
    override fun onAnimationRepeat(animator: Animator) {

    }
}