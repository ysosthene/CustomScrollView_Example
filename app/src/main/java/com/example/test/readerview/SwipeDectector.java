package com.example.test.readerview;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class SwipeDectector implements View.OnTouchListener {

    private static final float MIN_DISTANCE = 10;
    private float downY;
    private float upY;

    public SwipeDectector() {
    }

    public void onTopToBottomSwipe() {
        Log.d("swipe", "onTopToBottomSwipe! deltaY =" + (downY - upY));
    }

    public void onBottomToTopSwipe() {
        Log.d("swipe", "onBottomToTopSwipe! deltaY =" + (downY - upY));
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upY = event.getY();
                float deltaY = downY - upY;

                // swipe vertical
                if (Math.abs(deltaY) > MIN_DISTANCE) {

                    AndroidBus bus = new AndroidBus();

                    // top or down
                    if (deltaY < 0) {
                        bus.post(new ScrollUpEvent());
                        BusProvider.getInstance().post(new ScrollUpEvent());
                        this.onTopToBottomSwipe();
                    }
                    if (deltaY > 0) {
                        bus.post(new ScrollUpEvent());
                        BusProvider.getInstance().post(new ScrollDownEvent());
                        this.onBottomToTopSwipe();
                    }
                } else {
                    Log.i("Move", "Swipe was only " + Math.abs(deltaY) + " long, need at least " + MIN_DISTANCE);
                }
                return true;
            }
            default:
                return false;
        }

    }
}

