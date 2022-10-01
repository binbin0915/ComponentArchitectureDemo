package com.youjingjiaoyu.upload.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.util.Locale

object ResUtils {
    private var mContext: Context? = null
    fun init(context: Context?) {
        mContext = context
    }

    private val context: Context
        private get() = mContext!!

    /**
     * 得到Resouce对象
     */
    val resource: Resources
        get() = context.resources

    /**
     * 得到String.xml中的字符串
     */
    fun getString(@StringRes resId: Int): String {
        return resource.getString(resId)
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return resource.getString(id, *formatArgs)
    }

    /**
     * 格式 String 字符版本
     */
    fun getString(format: String?, vararg formatArgs: Any?): String {
        return String.format(Locale.getDefault(), format!!, *formatArgs)
    }

    /**
     * array.xml中的字符串数组
     */
    fun getStringArr(@ArrayRes resId: Int): Array<String> {
        return resource.getStringArray(resId)
    }

    /**
     * array.xml中的数组
     */
    fun getIntArr(@ArrayRes resId: Int): IntArray {
        return resource.getIntArray(resId)
    }

    fun getDimension(@DimenRes demenId: Int): Float {
        return resource.getDimension(demenId)
    }

    fun getDimensionPixelOffset(@DimenRes demenId: Int): Int {
        return resource.getDimensionPixelOffset(demenId)
    }

    /**
     * 得到colors.xml中的颜色
     */
    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    /**
     * 获取 drawable
     *
     * @param resId 资源 id
     * @return Drawable
     */
    fun getDrawable(@DrawableRes resId: Int): Drawable {
        return resource.getDrawable(resId)
    }

    val configuration: Configuration
        get() = resource.configuration
}