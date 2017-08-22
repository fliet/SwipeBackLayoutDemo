package com.example.fliest.swipebacklayoutdemo.swipeback;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Fliest on 2017/8/4.
 */

public class SwipeBackHelper {

    private static Stack<SwipeBackPage> pageStack = new Stack<SwipeBackPage>();

    public void onCreate(Activity activity) {
        SwipeBackPage swipeBackPage;
        if((swipeBackPage = findPageWithActivity(activity)) == null){
            swipeBackPage = pageStack.push(new SwipeBackPage(activity));
        }

        swipeBackPage.onCreate();
    }

    private static SwipeBackPage findPageWithActivity(Activity activity){
        for (SwipeBackPage page : pageStack) {
            if(page.mActivity == activity){
                return page;
            }
        }

        return null;
    }

    public SwipeBackPage getCurrentPage(Activity activity){
        return findPageWithActivity(activity);
    }

    public static SwipeBackPage getPrePage(SwipeBackPage swipeBackPage){
        int index = pageStack.indexOf(swipeBackPage);
        SwipeBackPage page = pageStack.get(index - 1);
        return page;
    }

    public static void onDestroy(Activity activity){
        SwipeBackPage page;
        if ((page = findPageWithActivity(activity)) == null){
            throw new RuntimeException("You Should call SwipeBackHelper.onCreate(activity) first");
        }
        pageStack.remove(page);
        page.mActivity=null;
    }
}
