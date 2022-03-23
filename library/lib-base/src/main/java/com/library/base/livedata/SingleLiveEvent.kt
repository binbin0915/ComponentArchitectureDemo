package com.library.base.livedata

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 作用描述：解决LiveData 数据倒灌的问题（LiveData会经常多次回调数据）
 *
 * 复现：我们的ViewModel里是给Activity持有的并且里面有一个LiveData数据，
 * 我们A_Fragment现在获得Activity的ViewModel并且注册LiveData数据成为观察者，
 * 这个时候我们setValue()就会让前台的A_Fragment得到一次LiveData数据，
 * 接下来操作 A_Fragment 启动 B_Fragment，在返回到A_Fragment。
 * 你会发现只要再次注册LiveData的observe(this, new Observer ...)，那么A_Fragment里面又会接收到一次LiveData的数据。
 *
 * 分析原因：
 * * 一部分原因是LiveData的机制，就是向所有前台Fragment或者Activity发送数据。只要注册的观察者在前台就必定会收到这个数据。
 * * 另一部分的原因是对ViewModel理解不深刻，理论上只有在Activity保存的ViewModel它没被销毁过就会一直给新的前台Fragment观察者发送数据。我们需要管理好ViewModel的使用范围。 比如只需要在Fragment里使用的ViewModel就不要给Activity保管。而根Activity的ViewModel只需要做一下数据共享与看情况使用LiveData。
 *
 * 解决方案：
 * * 管理好ViewModel的范围，如果业务范围只跟某个Fragment有关，那么最好就只给这个Fragment使用。这样Fragment在销毁或者创建的时候，也会销毁ViewModel与创建ViewModel，ViewModel携带的LiveData就是全新的不会在发送之前设置的数据。
 * * 复写类 SingleLiveEvent，其中的机制是用一个原子 AtomicBoolean记录一次setValue。在发送一次后在将AtomicBoolean设置为false，阻止后续前台重新触发时的数据发送。
 *
 * 创建时间：2022/03/18
 * @author：WangKai
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.w(
                "===>>>", "Multiple observers registered but only one will be notified of changes."
            )
        }

        // Observe the internal MutableLiveData
        super.observe(owner, Observer<T> { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(@Nullable t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}