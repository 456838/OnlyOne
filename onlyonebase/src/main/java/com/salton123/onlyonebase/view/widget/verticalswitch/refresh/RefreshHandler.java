package com.salton123.onlyonebase.view.widget.verticalswitch.refresh;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.salton123.util.log.MLog;

/**
 * Created by ZZB on 2017/9/27.
 */

public class RefreshHandler {
    private static final String TAG = "RefreshHandler";
    private boolean mCanRefresh;
    private IRefreshHeader mRefreshHeader;
    private boolean mIsHandlingRefresh;
    private float mScrollY;
    private Callback mCallback;

    public interface Callback {
        void onRefresh();

        void scrollToWithAnimation(float dy);

        void onScroll(float dy);
    }

    public RefreshHandler(ViewGroup parent, Callback callback) {
        mCallback = callback;
        mRefreshHeader = new DefaultRefreshHeader(parent);
        addHeaderToParent(parent);
    }

    public void onActionDown() {
        mScrollY = 0;
    }

    public void onActionUp() {
        if (!mRefreshHeader.isLoading()) {
            mIsHandlingRefresh = false;
        }
        if (Math.abs(mScrollY) >= mRefreshHeader.getMinHeight() && mCanRefresh) {
            MLog.info(TAG, "onActionUp, refresh");
            mCallback.scrollToWithAnimation(mRefreshHeader.getMinHeight());
            mCallback.onRefresh();
        } else {
            MLog.info(TAG, "onActionUp, restore layout");
            completeRefresh();
        }
    }

    public void setCanRefresh(boolean canRefresh) {
        mCanRefresh = canRefresh;
    }

    public void completeRefresh() {
        mIsHandlingRefresh = false;
        mRefreshHeader.hide();
        mCallback.scrollToWithAnimation(0);
    }

    public boolean isHandlingRefresh() {
        return mIsHandlingRefresh;
    }

    public void onScrollToRefresh(float dy) {
        mIsHandlingRefresh = true;
        //dy < 0
        dy = fixDyIfNeeded(dy);
        mScrollY += dy;
        Log.d(TAG, "onScrollToRefresh, dy: " + dy + "  scrollY: " + mScrollY);
        if (mCanRefresh) {
            MLog.info(TAG, "show refresh");
            mRefreshHeader.showLoading();
        } else {
            Log.d(TAG, "show no refresh");
            mRefreshHeader.showNoRefresh();
        }
        mCallback.onScroll(dy);
    }

    //dy < 0
    private float fixDyIfNeeded(float dy) {
        if (Math.abs(mScrollY + dy) > mRefreshHeader.getMaxHeight()) {
            dy = -(mRefreshHeader.getMaxHeight() - Math.abs(mScrollY));
        }
        return dy;
    }

    private void addHeaderToParent(ViewGroup parent) {
        View headerView = mRefreshHeader.getView();
        ViewGroup.LayoutParams layoutParams = headerView.getLayoutParams();
        if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutParams;
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            parent.addView(headerView, params);
        } else if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
            params.gravity = Gravity.TOP;
            parent.addView(headerView, params);
        } else {
            throw new IllegalArgumentException("parent must be RelativeLayout or FrameLayout");
        }
    }
}
