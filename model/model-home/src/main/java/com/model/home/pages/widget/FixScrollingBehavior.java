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
     * child就是绑定此behavior的view，dependency是发送变化的view
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
     * 此处child 就是fab，dependency是被依赖的view
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
