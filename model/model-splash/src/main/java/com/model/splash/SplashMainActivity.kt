package com.model.splash

import android.animation.Animator
import android.os.Bundle
import android.view.View
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.model.splash.databinding.SplashActivityMainBinding

class SplashMainActivity : BaseActivity<BaseViewModel, SplashActivityMainBinding>(),
    Animator.AnimatorListener {
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }


    override fun initData() {
        viewBinding.animView.addAnimatorListener(this)
        viewBinding.animView.playAnimation()
    }

    override fun createdObserve() {

    }

    override fun onAnimationStart(p0: Animator?) {

    }

    override fun onAnimationEnd(p0: Animator?) {
        JumpActivity.jump(this, RouterPath.GROUP_HOME, RouterPath.PAGE_HOME_MAIN_ACTIVITY, true)
    }

    override fun onAnimationCancel(p0: Animator?) {

    }

    override fun onAnimationRepeat(p0: Animator?) {

    }
}