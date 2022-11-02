package com.model.center.database

import com.model.center.dao.UserDao

object CenterModuleDatabase {
    lateinit var onGetDaoCallback: OnGetDaoCallback

    internal fun getUserDao(): UserDao {
        return if (::onGetDaoCallback.isInitialized) {
            onGetDaoCallback.onGetUserDao()
        } else {
            throw IllegalArgumentException("需要在MainApplication中初始化CenterModuleDatabase!!")
        }
    }

    interface OnGetDaoCallback {
        fun onGetUserDao(): UserDao
    }
}