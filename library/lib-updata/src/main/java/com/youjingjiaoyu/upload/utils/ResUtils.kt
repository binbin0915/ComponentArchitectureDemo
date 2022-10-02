package com.youjingjiaoyu.upload.utils

import android.app.Application
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
import androidx.core.content.res.ResourcesCompat
import java.util.Locale

object ResUtils {
    private lateinit var mContext: Application
    fun init(context: Application) {
        mContext = context
    }

    private val context: Context
        get() = mContext

    /**
     * 得到Resouce对象
     */
    private val resource: Resources
        get() = context.resources

    /**
     * 得到String.xml中的字符串
     */
    @JvmStatic
    fun getString(@StringRes resId: Int): String {
        return resource.getString(resId)
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    @JvmStatic
    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return resource.getString(id, *formatArgs)
    }

    /**
     * 格式 String 字符版本
     */
    @JvmStatic
    fun getString(format: String, vararg formatArgs: Any): String {
        return String.format(Locale.getDefault(), format, *formatArgs)
    }

    /**
     * array.xml中的字符串数组
     */
    @JvmStatic
    fun getStringArr(@ArrayRes resId: Int): Array<String> {
        return resource.getStringArray(resId)
    }

    /**
     * array.xml中的数组
     */
    @JvmStatic
    fun getIntArr(@ArrayRes resId: Int): IntArray {
        return resource.getIntArray(resId)
    }

    @JvmStatic
    fun getDimension(@DimenRes demenId: Int): Float {
        return resource.getDimension(demenId)
    }

    @JvmStatic
    fun getDimensionPixelOffset(@DimenRes demenId: Int): Int {
        return resource.getDimensionPixelOffset(demenId)
    }

    /**
     * 得到colors.xml中的颜色
     */
    @JvmStatic
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
    @JvmStatic
    fun getDrawable(res: Resources, @DrawableRes resId: Int, theme: Resources.Theme): Drawable? {
        return ResourcesCompat.getDrawable(res, resId, theme)
    }

    @JvmStatic
    val configuration: Configuration
        get() = resource.configuration
}