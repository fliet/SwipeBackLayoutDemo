package com.example.fliest.swipebacklayoutdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fliest.swipebacklayoutdemo.swipeback.SwipeBackHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeBackHelper swipeBackHelper = new SwipeBackHelper();
        swipeBackHelper.onCreate(this);
        swipeBackHelper.getCurrentPage(this)
                .setRequestDisallowInterceptTouchEvent(false)
                .setSwipeBackEnable(false);

        View decorView = getWindow().getDecorView();
        decorView.setBackgroundColor(Color.BLACK);

        Button button = (Button) findViewById(R.id.btn_swipe);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SwipeBackActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SwipeBackHelper.onDestroy(this);
    }
}
