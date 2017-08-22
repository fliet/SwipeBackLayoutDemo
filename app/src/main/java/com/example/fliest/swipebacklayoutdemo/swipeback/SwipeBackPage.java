package com.example.fliest.swipebacklayoutdemo.swipeback;

import android.app.Activity;

import com.example.fliest.swipebacklayoutdemo.FrontSlider;


/**
 * Created by Fliest on 2017/8/4.
 */

public class SwipeBackPage {
    public Activity mActivity;
    private SwipeBackLayout mSlidingLayout;
    private boolean mSwipeEnable = true;

    public SwipeBackPage(Activity activity){
        mActivity = activity;
    }

    public void onCreate(){
        mSlidingLayout = new SwipeBackLayout(mActivity);
        if(mSwipeEnable){
            mSlidingLayout.bindActivity(mActivity);
        }

        FrontSlider frontSlider = new FrontSlider(this);
        mSlidingLayout.setSwipeBackListener(frontSlider);
    }

    public SwipeBackLayout getSlidingLayout(){
        return mSlidingLayout;
    }

    public SwipeBackPage setRequestDisallowInterceptTouchEvent(boolean disallowIntercept){
        mSlidingLayout.setRequestDisallowInterceptTouchEvent(disallowIntercept);
        return this;
    }

    public SwipeBackPage setSwipeBackEnable(boolean enable) {
        mSlidingLayout.setEnableGesture(enable);

        return this;
    }
}
