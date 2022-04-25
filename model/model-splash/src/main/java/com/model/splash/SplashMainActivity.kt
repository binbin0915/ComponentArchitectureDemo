package com.model.splash

import android.animation.Animator
import android.os.Bundle
import com.efs.sdk.launch.LaunchManager
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.model.splash.databinding.SplashActivityMainBinding

class SplashMainActivity : BaseActivity<BaseViewModel, SplashActivityMainBinding>(),
    Animator.AnimatorListener {
    /**
     * 窗口获得焦点
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LaunchManager.onTracePage(this, LaunchManager.PAGE_ON_CREATE, true);
    }

    override fun onRestart() {
        super.onRestart()
        LaunchManager.onTracePage(this, LaunchManager.PAGE_ON_RE_START, true);
    }

    override fun onStart() {
        super.onStart()
        LaunchManager.onTracePage(this, LaunchManager.PAGE_ON_START, true);
    }

    override fun onResume() {
        super.onResume()
        LaunchManager.onTracePage(this, LaunchManager.PAGE_ON_RESUME, false);
    }

    override fun onStop() {
        super.onStop()
        LaunchManager.onTracePage(this, LaunchManager.PAGE_ON_STOP, true);
    }


    override fun initData() {
//        viewBinding.animView.addAnimatorListener(this)
//        viewBinding.animView.playAnimation()
        JumpActivity.jump(this, RouterPath.GROUP_HOME, RouterPath.PAGE_HOME_MAIN_ACTIVITY, true)
    }

    override fun createdObserve() {

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