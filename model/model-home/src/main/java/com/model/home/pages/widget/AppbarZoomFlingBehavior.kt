package com.model.home.pages.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.model.home.R
import java.lang.reflect.Field
import kotlin.math.max
import kotlin.math.min


/**
 * ###解决的问题：
 * - 头部下拉放大Behavior
 * - 解决appbarLayout若干问题
 * 1. 快速滑动appbarLayout会出现回弹
 * 2. 快速滑动appbarLayout到折叠状态下，立马下滑，会出现抖动的问题
 * 3. 滑动appbarLayout，无法通过手指按下让其停止滑动
 */
class AppbarZoomFlingBehavior(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {
    companion object {
        /**
         * 放大最大高度
         */
        private const val MAX_ZOOM_HEIGHT = 500f
    }

    private var isFlinging = false
    private var shouldBlockNestedScroll = false

    /**
     * 是否做动画标志
     */
    private var isAnimate = false

    /**
     * 放大View的动画
     */
    private lateinit var valueAnimator: ValueAnimator

    /**
     * 要放大的View
     */
    private lateinit var mImageView: ImageView

    /**
     * 记录AppbarLayout原始高度
     */
    private var mAppbarHeight = 0

    /**
     * 记录要放大View原始高度
     */
    private var mImageViewHeight = 0

    /**
     * 手指在Y轴滑动的总距离
     */
    private var mTotalDy = 0f

    /**
     * 放大View的缩放比例
     */
    private var mScaleValue = 0f

    /**
     * Appbar的变化高度
     */
    private var mLastBottom = 0

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        abl: AppBarLayout,
        layoutDirection: Int
    ): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        init(abl)
        return handled
    }

    /**
     * 进行初始化操作，在这里获取到ImageView的引用，和Appbar的原始高度
     */
    private fun init(appBarLayout: AppBarLayout) {
        appBarLayout.clipChildren = false
        mAppbarHeight = appBarLayout.height
        mImageView = appBarLayout.findViewById(R.id.fragment4_imageview)
        mImageViewHeight = mImageView.height
    }

    /**
     * 1. 嵌套滑动时，停止AppbarLayout的fling
     * 2. 可以下拉放大View
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        stopAppbarLayoutFling(child)
        isAnimate = true
        return super.onStartNestedScroll(
            parent,
            child,
            directTargetChild,
            target,
            nestedScrollAxes,
            type
        )
    }

    /**
     * 是否应该阻止嵌套滚动
     */
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (!shouldBlockNestedScroll) {
            super.onNestedScroll(
                coordinatorLayout, child,
                target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed
            )
        }
    }

    /**
     * 在这里做具体的滑动处理
     */
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        /*
        type返回1时，表示当前target处于非touch的滑动，
        该bug的引起是因为appbar在滑动时，CoordinatorLayout内的实现NestedScrollingChild2接口的滑动子类还未结束其自身的fling
        所以这里监听子类的非touch时的滑动，然后block掉滑动事件传递给AppBarLayout
        */
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            isFlinging = true
        }
        /*scrollview滑动*/
        if (!shouldBlockNestedScroll) {
            if (child.bottom >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
                zoomHeaderImageView(child, dy)
            } else {
                if (child.bottom > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
                    consumed[1] = dy
                    zoomHeaderImageView(child, dy)
                } else {
                    if (!this::valueAnimator.isInitialized || !valueAnimator.isRunning) {
                        super.onNestedPreScroll(
                            coordinatorLayout,
                            child,
                            target,
                            dx,
                            dy,
                            consumed,
                            type
                        )
                    }
                }
            }
        }
    }

    /**
     * 对ImageView进行缩放处理，对AppbarLayout进行高度的设置
     */
    private fun zoomHeaderImageView(abl: AppBarLayout, dy: Int) {
        mTotalDy -= dy.toFloat()
        mTotalDy = min(mTotalDy, MAX_ZOOM_HEIGHT)
        mScaleValue = max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT)
        mImageView.scaleX = mScaleValue
        mImageView.scaleY = mScaleValue
        mLastBottom = mAppbarHeight + (mImageViewHeight / 2 * (mScaleValue - 1)).toInt()
        abl.bottom = mLastBottom
    }

    /**
     * 处理惯性滑动的情况
     */
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        /*下拉最大距离*/
        val maxVelocityY = 100
        if (velocityY > maxVelocityY) {
            isAnimate = false
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
    }

    /**
     * 滑动停止的时候，恢复AppbarLayout、ImageView的原始状态
     */
    override fun onStopNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        abl: AppBarLayout,
        target: View,
        type: Int
    ) {
        recovery(abl)
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
        isFlinging = false
        shouldBlockNestedScroll = false
    }

    /**
     * 通过属性动画的形式，恢复AppbarLayout、ImageView的原始状态
     */
    private fun recovery(abl: AppBarLayout) {
        if (mTotalDy > 0) {
            mTotalDy = 0f
            if (isAnimate) {
                valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(220)
                valueAnimator.addUpdateListener { animation: ValueAnimator ->
                    val value = animation.animatedValue as Float
                    mImageView.scaleX = value
                    mImageView.scaleY = value
                    abl.bottom =
                        (mLastBottom - (mLastBottom - mAppbarHeight) * animation.animatedFraction).toInt()
                }
                valueAnimator.start()
            } else {
                mImageView.scaleX = 1f
                mImageView.scaleY = 1f
                abl.bottom = mAppbarHeight
            }
        }
    }

    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        shouldBlockNestedScroll = false
        if (isFlinging) {
            shouldBlockNestedScroll = true
        }
        val x = ev.x
        val y = ev.y
        return when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                stopAppbarLayoutFling(child)
                super.onInterceptTouchEvent(parent, child, ev)
            }
            else -> super.onInterceptTouchEvent(parent, child, ev)
        }

    }

    /**
     * 手指触摸屏幕的时候停止fling事件
     * 停止appbarLayout的fling事件
     * @param appBarLayout
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            val flingRunnableField: Field = getFlingRunnableField()
            val scrollerField: Field = getScrollerField()
            flingRunnableField.isAccessible = true
            scrollerField.isAccessible = true
            val overScroller = scrollerField.get(this) as OverScroller
            val flingRunnable = flingRunnableField.get(this) as Runnable
            appBarLayout.removeCallbacks(flingRunnable)
            flingRunnableField.set(this, null)
            if (!overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    /**
     * 反射获取私有的flingRunnable 属性，考虑support 28以后变量名修改的问题
     * @return Field
     */
    @Throws(NoSuchFieldException::class)
    private fun getFlingRunnableField(): Field {
        return try {
            // support design 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mFlingRunnable")
        } catch (e: NoSuchFieldException) {
            // 可能是28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("flingRunnable")
        }
    }

    /**
     * 反射获取私有的scroller 属性，考虑support 28以后变量名修改的问题
     * @return Field
     */
    @Throws(NoSuchFieldException::class)
    private fun getScrollerField(): Field {
        return try {
            // support design 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mScroller")
        } catch (e: NoSuchFieldException) {
            // 可能是28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("scroller")
        }
    }
}