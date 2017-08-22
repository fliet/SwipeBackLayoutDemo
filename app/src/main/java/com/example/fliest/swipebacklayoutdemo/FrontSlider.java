package com.example.fliest.swipebacklayoutdemo;

import android.os.Build;

import com.example.fliest.swipebacklayoutdemo.swipeback.SwipeBackHelper;
import com.example.fliest.swipebacklayoutdemo.swipeback.SwipeBackListener;
import com.example.fliest.swipebacklayoutdemo.swipeback.SwipeBackPage;

/**
 * Created by Fliest on 2017/8/4.
 */

public class FrontSlider implements SwipeBackListener {
    private SwipeBackPage mCurrentPage;
    private boolean isClose = false;

    public FrontSlider(SwipeBackPage swipeBackPage) {
        mCurrentPage = swipeBackPage;
    }


    @Override
    public void onScroll(int width, int delta) {
        int offset = 100;
        SwipeBackPage page = SwipeBackHelper.getPrePage(mCurrentPage);
        if (Build.VERSION.SDK_INT > 11 && !isClose) {
            float perscentage = (delta + offset) * 1.0f / width;
            float scaleY = (1 - 0.9f) * perscentage + 0.9f;
            float scaleX = (1 - 0.9f) * perscentage + 0.9f;
            if (page != null) {
                page.getSlidingLayout().setScaleY(scaleY);
                page.getSlidingLayout().setScaleX(scaleX);
            }

            if (perscentage == 1) {
                page.getSlidingLayout().setScaleY(1);
                page.getSlidingLayout().setScaleX(1);
            }
        }
    }

    @Override
    public void onScrollCloser() {
        SwipeBackPage page = SwipeBackHelper.getPrePage(mCurrentPage);
        if (Build.VERSION.SDK_INT > 11) {
            if (page != null) {
                page.getSlidingLayout().setScaleY(1);
                page.getSlidingLayout().setScaleX(1);
                isClose = true;
            }
        }
    }
}
