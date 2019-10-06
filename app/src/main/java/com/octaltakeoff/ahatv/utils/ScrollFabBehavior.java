package com.octaltakeoff.ahatv.utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class ScrollFabBehavior extends FloatingActionButton.Behavior {
    public ScrollFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        // Scrolling down
        if (dyConsumed < 0 && child.getVisibility() == View.VISIBLE){

            child.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                /**
                 * Called when a FloatingActionButton has been hidden
                 * @param fab the FloatingActionButton that was hidden.
                 */
                @Override
                public void onHidden(FloatingActionButton fab) {
                    super.onHidden(fab);
                    fab.setVisibility(View.INVISIBLE);

                }
            });

            // Scrolling UP
        }else if (dyConsumed > 0 && child.getVisibility() != View.VISIBLE){
            child.hide();
        }

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        final boolean ret = nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes, type);
        return ret;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);

        if (child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }

}


