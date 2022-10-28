package com.model.center.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.model.center.dao.UserDao
import com.model.center.entity.User

/**
 * 创建数据库
 *
 * 注意：如果您的应用在单个进程中运行，在实例化 AppDatabase 对象时应遵循单例设计模式。
 * 每个 RoomDatabase 实例的成本相当高，而您几乎不需要在单个进程中访问多个实例。
 * 如果您的应用在多个进程中运行，请在数据库构建器调用中包含 enableMultiInstanceInvalidation()。
 * 这样，如果您在每个进程中都有一个 AppDatabase 实例，可以在一个进程中使共享数据库文件失效，并且这种失效会自动传播到其他进程中 AppDatabase 的实例。
 *
 * Room的工作方式是app通过Room组件获取到SQLite的Dao实例，通过Dao实例对Entity实例进行GET/SET操作，之后通过Dao对象根据Entity实例的属性值情况对数据库进行变更。
 * @author wangkai
 */
@Database(
    entities = [User::class],//指定表
    version = 1,//版本
    exportSchema = false//版本升级是否记录到本地，暂时为false
)
abstract class UserDataBase : RoomDatabase() {

    //增加抽象方法，Room框架会自动实现这个方法
    abstract fun getUserDao(): UserDao

    companion object {
        private lateinit var instance: UserDataBase

        //初始化，这里要采用单例，不然会有坑（表数据变化时，可能会监听不了）
        fun init(context: Context) {
            if (::instance.isInitialized) {
                return
            }
            create(context)
        }

        private fun create(context: Context) {
            instance = Room.databaseBuilder(context, UserDataBase::class.java, "user_db")
                .allowMainThreadQueries()//可以在主线程执行查询，不建议这么做
                .fallbackToDestructiveMigration()//数据库改变时，强制升级时，不报错
                .build()
        }

        fun getUserDB(): UserDataBase {
            if (!::instance.isInitialized) {
                throw NullPointerException("UserDataBase 未初始化")
            }
            return instance
        }

    }
}