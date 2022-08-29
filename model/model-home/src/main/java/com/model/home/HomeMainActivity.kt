package com.model.home

import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.expand.StatusBarStyle
import com.library.base.view.activity.BaseActivity
import com.library.router.JumpActivity
import com.library.router.RouterPath
import com.library.widget.banner.ext.dp
import com.library.widget.drawer.DrawerLayout
import com.model.home.databinding.HomeActivityMainBinding
import com.model.home.pages.adapter.HomeViewPagerAdapter
import com.model.home.viewmodel.HomeMainActivityShareViewModel
import com.model.home.viewmodel.HomeMainActivityViewModel

@Route(path = RouterPath.PAGE_HOME_MAIN_ACTIVITY, group = RouterPath.GROUP_HOME)
class HomeMainActivity : BaseActivity<HomeMainActivityViewModel, HomeActivityMainBinding>() {

    private lateinit var adapter: HomeViewPagerAdapter

    /**
     * activity和fragment共享的ViewModel
     */
    private val sharedViewModel by viewModels<HomeMainActivityShareViewModel>()

    override fun createdObserve() {
        viewModel.pageData.observe(this) {}


        viewBinding.tvPersonalCenter.setOnClickListener {
            //关闭侧边
            viewBinding.layoutDrawer.closeDrawer(GravityCompat.START)
            //跳转到登录页
            JumpActivity.jump(RouterPath.GROUP_LOGIN, RouterPath.PAGE_LOGIN_ACTIVITY)
        }
    }

    /**
     * 布局侵入状态栏
     */
    override fun statusBarStyle(): StatusBarStyle {
        return StatusBarStyle.ONLY_STATUS
    }

    /**
     * ### bug：后台恢复后motionLayout被还原
     * fix：为了保证后台长时间驻留app被杀死导致motionLayout被还原，
     * 目前的解决办法只能牺牲体验，当app回到首页，重置layoutDrawer状态
     */
    override fun onResume() {
        super.onResume()
        if (viewBinding.layoutDrawer.isOpen) {
            viewBinding.layoutDrawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun initData() {
        /**
         * 侧边菜单动画
         */
        viewBinding.layoutDrawer.setScrimColor(Color.TRANSPARENT)
        sharedViewModel.isClick.observe(this) {
            viewBinding.layoutDrawer.openDrawer(GravityCompat.START)
        }

        /**
         * fragment页面
         */
        adapter = HomeViewPagerAdapter(this, 4)
        viewBinding.viewPager.adapter = adapter

        /**
         * layoutDrawer监听
         */
        viewBinding.layoutDrawer.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                viewBinding.motionLayout.progress = slideOffset
                viewBinding.cardView1.radius = slideOffset * 18.dp.toInt()
                viewBinding.cardView2.radius = slideOffset * 18.dp.toInt()
                viewBinding.cardView3.radius = slideOffset * 18.dp.toInt()
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })


        //获取网络数据
        //viewModel.getPageData()


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