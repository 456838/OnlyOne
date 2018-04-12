package com.salton123.onlyonebase.view.verticalswitch.loadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.salton123.onlyonebase.R;
import com.salton123.util.ScreenUtils;


/**
 * Created by ZZB on 2017/8/31.
 */

public class DefaultLoadMoreAdapter implements ILoadMoreAdapter {
    private int mMaxHeight, mMinHeight;
    private View mContentView;
    private TextView mTvContent;
    private boolean mIsShowingLoading;
    private boolean mIsShowingNoMore;
    private ProgressBar mProgressBar;

    public DefaultLoadMoreAdapter(ViewGroup parent) {
        Context context = parent.getContext();
        mMaxHeight = ScreenUtils.dpToPxInt(context,100);
        mMinHeight = ScreenUtils.dpToPxInt(context,50);
        createView(parent);
    }

    private void createView(ViewGroup parent) {
        mContentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.default_small_video_footer, parent, false);
        mTvContent = (TextView) mContentView.findViewById(R.id.tv_load_more_content);
        mProgressBar = (ProgressBar) mContentView.findViewById(R.id.pb_loading);
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
        if (mIsShowingLoading) {
            return;
        }
        mContentView.setVisibility(View.VISIBLE);
        mTvContent.setText(R.string.small_video_footer_loading);
        mProgressBar.setVisibility(View.VISIBLE);
        mIsShowingLoading = true;
        mIsShowingNoMore = false;
    }

    @Override
    public void showNoMore() {
        if (mIsShowingNoMore) {
            return;
        }
        mContentView.setVisibility(View.VISIBLE);
        mTvContent.setText(R.string.small_video_footer_no_more);
        mProgressBar.setVisibility(View.GONE);
        mIsShowingNoMore = true;
        mIsShowingLoading = false;
    }

    @Override
    public boolean isLoading() {
        return mIsShowingLoading;
    }

    @Override
    public void hide() {
        mContentView.setVisibility(View.INVISIBLE);
        mIsShowingNoMore = false;
        mIsShowingLoading = false;
    }
}
