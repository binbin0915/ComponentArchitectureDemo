package com.wangkai.myapplication.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.library.logcat.Logcat

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
object RoomDBUtils {
    private lateinit var dataBase: AppRoomDatabase
    private const val DB_NAME = "test_info"
    fun init(context: Context) {
        dataBase = Room.databaseBuilder(context, AppRoomDatabase::class.java, DB_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Logcat.log("Room database onCreate in thread " + Thread.currentThread().name)
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Logcat.log("Room database onOpen in thread " + Thread.currentThread().name)
                }
            })
            .allowMainThreadQueries()//可以在主线程执行查询，不建议这么做
            .fallbackToDestructiveMigration()//数据库改变时，强制升级时，不报错
            .build()
    }


    //SYNCHRONIZED同步：只会调用一次初始化方法。单例模式：懒汉式，线程安全
    //PUBLICATION：会调用多次初始化方法，但只有第一次的有效。
    //NONE：会调用多次，且会改变常量的值为最后一次的值。单例模式：懒汉式，线程不安全
    //初始化，这里要采用单例，不然会有坑（表数据变化时，可能会监听不了）
    private val db: AppRoomDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        dataBase
    }


    fun getDB(): AppRoomDatabase {
        return if (::dataBase.isInitialized) {
            db
        } else {
            throw ExceptionInInitializerError("RoomDbUtils未初始化")
        }
    }


}