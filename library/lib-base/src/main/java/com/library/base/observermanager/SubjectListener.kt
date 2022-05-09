package com.library.base.observermanager

interface SubjectListener {
    fun add( observerListener:ObserverListener)
    fun notifyObserver( content:String)
    fun remove( observerListener:ObserverListener)
}