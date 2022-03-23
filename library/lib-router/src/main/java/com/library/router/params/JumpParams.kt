package com.library.router.params

/**
 * 作用描述：跳转参数
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class JumpParams : HashMap<String, Any>() {
    /**
     * 添加参数
     */
    fun addParams(key: String, value: Any) {
        this[key] = value
    }


}