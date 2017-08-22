package com.example.fliest.swipebacklayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fliest.swipebacklayoutdemo.swipeback.SwipeBackHelper;

public class SwipeBackActivity extends AppCompatActivity {
    SwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_back);

        mSwipeBackHelper = new SwipeBackHelper();
        mSwipeBackHelper.onCreate(this);
    }

    @Override
    public void onBackPressed() {
        mSwipeBackHelper.getCurrentPage(this).getSlidingLayout().onScrollClose();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SwipeBackHelper.onDestroy(this);
    }
}
