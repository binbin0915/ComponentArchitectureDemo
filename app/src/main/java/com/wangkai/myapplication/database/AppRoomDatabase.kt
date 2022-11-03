package com.wangkai.myapplication.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.model.center.dao.UserDao
import com.model.center.entity.User

@Database(
    entities = [
        User::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}