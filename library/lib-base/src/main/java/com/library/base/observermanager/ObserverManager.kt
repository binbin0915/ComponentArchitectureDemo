package com.library.base.observermanager


/**
 * ## 通过观察者设计模式刷新数据或者关闭界面
 * 1. 可以用于登陆后返回到上一个界面，对界面进行刷新；
 * 2. 可以解决用startActivityForResult新打开的界面中返回数据难以操作的问题；
 * 3. 可以同时对整个项目中已经打开的多个界面同时进行数据更新，方便维护；
 * 4. 可以对指定界面进行指定操作，例如关闭指定界面
 *
 * 创建时间：2022.05.09
 * @author WangKai
 */

open class ObserverManager : SubjectListener {
    companion object {
        //观察者接口集合
        private val list: ArrayList<ObserverListener> = ArrayList()

        /**
         * 单例
         */
        private val observerManager: ObserverManager = ObserverManager()
        fun getInstance(): ObserverManager {
            return observerManager
        }
    }


    override fun add(observerListener: ObserverListener) {
        list.add(observerListener);
    }

    override fun notifyObserver(content: String) {
        list.forEach {
            it.observerUpData(content)
        }
    }

    override fun remove(observerListener: ObserverListener) {
        if (list.contains(observerListener)) {
            list.remove(observerListener);
        }
    }
}