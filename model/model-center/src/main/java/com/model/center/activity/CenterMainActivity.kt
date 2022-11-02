package com.model.center.activity

import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.library.base.view.activity.BaseActivity
import com.library.base.viewmodel.BaseViewModel
import com.library.logcat.Logcat
import com.library.router.RouterPath
import com.model.center.database.CenterModuleDatabase
import com.model.center.databinding.CenterActivityCenterMainBinding
import com.model.center.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Route(path = RouterPath.PAGE_CENTER_MAIN_ACTIVITY, group = RouterPath.GROUP_CENTER)
class CenterMainActivity : BaseActivity<BaseViewModel, CenterActivityCenterMainBinding>() {
    override fun initData() {

    }

    override fun createdObserve() {
        //insert,插入三个数据
        val user1 = User("小明", 12)
        val user2 = User("小红", 18)
        val user3 = User("小清", 20)
        CenterModuleDatabase.getUserDao().insertUsers(user1, user2, user3)
        //删除id为3的User
        CenterModuleDatabase.getUserDao().deleteUser(User(3))
        //修改，将第一个数据修改名称与age
        CenterModuleDatabase.getUserDao().updateUser(User(1, "大明明3333", 15))
        //查询
        flow {//用Flow查询
            emit(CenterModuleDatabase.getUserDao().getAllUser())
        }.flowOn(Dispatchers.IO)
            .onEach {//数据更新
                Logcat.log(it.toString())
            }.launchIn(lifecycleScope)
    }
}