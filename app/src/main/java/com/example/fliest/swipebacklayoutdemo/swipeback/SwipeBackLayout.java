package com.example.fliest.swipebacklayoutdemo.swipeback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.example.fliest.swipebacklayoutdemo.R;
import com.example.fliest.swipebacklayoutdemo.util.DensityUtil;
import com.example.fliest.swipebacklayoutdemo.util.DeviceInfoUtil;


public class SwipeBackLayout extends FrameLayout {
    // 页面边缘阴影的宽度默认值
    private static final int SHADOW_WIDTH = 16;
    private Activity mActivity;
    private Scroller mScroller;
    // 页面边缘的阴影图
    private Drawable mLeftShadow;
    // 页面边缘阴影的宽度
    private int mShadowWidth;
    private int mInterceptDownX;
    private int mLastInterceptX;
    private int mLastInterceptY;
    private int mTouchDownX;
    private int mLastTouchX;
    private int mLastTouchY;
    private boolean isConsumed = false;
    private View mContentView;
    private VelocityTracker mVelocityTracker;
    private SwipeBackListener mListener;
    private boolean mDisallowIntercept = true;
    private boolean mEnable = true;
    private Context mContext;
    private Paint mPaint;
    private View mView;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mVelocityTracker = VelocityTracker.obtain();

        initView(context);

        //mView = LayoutInflater.from(context).inflate(R.layout.layout_item_slide_layout,this,false);


        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);
        mLeftShadow = getResources().getDrawable(R.drawable.shadow);
        int density = (int) getResources().getDisplayMetrics().density;
        mShadowWidth = 25;
    }

    /**
     * 绑定Activity
     */
    public void bindActivity(Activity activity) {
        mActivity = activity;
        ViewGroup decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        
        View child = decorView.getChildAt(0);

        decorView.removeView(child);
        addView(child);
       // setContentView(child);
        decorView.addView(this);
    }

    private void setContentView(View view) {
        mContentView = view;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(!mDisallowIntercept){
            return false;
        }
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                mInterceptDownX = x;
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastInterceptX;
                int deltaY = y - mLastInterceptY;
                // 手指处于屏幕边缘，且横向滑动距离大于纵向滑动距离时，拦截事件
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                } else {
                    intercept = false;
                }
                mLastInterceptX = x;
                mLastInterceptY = y;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                mInterceptDownX = mLastInterceptX = mLastInterceptY = 0;
                break;
        }
        return intercept;
    }

    float xVeloticty = 0;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(!mEnable){
            return false;
        }

        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mLastTouchX = x;
                mLastTouchY = y;
                break;
            case MotionEvent.ACTION_MOVE:


                int deltaX = x - mLastTouchX;
                int deltaY = y - mLastTouchY;

                onScroll();

                if (!isConsumed && mTouchDownX < (getWidth() / 10) && Math.abs(deltaX) > Math.abs(deltaY)) {
                    isConsumed = true;
                }

                if (isConsumed) {
                    int rightMovedX = mLastTouchX - (int) ev.getX();
                    // 左侧即将滑出屏幕
                    if (getScrollX() + rightMovedX >= 0) {
                        scrollTo(0, 0);
                    } else {
                        scrollBy(rightMovedX, 0);
                    }
                }
                mLastTouchX = x;
                mLastTouchY = y;

                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(100);
                xVeloticty = mVelocityTracker.getXVelocity();

                break;
            case MotionEvent.ACTION_UP:

                isConsumed = false;
                mTouchDownX = mLastTouchX = mLastTouchY = 0;
                // 根据手指释放时的位置决定回弹还是关闭

                if (xVeloticty >= 200) {
                    scrollClose();
                }

                if (xVeloticty <= -70) {
                    scrollBack();
                }

                if (xVeloticty < 200 && xVeloticty > -70) {
                    if (-getScrollX() < getWidth() / 2) {
                        scrollBack();
                    } else {
                        scrollClose();
                    }
                }

                break;
        }
        return true;
    }

    private void onScroll(){
        mListener.onScroll(getWidth(),-getScrollX());
    }

    public void onScrollClose(){
        mListener.onScrollCloser();
    }

    /**
     * 滑动返回
     */
    private void scrollBack() {
        int startX = getScrollX();
        int dx = -getScrollX();
        mScroller.startScroll(startX, 0, dx, 0, 600);
        invalidate();
    }


    /**
     * 滑动关闭
     */
    private void scrollClose() {
        int startX = getScrollX();
        int dx = -getScrollX() - getWidth();
        mScroller.startScroll(startX, 0, dx, 0, 600);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            onScroll();
            postInvalidate();
        } else if (-getScrollX() >= getWidth()) {
            mActivity.finish();
        }
        System.out.println("getScrollX():" + getScrollX());

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //drawStatueBar(canvas);
        if (-getScrollX() <= getWidth() - 10) {
            drawShadow(canvas);
        }

        drawAlpha(canvas);
    }

    private void drawStatueBar(Canvas canvas){
        int width = DeviceInfoUtil.getDeviceWidth(mContext);
        int height = DensityUtil.dip2px(mContext,23);

        Rect rect = new Rect(0,0,width,height);
        canvas.drawRect(rect,mPaint);
    }

    /**
     * 绘制边缘的阴影
     */
    int shadowWidth = 0;

    private void drawShadow(Canvas canvas) {
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());
        canvas.save();
        canvas.translate(-mShadowWidth, 0);

        mLeftShadow.draw(canvas);
        canvas.restore();
    }

    private void drawAlpha(Canvas canvas) {
        canvas.save();
        canvas.translate(getScrollX(), 0);

        int currentColor = 150 + getScrollX() * 150 / getWidth();
        // System.out.println("currentColor:" + currentColor);
        canvas.clipRect(0, 0, -getScrollX(), getHeight());

        if (currentColor > 0) {
            canvas.drawColor(Color.argb(currentColor, 0, 0, 0));
        }

        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mVelocityTracker.clear();
        mVelocityTracker.recycle();
    }

    public void setRequestDisallowInterceptTouchEvent(boolean disallowIntercept){
        mDisallowIntercept = disallowIntercept;
    }

    public void setEnableGesture(boolean enable) {
        mEnable = enable;
    }

    public void setSwipeBackListener(SwipeBackListener listener){
        mListener = listener;
    }

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int width = DeviceInfoUtil.getDeviceWidth(mContext);
        int height = DensityUtil.dip2px(mContext,23);
        mView.layout(0,0,width,height);
        //mView.bringToFront();
        bringChildToFront(mView);
    }*/
}
