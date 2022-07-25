package com.library.common.commonutils

import android.content.Context
import android.content.SharedPreferences
import com.library.base.application.BaseApplication

/**
 * SharedPreferences工具类
 *
 * 支持同步和异步的提交方法
 * * putCommit
 * * putApply
 *
 * 支持的数据类型：
 * * String
 * * Int
 * * Boolean
 * * Float
 * * Long
 * * Set<String>
 */


@Suppress("unused", "UNCHECKED_CAST")
class SPUtils {
    companion object {
        /**
         * 保存在手机里面的文件名 和读写模式
         */
        private const val FILE_NAME = "share_data"
        private const val MODE = Context.MODE_PRIVATE

        /**
         * 异步提交方法
         * @param context
         * @param key
         * @param any
         */
        fun putApply(key: String, any: Any) {
            val sp = BaseApplication.appContext.getSharedPreferences(
                FILE_NAME, MODE
            )
            val editor = sp.edit()
            judgePutDataType(key, any, editor)
            editor.apply()
        }

        /**
         * 同步提交方法
         * @param context
         * @param key
         * @param any
         * @return
         */
        fun putCommit(key: String, any: Any): Boolean {
            val sp = BaseApplication.appContext.getSharedPreferences(
                FILE_NAME, MODE
            )
            val editor = sp.edit()
            judgePutDataType(key, any, editor)
            return editor.commit()
        }

        /**
         * 根据不同类型 使用不同的写入方法
         * @param key
         * @param any
         * @param editor
         */
        private fun judgePutDataType(key: String, any: Any, editor: SharedPreferences.Editor) {
            when (any) {
                is String -> {
                    editor.putString(key, any)
                }
                is Int -> {
                    editor.putInt(key, any)
                }
                is Boolean -> {
                    editor.putBoolean(key, any)
                }
                is Float -> {
                    editor.putFloat(key, any)
                }
                is Long -> {
                    editor.putLong(key, any)
                }
                is Set<*> -> {
                    editor.putStringSet(key, any as Set<String?>)
                }
                else -> {
                    editor.putString(key, any.toString())
                }
            }
        }

        fun putAdd(editor: SharedPreferences.Editor, key: String, any: Any) {
            judgePutDataType(key, any, editor)
        }

        /**
         * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
         *
         * @param context
         * @param key
         * @param defaultAny
         * @return
         */
        operator fun get(key: String?, defaultAny: Any?): Any? {
            val sp = BaseApplication.appContext.getSharedPreferences(
                FILE_NAME, MODE
            )
            return when (defaultAny) {
                is String -> {
                    sp.getString(key, defaultAny as String?)
                }
                is Int -> {
                    sp.getInt(key, (defaultAny as Int?)!!)
                }
                is Boolean -> {
                    sp.getBoolean(key, (defaultAny as Boolean?)!!)
                }
                is Float -> {
                    sp.getFloat(key, (defaultAny as Float?)!!)
                }
                is Long -> {
                    sp.getLong(key, (defaultAny as Long?)!!)
                }
                is Set<*> -> {
                    sp.getStringSet(key, defaultAny as Set<String?>?)
                }
                else -> null
            }
        }

        /**
         * 移除某个key值已经对应的值
         *
         * @param context
         * @param key
         */
        fun remove(key: String?) {
            val sp = BaseApplication.appContext.getSharedPreferences(
                FILE_NAME, MODE
            )
            val editor = sp.edit()
            editor.remove(key)
            editor.apply()
        }

        /**
         * 清除所有数据
         *
         * @param context
         */
        fun clear(context: Context) {
            val sp = context.getSharedPreferences(
                FILE_NAME, MODE
            )
            val editor = sp.edit()
            editor.clear()
            editor.apply()
        }

        /**
         * 查询某个key是否已经存在
         *
         * @param context
         * @param key
         * @return
         */
        fun contains(key: String?): Boolean {
            val sp = BaseApplication.appContext.getSharedPreferences(
                FILE_NAME, MODE
            )
            return sp.contains(key)
        }

        /**
         * 返回所有的键值对
         *
         * @param context
         * @return
         */
        fun getAll(context: Context): Map<String, *> {
            val sp = context.getSharedPreferences(
                FILE_NAME, MODE
            )
            return sp.all
        }
    }
}