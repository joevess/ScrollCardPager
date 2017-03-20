package com.joevess.scrollcardpager;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 屏蔽所有触摸事件
 */
public class NotouchLayout extends LinearLayout{

    public NotouchLayout(Context context) {
        super(context);
    }

    public NotouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}

