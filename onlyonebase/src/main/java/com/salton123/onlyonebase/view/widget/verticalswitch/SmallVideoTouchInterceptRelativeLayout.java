package com.salton123.onlyonebase.view.widget.verticalswitch;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * User: newSalton@outlook.com
 * Date: 2017/12/23 22:17
 * ModifyTime: 22:17
 * Description:
 */
public class SmallVideoTouchInterceptRelativeLayout extends RelativeLayout {
    private boolean mIsInterceptTouch = false;

    public SmallVideoTouchInterceptRelativeLayout(Context context) {
        super(context);
    }

    public SmallVideoTouchInterceptRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallVideoTouchInterceptRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmallVideoTouchInterceptRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mIsInterceptTouch) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }


    public void setInterceptTouch(boolean isInterceptTouch) {
        mIsInterceptTouch = isInterceptTouch;
    }
}
