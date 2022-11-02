package com.library.base.database

import android.content.Context
import android.content.SharedPreferences

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
object SPUtils {
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
    fun putApply(context: Context, key: String, any: Any) {
        val sp = context.getSharedPreferences(
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
    fun putCommit(context: Context, key: String, any: Any): Boolean {
        val sp = context.getSharedPreferences(
            FILE_NAME, MODE
        )
        val editor = sp.edit()
        judgePutDataType(key, any, editor)
        return editor.commit()
    }

    /**
     * 提交到其他单独的文件
     * @param editor
     * @param key
     * @param any
     * @return
     */
    fun putAdd(editor: SharedPreferences.Editor, key: String, any: Any) {
        judgePutDataType(key, any, editor)
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

            else -> {
                editor.putString(key, any.toString())
            }
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultAny
     * @return
     */
    operator fun get(context: Context, key: String, defaultAny: Any): Any? {
        val sp = context.getSharedPreferences(
            FILE_NAME, MODE
        )
        return when (defaultAny) {
            is String -> {
                sp.getString(key, defaultAny)
            }

            is Int -> {
                sp.getInt(key, defaultAny)
            }

            is Boolean -> {
                sp.getBoolean(key, (defaultAny))
            }

            is Float -> {
                sp.getFloat(key, (defaultAny))
            }

            is Long -> {
                sp.getLong(key, (defaultAny))
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
    fun remove(context: Context, key: String) {
        val sp = context.getSharedPreferences(
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
    fun contains(context: Context, key: String): Boolean {
        val sp = context.getSharedPreferences(
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