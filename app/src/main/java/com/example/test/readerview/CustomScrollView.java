package com.example.test.readerview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    SwipeDectector swipeDetect = new SwipeDectector();
    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isUp = swipeDetect.onTouch(this,ev);
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            swipeDetect.onTouch(this,ev);
        }

        return super.onInterceptTouchEvent(ev);

    }


}
