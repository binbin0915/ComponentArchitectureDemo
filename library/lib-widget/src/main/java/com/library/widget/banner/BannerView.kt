package com.library.widget.banner

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.library.widget.R
import com.library.widget.banner.ext.dp
import com.library.widget.databinding.WidgetItemBannerImageBinding
import kotlinx.coroutines.*
import kotlin.reflect.KClass
import kotlin.reflect.full.functions

class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //指示器属性
    private var showIndicator = true
    private var indicatorBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    private var indicatorDrawableResId = R.drawable.widget_selector_banner_indicator
    private var indicatorHeight = 44.dp.toInt()
    private var indicatorSpacing = 16.dp.toInt()
    private var indicatorGravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
    private var indicatorPaddingStart = indicatorSpacing
    private var indicatorPaddingEnd = indicatorSpacing
    private var indicatorMarginStart = 0
    private var indicatorMarginEnd = 0
    private var indicatorMarginTop = 0
    private var indicatorMarginBottom = 0
    private var isNumberIndicator = false
    private var numberIndicatorTextColor = Color.WHITE
    private var numberIndicatorTextSize = 14.dp.toInt()

    private var autoplay = true
    private var loopPlay = true
    private var autoplayInterval = 3000//ms
    private var pageChangeDuration = 500//ms
    private var pageLimit = 1
    private var pagePaddingTop = 0
    private var pagePaddingBottom = 0
    private var pagePaddingStart = 0
    private var pagePaddingEnd = 0

    private var showDisplayText = true
    private var displayTextColor = Color.WHITE
    private var displayTextSize = 14.dp.toInt()
    private var displayTextLines = 1
    private var displayTextStyle = Typeface.NORMAL
    private var displayTextBackground: Drawable = ColorDrawable(Color.TRANSPARENT)
    private var displayTextBgHeight = 44.dp.toInt()
    private var displayTextMarginTop = 0
    private var displayTextMarginBottom = 0
    private var displayTextPaddingStart = 16.dp.toInt()
    private var displayTextPaddingEnd = 16.dp.toInt()
    private var displayTextGravity = Gravity.CENTER_HORIZONTAL
    private var displayTextLayoutGravity = Gravity.BOTTOM
    private lateinit var displayTv: TextView
    private val viewPager: ViewPager2
    private var adapter: Adapter<*, *>? = null
    private lateinit var indicatorParent: LinearLayout
    private lateinit var numberTv: TextView
    private val viewScope = MainScope()
    private var autoplayJob: Job? = null
    private var allowUserScrollable = true
    private var dataSize = 0
    private var displayTextList: List<String>? = null

    init {
        if (attrs != null) {
            initCustomAttrs(context, attrs)
        }
        viewPager = ViewPager2(context)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.offscreenPageLimit = pageLimit
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                switchIndicator(position)
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                setDisplayTv(position, positionOffset)
            }
        })
        val vpView: View = viewPager.getChildAt(0)
        if (vpView is RecyclerView) {
            vpView.setPaddingRelative(
                pagePaddingStart, pagePaddingTop, pagePaddingEnd, pagePaddingBottom
            )
            vpView.clipToPadding = false
        }
        val viewPageLp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(viewPager, viewPageLp)
        if (showDisplayText) {
            initTitleTextLayout()
        }
        if (showIndicator) {
            initIndicatorParent()
        }
    }

    private fun setDisplayTv(position: Int, positionOffset: Float) {
        if (!showDisplayText || displayTextList == null) {
            return
        }
        val leftPosition: Int = position % dataSize
        val rightPosition: Int = (position + 1) % dataSize
        if (rightPosition < dataSize && leftPosition < dataSize) {
            if (positionOffset > 0.5) {
                displayTv.text = displayTextList!![rightPosition]
                displayTv.alpha = positionOffset
            } else {
                displayTv.text = displayTextList!![leftPosition]
                displayTv.alpha = 1 - positionOffset
            }
        }
    }

    private fun initCustomAttrs(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Widget_BannerView)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            initCustomAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    private fun initCustomAttr(attr: Int, typedArray: TypedArray) {
        when (attr) {
            R.styleable.Widget_BannerView_widget_showIndicator -> {
                showIndicator = typedArray.getBoolean(attr, showIndicator)
            }

            R.styleable.Widget_BannerView_widget_indicatorBackground -> {
                indicatorBackground = typedArray.getDrawable(attr)!!
            }

            R.styleable.Widget_BannerView_widget_indicatorDrawable -> {
                indicatorDrawableResId = typedArray.getResourceId(attr, indicatorDrawableResId)
            }

            R.styleable.Widget_BannerView_widget_indicatorHeight -> {
                indicatorHeight = typedArray.getDimensionPixelSize(attr, indicatorHeight)
            }

            R.styleable.Widget_BannerView_widget_indicatorSpacing -> {
                indicatorSpacing = typedArray.getDimensionPixelSize(attr, indicatorSpacing)
            }

            R.styleable.Widget_BannerView_widget_indicatorGravity -> {
                indicatorGravity = typedArray.getInt(attr, indicatorGravity)
            }

            R.styleable.Widget_BannerView_widget_indicatorPaddingStart -> {
                indicatorPaddingStart =
                    typedArray.getDimensionPixelSize(attr, indicatorPaddingStart)
            }

            R.styleable.Widget_BannerView_widget_indicatorPaddingEnd -> {
                indicatorPaddingEnd = typedArray.getDimensionPixelSize(attr, indicatorPaddingEnd)
            }

            R.styleable.Widget_BannerView_widget_indicatorMarginStart -> {
                indicatorMarginStart = typedArray.getDimensionPixelSize(attr, indicatorMarginStart)
            }

            R.styleable.Widget_BannerView_widget_indicatorMarginEnd -> {
                indicatorMarginEnd = typedArray.getDimensionPixelSize(attr, indicatorMarginEnd)
            }

            R.styleable.Widget_BannerView_widget_indicatorMarginTop -> {
                indicatorMarginTop = typedArray.getDimensionPixelSize(attr, indicatorMarginTop)
            }

            R.styleable.Widget_BannerView_widget_indicatorMarginBottom -> {
                indicatorMarginBottom =
                    typedArray.getDimensionPixelSize(attr, indicatorMarginBottom)
            }

            R.styleable.Widget_BannerView_widget_isNumberIndicator -> {
                isNumberIndicator = typedArray.getBoolean(attr, isNumberIndicator)
            }

            R.styleable.Widget_BannerView_widget_numberIndicatorTextColor -> {
                numberIndicatorTextColor = typedArray.getColor(attr, numberIndicatorTextColor)
            }

            R.styleable.Widget_BannerView_widget_numberIndicatorTextSize -> {
                numberIndicatorTextSize =
                    typedArray.getDimensionPixelSize(attr, numberIndicatorTextSize)
            }

            R.styleable.Widget_BannerView_widget_autoplay -> {
                autoplay = typedArray.getBoolean(attr, autoplay)
            }

            R.styleable.Widget_BannerView_widget_loopPlay -> {
                loopPlay = typedArray.getBoolean(attr, loopPlay)
            }

            R.styleable.Widget_BannerView_widget_autoplayInterval -> {
                autoplayInterval = typedArray.getInt(attr, autoplayInterval)
            }

            R.styleable.Widget_BannerView_widget_pageChangeDuration -> {
                pageChangeDuration = typedArray.getInt(attr, pageChangeDuration)
            }

            R.styleable.Widget_BannerView_widget_pageLimit -> {
                pageLimit = typedArray.getInt(attr, pageLimit)
            }

            R.styleable.Widget_BannerView_widget_pagePaddingTop -> {
                pagePaddingTop = typedArray.getDimensionPixelSize(attr, pagePaddingTop)
            }

            R.styleable.Widget_BannerView_widget_pagePaddingBottom -> {
                pagePaddingBottom = typedArray.getDimensionPixelSize(attr, pagePaddingBottom)
            }

            R.styleable.Widget_BannerView_widget_pagePaddingStart -> {
                pagePaddingStart = typedArray.getDimensionPixelSize(attr, pagePaddingStart)
            }

            R.styleable.Widget_BannerView_widget_pagePaddingEnd -> {
                pagePaddingEnd = typedArray.getDimensionPixelSize(attr, pagePaddingEnd)
            }

            R.styleable.Widget_BannerView_widget_showDisplayText -> {
                showDisplayText = typedArray.getBoolean(attr, showDisplayText)
            }

            R.styleable.Widget_BannerView_widget_displayTextColor -> {
                displayTextColor = typedArray.getColor(attr, displayTextColor)
            }

            R.styleable.Widget_BannerView_widget_displayTextSize -> {
                displayTextSize = typedArray.getDimensionPixelSize(attr, displayTextSize)
            }

            R.styleable.Widget_BannerView_widget_displayTextLines -> {
                displayTextLines = typedArray.getInt(attr, displayTextLines)
            }

            R.styleable.Widget_BannerView_widget_displayTextStyle -> {
                displayTextStyle = typedArray.getInt(attr, displayTextStyle)
            }

            R.styleable.Widget_BannerView_widget_displayTextBackground -> {
                displayTextBackground = typedArray.getDrawable(attr)!!
            }

            R.styleable.Widget_BannerView_widget_displayTextBgHeight -> {
                displayTextBgHeight = typedArray.getDimensionPixelSize(attr, displayTextBgHeight)
            }

            R.styleable.Widget_BannerView_widget_displayTextMarginTop -> {
                displayTextMarginTop = typedArray.getDimensionPixelSize(attr, displayTextMarginTop)
            }

            R.styleable.Widget_BannerView_widget_displayTextMarginBottom -> {
                displayTextMarginBottom =
                    typedArray.getDimensionPixelSize(attr, displayTextMarginBottom)
            }

            R.styleable.Widget_BannerView_widget_displayTextPaddingStart -> {
                displayTextPaddingStart =
                    typedArray.getDimensionPixelSize(attr, displayTextPaddingStart)
            }

            R.styleable.Widget_BannerView_widget_displayTextPaddingEnd -> {
                displayTextPaddingEnd =
                    typedArray.getDimensionPixelSize(attr, displayTextPaddingEnd)
            }

            R.styleable.Widget_BannerView_widget_displayTextGravity -> {
                displayTextGravity = typedArray.getInt(attr, displayTextGravity)
            }

            R.styleable.Widget_BannerView_widget_displayTextLayoutGravity -> {
                displayTextLayoutGravity = typedArray.getInt(attr, displayTextLayoutGravity)
            }
        }
    }

    private fun initIndicatorParent() {
        val indicatorLp = LayoutParams(LayoutParams.WRAP_CONTENT, indicatorHeight).apply {
            gravity = indicatorGravity
            marginStart = indicatorMarginStart
            marginEnd = indicatorMarginEnd
            topMargin = indicatorMarginTop
            bottomMargin = indicatorMarginBottom
        }
        indicatorParent = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPaddingRelative(indicatorPaddingStart, 0, indicatorPaddingEnd, 0)
            background = indicatorBackground
        }
        addView(indicatorParent, indicatorLp)
    }

    private fun initTitleTextLayout() {
        val displayTvLp = LayoutParams(LayoutParams.MATCH_PARENT, displayTextBgHeight).apply {
            gravity = displayTextLayoutGravity
            topMargin = displayTextMarginTop
            bottomMargin = displayTextMarginBottom
        }
        displayTv = TextView(context).apply {
            setTextColor(displayTextColor)
            setLines(displayTextLines)
            ellipsize = TextUtils.TruncateAt.END
            typeface = Typeface.defaultFromStyle(displayTextStyle)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, displayTextSize.toFloat())
            gravity = Gravity.CENTER_VERTICAL or displayTextGravity
            setPaddingRelative(displayTextPaddingStart, 0, displayTextPaddingEnd, 0)
            background = displayTextBackground
        }
        addView(displayTv, displayTvLp)
    }

    fun <M> setData(
        dataList: List<M>,
        displayTextList: List<String>? = null,
        bind: (WidgetItemBannerImageBinding, M) -> Unit
    ) {
        setData(dataList, displayTextList, WidgetItemBannerImageBinding::class, bind)
    }

    private fun <VB : ViewBinding, M> setData(
        dataList: List<M>,
        displayTextList: List<String>? = null,
        itemBinding: KClass<VB>,
        bind: (VB, M) -> Unit
    ) {
        if (displayTextList != null && displayTextList.size != dataList.size) {
            throw IllegalStateException("The length of displayTextList and dataList must be equal!")
        }
        this.displayTextList = displayTextList
        dataSize = dataList.size
        adapter = Adapter(dataList, itemBinding, bind)
        viewPager.adapter = adapter
        if (showIndicator) {
            updateIndicator()
        }
        if (loopPlay && dataSize > 1) {
            val firstItem: Int = Int.MAX_VALUE / 2 - Int.MAX_VALUE / 2 % dataList.size
            viewPager.setCurrentItem(firstItem, false)
        }
        if (autoplay) {
            startAutoplay()
        }
    }

    private fun updateIndicator() {
        indicatorParent.removeAllViews()
        if (dataSize <= 1) {
            return
        }
        if (!isNumberIndicator) {
            for (i in 0 until dataSize) {
                val indicatorLp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val imageView = ImageView(context)
                imageView.setImageResource(indicatorDrawableResId)
                when (i) {
                    dataSize - 1 -> {
                        indicatorLp.setMargins(0, 0, 0, 0)
                    }

                    else -> {
                        indicatorLp.setMargins(0, 0, indicatorSpacing, 0)
                    }
                }
                indicatorParent.addView(imageView, indicatorLp)
            }
        } else {
            numberTv = TextView(context)
            numberTv.setTextColor(numberIndicatorTextColor)
            numberTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, numberIndicatorTextSize.toFloat())
            indicatorParent.addView(
                numberTv, LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
            )
        }
    }

    private fun switchIndicator(position: Int) {
        if (showIndicator && adapter != null) {
            val realPosition = position % dataSize
            if (isNumberIndicator) {
                numberTv.text = String.format(
                    context.getString(R.string.widget_number_tv), (realPosition + 1), dataSize
                )
            } else {
                indicatorParent.children.forEachIndexed { index, child ->
                    child.isSelected = index == realPosition
                }
            }
        }
    }

    fun setAutoplay(autoplay: Boolean) {
        this.autoplay = autoplay
        if (autoplay) {
            startAutoplay()
        } else {
            stopAutoplay()
        }
    }

    fun setCurrentItem(item: Int) {
        if (adapter == null) {
            return
        }
        if (loopPlay) {
            val realCurrentItem: Int = viewPager.currentItem
            val currentItem: Int = realCurrentItem % dataSize
            val offset = item - currentItem
            viewPager.setCurrentItem(realCurrentItem + offset, false)
        } else {
            viewPager.setCurrentItem(item, false)
        }
    }

    fun setAllowUserScrollable(scrollable: Boolean) {
        allowUserScrollable = scrollable
    }

    fun setPageOverScrollMode(overScrollMode: Int) {
        viewPager.overScrollMode = overScrollMode
    }

    fun addOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        viewPager.registerOnPageChangeCallback(callback)
    }

    fun removeOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
        viewPager.unregisterOnPageChangeCallback(callback)
    }

    fun setPageTransformer(transformer: ViewPager2.PageTransformer) {
        viewPager.setPageTransformer(transformer)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (autoplay && allowUserScrollable) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    stopAutoplay()
                }

                MotionEvent.ACTION_MOVE -> {
                    //防止滑动冲突
                    requestDisallowInterceptTouchEvent(true)
                }

                MotionEvent.ACTION_UP -> {
                    startAutoplay()
                    requestDisallowInterceptTouchEvent(false)
                }

                MotionEvent.ACTION_CANCEL -> {
                    startAutoplay()
                    requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun startAutoplay() {
        if (adapter == null || dataSize <= 1 || visibility != VISIBLE) {
            return
        }
        stopAutoplay()
        autoplayJob = viewScope.launch {
            while (isActive) {
                delay(autoplayInterval.toLong())
                if (!loopPlay && viewPager.currentItem == dataSize - 1) {
                    viewPager.setCurrentItem(0, false)
                } else {
                    viewPager.setCurrentItemWithAnim(
                        viewPager.currentItem + 1,
                        pageChangeDuration.toLong(),
                        viewPager.width - pagePaddingStart - pagePaddingEnd
                    )
                }
            }
        }
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (autoplay) {
            if (visibility == VISIBLE) {
                startAutoplay()
            } else {
                stopAutoplay()
            }
        }
    }

    private fun stopAutoplay() {
        Log.e("AAAAAAAAAAAAXXS", "停止轮播")
        autoplayJob?.cancel()
        autoplayJob = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoplay()
        viewScope.cancel()
    }

    private inner class Adapter<VB : ViewBinding, M>(
        private val dataList: List<M>,
        private val itemClass: KClass<VB>,
        private val bind: (VB, M) -> Unit
    ) : RecyclerView.Adapter<Adapter<VB, M>.ViewHolder>() {

        inner class ViewHolder(private val itemBinding: VB) :
            RecyclerView.ViewHolder(itemBinding.root) {

            fun bind(model: M) {
                this@Adapter.bind(itemBinding, model)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            for (func in itemClass.functions) {
                if (func.name == "inflate" && func.parameters.size == 3) {
                    val itemBinding = func.call(LayoutInflater.from(parent.context), parent, false)
                    @Suppress("UNCHECKED_CAST") return ViewHolder(itemBinding as VB)
                }
            }
            throw IllegalStateException("ViewBinding instantiation exception")
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(dataList[position % dataList.size])
        }

        override fun getItemCount(): Int {
            return if (loopPlay && dataSize > 1) {
                Integer.MAX_VALUE
            } else {
                dataList.size
            }
        }
    }

    private var previousValue = 0
    private val pageChangeAnimator = ValueAnimator().apply {
        addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            viewPager.fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                previousValue = 0
                viewPager.beginFakeDrag()
            }

            override fun onAnimationEnd(animator: Animator) {
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        interpolator = AccelerateDecelerateInterpolator()
    }

    private fun ViewPager2.setCurrentItemWithAnim(
        item: Int,
        duration: Long,
        pagePxWidth: Int = width,
    ) {
        if (pagePxWidth <= 0) {
            return
        }
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        pageChangeAnimator.setIntValues(0, pxToDrag)
        pageChangeAnimator.duration = duration
        pageChangeAnimator.start()
    }

}