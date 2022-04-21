package com.library.base.expand

import java.lang.reflect.ParameterizedType

/**
 * 作用描述：公共扩展方法
 *
 * * getVmClazz:获取当前类绑定的泛型ViewModel-clazz
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */


fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}