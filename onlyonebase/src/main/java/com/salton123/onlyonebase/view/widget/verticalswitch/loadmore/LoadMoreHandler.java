package com.salton123.onlyonebase.view.widget.verticalswitch.loadmore;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.salton123.util.log.MLog;

/**
 * Created by ZZB on 2017/8/31.
 */

public class LoadMoreHandler {
    private static final String TAG = "LoadMoreHandler";
    private boolean mCanLoadMore;
    private ILoadMoreAdapter mLoadMoreAdapter;
    private Callback mCallback;
    private float mScrollY;
    private boolean mIsHandlingLoadMore;

    public LoadMoreHandler(ViewGroup parent, Callback callback) {
        mCallback = callback;
        mLoadMoreAdapter = new DefaultLoadMoreAdapter(parent);
        addFooterToParent(parent);
    }

    private void addFooterToParent(ViewGroup parent) {
        View footerView = mLoadMoreAdapter.getView();
        ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
        if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutParams;
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            parent.addView(footerView, params);
        } else if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
            params.gravity = Gravity.BOTTOM;
            parent.addView(footerView, params);
        } else {
            throw new IllegalArgumentException("parent must be RelativeLayout or FrameLayout");
        }
    }

    public void setLoadMoreAdapter(ILoadMoreAdapter loadMoreAdapter) {
        mLoadMoreAdapter = loadMoreAdapter;
    }

    public void onActionDown() {
        mScrollY = 0;
    }

    public void onScrollToLoadMore(float dy) {
        mIsHandlingLoadMore = true;
        //dy > 0
        dy = fixDyIfNeeded(dy);
        mScrollY += dy;
        Log.d(TAG, "onScrollToLoadMore, dy: " + dy + "  scrollY: " + mScrollY);
        if (mCanLoadMore) {
            MLog.info(TAG, "show load more");
            mLoadMoreAdapter.showLoading();
        } else {
            MLog.info(TAG, "show no more data");
            mLoadMoreAdapter.showNoMore();
        }
        mCallback.onScroll(dy);
    }

    private float fixDyIfNeeded(float dy) {
        if (mScrollY + dy > mLoadMoreAdapter.getMaxHeight()) { //往上不能大于maxHeight
            dy = mLoadMoreAdapter.getMaxHeight() - mScrollY;
        } else if (mScrollY + dy < mLoadMoreAdapter.getMinHeight()) { // 往下不能小于minHeight
            dy = mLoadMoreAdapter.getMinHeight() - mScrollY;
        }
        return dy;
    }

    public void onActionUp() {
        MLog.info(TAG, "onActionUp");
        if (!mLoadMoreAdapter.isLoading()) {
            mIsHandlingLoadMore = false;
        }
        float scrollY = mScrollY;
        if (scrollY >= mLoadMoreAdapter.getMinHeight() && mCanLoadMore) {
            MLog.info(TAG, "onActionUp, load more");
            mCallback.scrollToWithAnimation(-mLoadMoreAdapter.getMinHeight());
            mCallback.onLoadMore();
        } else {
            MLog.info(TAG, "onActionUp, restore layout");
            completeLoadMore(false, false);
        }
    }

    public boolean isHandlingLoadMore() {
        return mIsHandlingLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    public void completeLoadMore(boolean hasMoreData, boolean isPreload) {
        mIsHandlingLoadMore = false;
        mLoadMoreAdapter.hide();
        MLog.info(TAG, "completeLoadMore, hasMoreData: %b, isPreload: %b", hasMoreData, isPreload);
        if (!isPreload) {
            if (hasMoreData) {
                mCallback.scrollToNext();
            } else {
                mCallback.scrollToWithAnimation(0);
            }
        } else {
            MLog.info(TAG, "completeLoadMore, is pre load, do nothing");
        }
    }

    public interface Callback {
        void onLoadMore();

        void scrollToWithAnimation(float dy);

        void scrollToNext();

        void onScroll(float dy);
    }


}
