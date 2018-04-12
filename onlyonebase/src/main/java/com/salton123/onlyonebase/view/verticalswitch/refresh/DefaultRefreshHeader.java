package com.salton123.onlyonebase.view.verticalswitch.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.salton123.onlyonebase.R;
import com.salton123.util.ScreenUtils;


/**
 * Created by ZZB on 2017/9/27.
 */

public class DefaultRefreshHeader implements IRefreshHeader {

    private int mMaxHeight, mMinHeight;
    private View mContentView;
    private boolean mIsShowingRefresh;
    private boolean mIsShowingNoRefresh;
    private ProgressBar mProgressBar;

    public DefaultRefreshHeader(ViewGroup parent) {
        Context context = parent.getContext();
        mMaxHeight = ScreenUtils.dpToPxInt(context, 40);
        mMinHeight = ScreenUtils.dpToPxInt(context, 30);
        createView(parent);
    }

    private void createView(ViewGroup parent) {
        mContentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_small_video_refresh_header, parent, false);
        mProgressBar = mContentView.findViewById(R.id.pb_loading);
        mContentView.setVisibility(View.GONE);
    }

    @Override
    public int getMinHeight() {
        return mMinHeight;
    }

    @Override
    public int getMaxHeight() {
        return mMaxHeight;
    }

    @Override
    public View getView() {
        return mContentView;
    }

    @Override
    public void showLoading() {
        if (mIsShowingRefresh) {
            return;
        }
        mContentView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mIsShowingRefresh = true;
        mIsShowingNoRefresh = false;
    }

    @Override
    public void showNoRefresh() {
        if (mIsShowingNoRefresh) {
            return;
        }
        mContentView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mIsShowingNoRefresh = true;
        mIsShowingRefresh = false;
    }

    @Override
    public void hide() {
        mContentView.setVisibility(View.GONE);
        mIsShowingRefresh = false;
        mIsShowingNoRefresh = false;
    }

    @Override
    public boolean isLoading() {
        return mIsShowingRefresh;
    }
}
