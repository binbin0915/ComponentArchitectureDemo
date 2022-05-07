package com.model.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.model.home.pages.HomeFragment1
import com.model.home.pages.HomeFragment2
import com.model.home.pages.HomeFragment3
import com.model.home.pages.HomeFragment4

/**
 * 作用描述：容纳首页fragment的ViewPager
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class HomeViewPagerAdapter(activity: FragmentActivity, private val itemCount: Int) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return itemCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment1.newInstance()
            1 -> HomeFragment2.newInstance()
            2 -> HomeFragment3.newInstance()
            3 -> HomeFragment4.newInstance()
            else -> HomeFragment1.newInstance()
        }
    }
}