package com.salton123.onlyonebase.view.verticalswitch.preload;

import com.salton123.util.log.MLog;

/**
 * Created by ZZB on 2017/10/9.
 */

public class PreLoadHelper {
    private static final String TAG = "PreLoadHelper";
    private int mCurrentListSize;
    private int mLeftCountToStartPreload; //最后第几个开始预加载
    private boolean mIsLoading;
    private boolean mCanLoadMore;
    private Callback mCallback;
    private boolean mIsPreLoad;
    private boolean mDoPreloadOnListSizeLessThenStartCount = true; //数据长度少于开始预加载的数字，也执行预加载
    private boolean mUsePreLoad = true;

    public boolean isPreLoad() {
        return mIsPreLoad;
    }

    public interface Callback {
        void doLoadMore(boolean isPreLoad);
    }

    /**
     * 预加载参数
     *
     * @param leftCountToStartPreload 最后第几个开始预加载
     */
    public void initPreloadParam(int leftCountToStartPreload) {
        mLeftCountToStartPreload = leftCountToStartPreload;
    }

    public void setUsePreLoad(boolean usePreLoad) {
        mUsePreLoad = usePreLoad;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void setListSize(int listSize) {
        mCurrentListSize = listSize;
    }

    public void onScroll(int index) {
        if (!mUsePreLoad) {
            return;
        }
        if (mIsLoading) {
            return;
        }
        int leftCount = mCurrentListSize - index;
        if ((mDoPreloadOnListSizeLessThenStartCount && mCurrentListSize >= mLeftCountToStartPreload) && leftCount <= mLeftCountToStartPreload) {
            MLog.info(TAG, "try to pre load, index: %d, listSize: %d, leftCount: %d", index, mCurrentListSize, leftCount);
            doLoadMore(true);
        }
    }

    public void onLoadMore() {
        if (mIsLoading) {
            return;
        }
        doLoadMore(false);
    }

    private void doLoadMore(boolean isPreLoad) {
        if (mCallback == null || !mCanLoadMore || mIsLoading) {
            return;
        }
        mIsLoading = true;
        mIsPreLoad = isPreLoad;
        mCallback.doLoadMore(isPreLoad);
        MLog.info(TAG, "doLoadMore, isPreLoad:" + isPreLoad);
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mCanLoadMore = canLoadMore;
    }

    public void completeLoadMore() {
        mIsLoading = false;
        mIsPreLoad = false;
    }
}
