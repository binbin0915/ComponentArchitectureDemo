package com.library.network.dsl

import com.library.network.data.NetworkData

/**
 * 网络请求的DSL
 *
 * 什么是DSL?
 * * DSL（domain specific language），即领域专用语言：专门解决某一特定问题的计算机语言，比如大家耳熟能详的 SQL 和正则表达式。比如 SQL 仅支持数据库的相关处理，而正则表达式只用来检索和替换文本，我们无法用 SQL 或者正则表达式来开发一个完整的应用。
 * kotlin DSL
 * * 使用通用编程语言 kotlin 来构建 DSL
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class NetworkRequestDsl<T> {

    var api: (suspend () -> NetworkData<T>)? = null
    var startNumber: Int = 0
    var openLoadStatus = false

    /**
     * set方法私有
     */

    internal var onLoading: (() -> Unit)? = null
        private set
    internal var onBeforeHandler: (suspend (T) -> Unit)? = null
        private set
    internal var onSuccess: (suspend (T) -> Unit)? = null
        private set
    internal var onSuccessEmpty: (() -> Unit)? = null
        private set
    internal var onSuccessEmptyData: (suspend (T?) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set
    internal var onFailed: ((error: String?, code: Int?) -> Unit)? = null
        private set
    internal var onHideLoading: (() -> Unit)? = null
        private set


    /**
     * 基础数据
     */
    var totalRecord: Int = 0
    var baseTime: Long = 0
    var message: String? = ""


    internal fun clean() {
        onSuccess = null
        onComplete = null
        onFailed = null
        onLoading = null
        onSuccessEmpty = null
        onHideLoading = null
    }

    fun onLoading(block: () -> Unit) {
        this.onLoading = block
    }

    fun onHideLoading(block: () -> Unit) {
        this.onHideLoading = block
    }

    fun onBeforeHandler(block: suspend (T) -> Unit) {
        this.onBeforeHandler = block
    }

    fun onSuccess(block: suspend (T) -> Unit) {
        this.onSuccess = block
    }

    fun onSuccessEmpty(block: () -> Unit) {
        this.onSuccessEmpty = block
    }

    fun onSuccessEmptyData(block: suspend (T?) -> Unit) {
        this.onSuccessEmptyData = block
    }

    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    fun onFailed(block: (errorMsg: String?, code: Int?) -> Unit) {
        this.onFailed = block
    }

}