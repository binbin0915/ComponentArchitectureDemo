package com.library.base.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Activity在onCreate中使用
 */
@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Activity.inflateBindingWithGeneric(layoutInflater: LayoutInflater): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

/**
 * Fragment在onCreateView使用
 */
@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> Fragment.inflateBindingWithGeneric(
    layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean
): VB = withGenericBindingClass(this) { clazz ->
    clazz.getMethod(
        "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
    ).invoke(null, layoutInflater, parent, attachToParent) as VB
}

/**
 * 在自定义ViewGroup（包括Recyclerview）中使用
 */
@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> ViewGroup.inflateBindingWithGeneric(parent: ViewGroup): VB =
    withGenericBindingClass(this) { clazz ->
        clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        ).invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    }


/**
 * 在自定义View中使用
 */
@JvmName("inflateWithGeneric")
@Suppress("UNCHECKED_CAST")
fun <VB : ViewBinding> View.bindViewWithGeneric(view: View): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod("bind", View::class.java).invoke(null, view) as VB
    }

/**
 * 获取ViewBinding
 */
@Suppress("UNCHECKED_CAST")
private fun <VB : ViewBinding> withGenericBindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    any.allParameterizedType.forEach { parameterizedType ->
        parameterizedType.actualTypeArguments.forEach {
            try {
                return block.invoke(it as Class<VB>)
            } catch (_: NoSuchMethodException) {
            } catch (_: ClassCastException) {
            }
        }
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}

/**
 * ParameterizedType：参数化类型
 * getGenericSuperclass()：获得带有泛型的父类
 * Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
 */
private val Any.allParameterizedType: List<ParameterizedType>
    get() {
        val genericParameterizedType = mutableListOf<ParameterizedType>()
        var genericSuperclass = javaClass.genericSuperclass
        var superclass = javaClass.superclass
        while (superclass != null) {
            if (genericSuperclass is ParameterizedType) {
                genericParameterizedType.add(genericSuperclass)
            }
            genericSuperclass = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        return genericParameterizedType
    }