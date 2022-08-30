package com.model.home.pages.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

/**
 * @author wangkai
 */
public class FixScrollingBehavior extends AppBarLayout.ScrollingViewBehavior {

    private AppBarLayout appBarLayout;

    public FixScrollingBehavior() {
        super();
    }

    public FixScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 在CoordinatorLayout中通过循环遍历的方式找到被依赖的View
     *
     * @param parent     parent
     * @param child      child
     * @param dependency dependency
     * @return boolean
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }


    /**
     * @param parent     parent
     * @param child      child
     * @param dependency dependency
     */
    @Override
    public void onDependentViewRemoved(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }

    /**
     * 每次dependency的大小或位置有所变化的时候都会调用该方法
     *
     * @param parent     parent
     * @param child      child
     * @param dependency dependency
     */
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
