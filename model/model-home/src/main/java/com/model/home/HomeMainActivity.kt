package com.model.home

import android.graphics.Color
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.expand.StatusBarStyle
import com.library.base.view.activity.BaseActivity
import com.library.router.RouterPath
import com.model.home.adapter.HomeViewPagerAdapter
import com.model.home.databinding.HomeActivityMainBinding
import com.umeng.analytics.MobclickAgent

@Route(path = RouterPath.PAGE_HOME_MAIN_ACTIVITY, group = RouterPath.GROUP_HOME)
class HomeMainActivity : BaseActivity<HomeMainActivityViewModel, HomeActivityMainBinding>() {

    private lateinit var adapter: HomeViewPagerAdapter

    private lateinit var sharedViewModel: HomeMainActivityShareViewModel

    override fun createdObserve() {
        viewModel.pageData.observe(this) {}
    }

    /**
     * 布局侵入状态栏
     */
    override fun statusBarStyle(): StatusBarStyle {
        return StatusBarStyle.ONLY_STATUS
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //点击back退出时调用，用来保存友盟统计数据
        MobclickAgent.onKillProcess(applicationContext)
    }

    override fun initData() {
        adapter = HomeViewPagerAdapter(this, 4)
        viewBinding.viewPager.adapter = adapter

        sharedViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[HomeMainActivityShareViewModel::class.java]

        viewBinding.layoutDrawer.setScrimColor(Color.TRANSPARENT)
        sharedViewModel.isClick.observe(this) {
            //设为打开状态
            sharedViewModel.isOpen.value = true
            viewBinding.layoutDrawer.openDrawer(GravityCompat.START)
        }

        /**
         * layoutDrawer监听
         */
        val drawerListener = object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

                if (sharedViewModel.isOpen.value == true) {
                    //打开状态，马上要关闭
                    sharedViewModel.isOpen.value = false
                    viewBinding.motionLayout.transitionToState(R.id.end)
//                    if (viewBinding.layoutDrawer.isOpen) {
//                        viewBinding.motionLayout.transitionToState(R.id.end)
//                    } else {
//                        viewBinding.motionLayout.transitionToState(R.id.start)
//                    }
                }


            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {
//                if (newState == DrawerLayout.STATE_IDLE) {
//                    if (viewBinding.layoutDrawer.isOpen) {
//                        viewBinding.motionLayout.transitionToState(R.id.end)
//                    } else {
//                        viewBinding.motionLayout.transitionToState(R.id.start)
//                    }
//                } else if (newState == DrawerLayout.STATE_SETTLING) {
//                    if (viewBinding.layoutDrawer.isOpen) {
//                        viewBinding.motionLayout.transitionToState(R.id.end)
//                    } else {
//                        viewBinding.motionLayout.transitionToState(R.id.start)
//                    }
//                }
            }
        }
        viewBinding.layoutDrawer.addDrawerListener(drawerListener)


        //获取网络数据
        viewModel.getPageData()


        //1.注册viewpager页面滑动回调 -- 抽象类的使用
        viewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> viewBinding.bottomBar.selectedItemId = R.id.menu1
                    1 -> viewBinding.bottomBar.selectedItemId = R.id.menu2
                    2 -> viewBinding.bottomBar.selectedItemId = R.id.menu3
                    3 -> viewBinding.bottomBar.selectedItemId = R.id.menu4
                }
            }
        })
        //2.注册BottomNavigationView点击监听 -- kotlin接口的使用
        viewBinding.bottomBar.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu1 -> {
                    viewBinding.viewPager.currentItem = 0
                    true
                }
                R.id.menu2 -> {
                    viewBinding.viewPager.currentItem = 1
                    true
                }
                R.id.menu3 -> {
                    viewBinding.viewPager.currentItem = 2
                    true
                }
                R.id.menu4 -> {
                    viewBinding.viewPager.currentItem = 3
                    true
                }
                else -> false
            }
        }
    }
}