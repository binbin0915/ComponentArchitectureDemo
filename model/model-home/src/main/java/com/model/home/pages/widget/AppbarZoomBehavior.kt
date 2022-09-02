package com.model.home.pages.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.model.home.R
import kotlin.math.max
import kotlin.math.min

/**
 * 头部下拉放大Behavior
 */
class AppbarZoomBehavior(context: Context, attrs: AttributeSet) :
    AppBarLayout.Behavior(context, attrs) {

    companion object {
        /**
         * 放大最大高度
         */
        private const val MAX_ZOOM_HEIGHT = 500f
    }

    private lateinit var valueAnimator: ValueAnimator

    /**
     * 要放大的图片
     */
    private lateinit var mImageView: ImageView

    /**
     * 记录AppbarLayout原始高度
     */
    private var mAppbarHeight = 0

    /**
     * 记录ImageView原始高度
     */
    private var mImageViewHeight = 0

    /**
     * 手指在Y轴滑动的总距离
     */
    private var mTotalDy = 0f

    /**
     * 图片缩放比例
     */
    private var mScaleValue = 0f

    /**
     * Appbar的变化高度
     */
    private var mLastBottom = 0

    /**
     * 是否做动画标志
     */
    private var isAnimate = false

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
     * 是否处理嵌套滑动
     */
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        isAnimate = true
        return true
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
}