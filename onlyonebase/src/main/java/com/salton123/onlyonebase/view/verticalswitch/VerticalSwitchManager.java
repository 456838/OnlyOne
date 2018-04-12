package com.salton123.onlyonebase.view.verticalswitch;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.salton123.onlyonebase.view.verticalswitch.animation.AnimParam;
import com.salton123.onlyonebase.view.verticalswitch.animation.IMoveAnimation;
import com.salton123.onlyonebase.view.verticalswitch.animation.MoveAnimCallback;
import com.salton123.onlyonebase.view.verticalswitch.animation.impl.ObjectAnimatorMoveAnimation;
import com.salton123.onlyonebase.view.verticalswitch.loadmore.LoadMoreHandler;
import com.salton123.onlyonebase.view.verticalswitch.preload.PreLoadHelper;
import com.salton123.onlyonebase.view.verticalswitch.refresh.RefreshHandler;
import com.salton123.onlyonebase.view.verticalswitch.refresh.SimpleRefreshCallback;
import com.salton123.util.log.FP;
import com.salton123.util.log.MLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动，切换，切换成功->显示cover->恢复其他两位置，开始播放，播放成功，隐藏view
 * Created by ZZB on 2017/8/7.
 */

public class VerticalSwitchManager<T> {
    private static final String TAG = "VerticalSwitchManager";
    private static final int MOVE_TRANSLATE_DURATION_MS = 300;
    private static final int RESTORE_TRANSLATE_DURATION_MS = 50;
    //position(0, -H), (0,0), (0, H)
    private static final int POS_BOTTOM = 0;
    private static final int POS_CENTER = 1;
    private static final int POS_TOP = 2;
    private final int mOnScrollTriggerMoveDistance;
    private final int mOnFlingTriggerMoveDistance;
    private final boolean mEnableOnFlingMove = false;
    private static final float[] POSITIONS = new float[3];
    private ViewSwitchAdapter mAdapter;
    private List<ScrollItem<T>> mItems = new ArrayList<>();
    private ScrollItem<T> mCenterItem;
    private GestureDetectorCompat mGestureDetector;
    private ViewGroup mRootLayout;
    private Context mContext;
    private int mScreenHeight;
    private float mStartY;
    private boolean mIsInAnimation;
    private int mCenterDataPosition;
    private Callback mCallback;
    private List<View> mViewsToMoveWithCenterView = new ArrayList<>();
    private LoadMoreCallback mLoadMoreCallback;
    private LoadMoreHandler mLoadMoreHandler;
    private RefreshHandler mRefreshHandler;
    private IMoveAnimation mMoveAnimation;
    private MoveDirection mCurrentMoveDirection;
    private float mFlingVelocityY;
    private PreLoadHelper mPreLoadHelper;

    public VerticalSwitchManager(ViewGroup rootLayout, ViewSwitchAdapter adapter) {
        if (!(rootLayout instanceof FrameLayout || rootLayout instanceof RelativeLayout)) {
            throw new IllegalArgumentException("rootLayout must be FrameLayout or RelativeLayout");
        }
        mContext = rootLayout.getContext();
        mAdapter = adapter;
        mRootLayout = rootLayout;
        mScreenHeight = getScreenHeight(mContext);
        mOnScrollTriggerMoveDistance = mScreenHeight / 7;
        mOnFlingTriggerMoveDistance = mScreenHeight / 12;
        POSITIONS[POS_BOTTOM] = -mScreenHeight;
        POSITIONS[POS_CENTER] = 0;
        POSITIONS[POS_TOP] = mScreenHeight;
        initDetector();
        initViews();
        initAnimation();
        initPreloadHelper();
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return by Hankkin at:2015-10-07 21:16:13
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private void initPreloadHelper() {
        mPreLoadHelper = new PreLoadHelper();
        mPreLoadHelper.setCallback(new PreLoadHelper.Callback() {
            @Override
            public void doLoadMore(boolean isPreload) {
                if (mLoadMoreCallback != null) {
                    mLoadMoreCallback.onLoadMore();
                }
            }
        });
    }

    /**
     * 预加载参数
     *
     * @param leftCountToStartPreload 最后第几个开始预加载
     */
    public void initPreloadParam(int leftCountToStartPreload) {
        mPreLoadHelper.initPreloadParam(leftCountToStartPreload);
    }

    public void usePreload(boolean usePreload) {
        mPreLoadHelper.setUsePreLoad(usePreload);
    }

    private void initAnimation() {
        mMoveAnimation = new ObjectAnimatorMoveAnimation(mScreenHeight, new MoveAnimCallback() {
            @Override
            public void onAnimationEnd(AnimParam param) {
                VerticalSwitchManager.this.onAnimationEnd(param);
            }
        }, mItems, mViewsToMoveWithCenterView);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void addViewToMoveWithCenterView(View view) {
        mViewsToMoveWithCenterView.add(view);
    }

    private void initViews() {
        initLoadMoreHandler();
        initRefreshHandler();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mScreenHeight);
        for (int i = 0; i < 3; i++) {
            ViewSwitchAdapter.ViewHolder vh = mAdapter.createViewHolder(mRootLayout);
            View view = vh.itemView;
            mRootLayout.addView(view, params);
            mItems.add(new ScrollItem(view, i, vh));
        }
    }

    private void initRefreshHandler() {
        mRefreshHandler = new RefreshHandler(mRootLayout, new SimpleRefreshCallback() {
            @Override
            public void scrollToWithAnimation(float dy) {
                restoreLayout(dy);
            }

            @Override
            public void onScroll(float dy) {
                scrollChildViews(dy);
            }
        });
    }

    private void initLoadMoreHandler() {
        mLoadMoreHandler = new LoadMoreHandler(mRootLayout, new LoadMoreHandler.Callback() {
            @Override
            public void onLoadMore() {
                // if (mLoadMoreCallback != null) {
                //     mLoadMoreCallback.onLoadMore();
                // }
                mPreLoadHelper.onLoadMore();
            }

            @Override
            public void scrollToWithAnimation(float dy) {
                restoreLayout(dy);
            }

            @Override
            public void scrollToNext() {
                moveToNext();
            }

            @Override
            public void onScroll(float dy) {
                scrollChildViews(dy);
            }
        });
    }

    private void initDetector() {
        mGestureDetector = new GestureDetectorCompat(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                VerticalSwitchManager.this.onScroll(distanceY);
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                VerticalSwitchManager.this.onFling(velocityY);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void setData(List<T> data, T centerData) {
        if (data == null || data.indexOf(centerData) == -1) {
            MLog.error(TAG, "setData, illegal arguments");
            return;
        }
        mCurrentMoveDirection = MoveDirection.NONE;
        mCenterDataPosition = data.indexOf(centerData);
        mAdapter.setData(data);
        mPreLoadHelper.setListSize(getVideoListCount());
        updateViewsPositionAndData(true, true);
    }

    public void addData(List<T> videos) {
        if (FP.empty(videos)) {
            return;
        }
        mCurrentMoveDirection = MoveDirection.NONE;
        mAdapter.addData(videos);
        mPreLoadHelper.setListSize(getVideoListCount());
        ScrollItem<T> centerItem = getCenterItem();
        if (centerItem != null) {
            if (mAdapter.getCount() > centerItem.dataPosition && centerItem.dataPosition >= 0) {
                mCenterDataPosition = centerItem.dataPosition;
                updateViewsPositionAndData(true, false);
            }
        }

    }

    private void onAnimationEnd(AnimParam param) {
        MLog.info(TAG, "onAnimationEnd");
        mIsInAnimation = false;
        if (!param.isLoadingMore) {
            updateViewsPositionAndData(false, param.callOnSelectedOnAnimationEnd);
        }
        unInterceptTouchEvent();
    }

    /**
     * 遍历各个view，如果位置不对，要更新位置，还有设置view的数据
     *
     * @param callOnSelected false，这里不会回调onSelected出去(只是位置恢复，不算选中)
     */
    private void updateViewsPositionAndData(boolean forceUpdateData, boolean callOnSelected) {
        final ScrollItems scrollItems = new ScrollItems();
        for (ScrollItem item : mItems) {
            float targetY = getViewYByIndex(item.viewIndex);
            updateDataPosition(item, targetY);
            View view = item.mView;
            float y = view.getY();
            item.viewY = targetY;
            if (y != targetY) {
                Log.d(TAG, "updateView, diff position, update position and data, y:" + targetY);
                view.setTranslationY(targetY);
                bindView(item, view);
            } else if (forceUpdateData) { //位置一样也更新数据
                Log.d(TAG, "updateView, same position, forceUpdateData");
                bindView(item, view);
            } else {
                Log.d(TAG, "updateView, same position, skip");
            }
//            Log.d(TAG, "updateView, data:" + item.toString());
            if (targetY == POSITIONS[POS_TOP]) {
                scrollItems.preItem = item;
            } else if (targetY == POSITIONS[POS_BOTTOM]) {
                scrollItems.nextItem = item;
            } else if (targetY == POSITIONS[POS_CENTER]) {
                scrollItems.selectedItem = item;
                mCenterItem = item;
            }
        }

        if (!callOnSelected) {
            return;
        }

        mRootLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCallback != null) {
                    mCallback.onSelected(scrollItems.selectedItem, scrollItems.nextItem,
                            scrollItems.preItem, mCurrentMoveDirection);
                    mPreLoadHelper.onScroll(mCenterDataPosition);
                }
            }
        }, 50);
    }

    private void bindView(ScrollItem item, View view) {
        int itemPos = item.dataPosition;
        if (itemPos >= 0 && itemPos < mAdapter.getCount()) {
            view.setVisibility(View.VISIBLE);
            item.data = mAdapter.getItemData(item.dataPosition);
            mAdapter.onBindView(item.viewHolder, item.dataPosition);
        } else {
            view.setVisibility(View.INVISIBLE);
            Log.d(TAG, "skip bind view, invalid position:" + itemPos);
        }
    }

    private void updateDataPosition(ScrollItem item, float targetY) {
//        Log.d(TAG, "updateDataPosition, currentPos:" + mCenterDataPosition);
        if (targetY == POSITIONS[POS_BOTTOM]) {
            item.dataPosition = mCenterDataPosition - 1;
        } else if (targetY == POSITIONS[POS_CENTER]) {
            item.dataPosition = mCenterDataPosition;
        } else if (targetY == POSITIONS[POS_TOP]) {
            item.dataPosition = mCenterDataPosition + 1;
        }
//        Log.d(TAG, "updateDataPosition, centerDataPos:" + mCenterDataPosition + " updated data: " + item.toString());
    }

    public void onDispatchTouchEvent(MotionEvent ev) {
        mGestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(ev);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(ev);
                break;
            default:
                break;
        }
    }


    private void onFling(float velocityY) {
        mFlingVelocityY = velocityY;
    }

    private void onScroll(float distanceY) {
        interceptTouchEvent();
        if (!canScrollOnScroll(distanceY)
                || mLoadMoreHandler.isHandlingLoadMore() || mRefreshHandler.isHandlingRefresh()) {
            if (distanceY > 0) {
                if (mRefreshHandler.isHandlingRefresh()) {
                    handleRefresh(distanceY);
                } else {
                    handleLoadMore(distanceY);
                }
            } else if (distanceY < 0) {
                if (mLoadMoreHandler.isHandlingLoadMore()) {
                    handleLoadMore(distanceY);
                } else {
                    handleRefresh(distanceY);
                }
            }
            return;
        }
        distanceY = fixDyOnFirstOrLastItemIfNeeded(distanceY);
        scrollChildViews(distanceY);
    }

    private void scrollChildViews(float distanceY) {
        for (ScrollItem item : mItems) {
            View view = item.mView;
            float tY = (-distanceY + view.getY());
            view.setTranslationY(tY);
        }
        if (mCallback != null) {
            mCallback.onScroll(distanceY);
        }
    }

    private void interceptTouchEvent() {
        if (mRootLayout instanceof SmallVideoTouchInterceptRelativeLayout) {
            ((SmallVideoTouchInterceptRelativeLayout) mRootLayout).setInterceptTouch(true);
        }
    }

    private void unInterceptTouchEvent() {
        if (mRootLayout instanceof SmallVideoTouchInterceptRelativeLayout) {
            ((SmallVideoTouchInterceptRelativeLayout) mRootLayout).setInterceptTouch(false);
        }
    }

    private void handleRefresh(float distanceY) {
        if (mIsInAnimation || (mCallback != null && !mCallback.canScroll())) {
            return;
        }
        if (distanceY < 0 || mRefreshHandler.isHandlingRefresh()) {
            mRefreshHandler.onScrollToRefresh(distanceY);
        }
    }

    private void handleLoadMore(float distanceY) {
        if (mIsInAnimation || (mCallback != null && !mCallback.canScroll())) {
            return;
        }
        if (distanceY > 0 || mLoadMoreHandler.isHandlingLoadMore()) {
            mLoadMoreHandler.onScrollToLoadMore(distanceY);
        }
    }

    //解决第一个item上滑再下滑，以及最后一个item下滑再上滑的bug
    private float fixDyOnFirstOrLastItemIfNeeded(float distanceY) {
        boolean moveToPre = distanceY < 0;
        boolean moveToNext = distanceY > 0;
        boolean isFirstItem = mCenterDataPosition == 0;
        boolean isLastItem = mCenterDataPosition == mAdapter.getCount() - 1;
        ScrollItem centerItem = getCenterItem();
        if (centerItem != null) {
            float viewY = centerItem.mView.getY();
            if (isFirstItem && moveToPre) {
                if (viewY - distanceY > 0) {
                    distanceY = viewY;
                }
            } else if (isLastItem && moveToNext) {
                if (viewY - distanceY < 0) {
                    distanceY = viewY;
                }
            }
        }
        return distanceY;
    }

    private void onActionDown(MotionEvent ev) {
        SameFingerChecker.onDownEvent(ev);
        mStartY = ev.getY();
        mRefreshHandler.onActionDown();
        mLoadMoreHandler.onActionDown();
        mFlingVelocityY = 0;
    }

    private void onActionUp(MotionEvent ev) {
        float distanceY = ev.getY() - mStartY;
        if (mRefreshHandler.isHandlingRefresh()) {
            mRefreshHandler.onActionUp();
            return;
        }
        if (mLoadMoreHandler.isHandlingLoadMore()) {
            mLoadMoreHandler.onActionUp();
            return;
        }
        if (!canScrollOnActionUp(distanceY)) {
            unInterceptTouchEvent();
            return;
        }
        boolean triggerRestoreOnScroll = Math.abs(distanceY) < mOnScrollTriggerMoveDistance; //滑动没有触发上下滑
        boolean triggerMoveOnFling = mEnableOnFlingMove && Math.abs(mFlingVelocityY) >= mOnFlingTriggerMoveDistance && triggerRestoreOnScroll; //当onScroll没有触发上下滑的时候，检测onFling
        if (triggerMoveOnFling) {
            handleMoveOnFling();
        } else {
            handleMoveOnScroll(SameFingerChecker.checkIsTheSameFinger(ev), distanceY);
        }
    }

    private void handleMoveOnScroll(boolean downUpTheSameFinger, float distanceY) {
        boolean triggerRestoreOnScroll = Math.abs(distanceY) < mOnScrollTriggerMoveDistance; //滑动没有触发上下滑
        if (!downUpTheSameFinger) {
            restoreLayout(0);
            return;
        }
        if (triggerRestoreOnScroll) {
            restoreLayout(0);
        } else if (distanceY > 0) {
            if (mCenterDataPosition == 0) {
                MLog.error(TAG, "data position is zero, should not move to pre");
                restoreLayout(0);
            } else {
                moveToPre();
            }
        } else {
            moveToNext();
        }
    }

    private void handleMoveOnFling() {
        if (mFlingVelocityY > 0 && mCenterDataPosition > 0) {
            moveToPre();
        } else if (mFlingVelocityY < 0 && mCenterDataPosition < mAdapter.getCount() - 1) {
            moveToNext();
        } else {
            restoreLayout(0);
        }
    }

    public void moveToNext() {
        mCurrentMoveDirection = MoveDirection.MOVE_TO_NEXT;
        mCenterDataPosition++;
        mIsInAnimation = true;
        mMoveAnimation.moveToNext(new AnimParam(MOVE_TRANSLATE_DURATION_MS));
    }

    private void moveToPre() {
        mCurrentMoveDirection = MoveDirection.MOVE_TO_PRE;
        mCenterDataPosition--;
        mIsInAnimation = true;
        mMoveAnimation.moveToPre(new AnimParam(MOVE_TRANSLATE_DURATION_MS));
    }

    private void restoreLayout(float restoreTo) {
        mCurrentMoveDirection = MoveDirection.NONE;
        boolean isLoadingMore = restoreTo != 0;
        mIsInAnimation = true;
        mMoveAnimation.restoreLayout(new AnimParam(RESTORE_TRANSLATE_DURATION_MS, false,
                isLoadingMore, restoreTo));
    }

    private float getViewYByIndex(int index) {
        while (index < 0) {
            index += 3;
        }
        return POSITIONS[Math.abs(index % 3)];
    }

    private boolean canScrollOnScroll(float distanceY) {
        return canScroll(distanceY, false);
    }

    private boolean canScrollOnActionUp(float distanceY) {
        return canScroll(distanceY, true);
    }

    private boolean canScroll(float distanceY, boolean isCallOnActionUp) {
        if (mIsInAnimation) {
            return false;
        }
        if (mAdapter.getCount() <= 1 || (mCallback != null && !mCallback.canScroll())) {
            return false;
        }
        boolean moveToPre = isCallOnActionUp ? distanceY > 0 : distanceY < 0;
        boolean moveToNext = isCallOnActionUp ? distanceY < 0 : distanceY > 0;
        ScrollItem centerItem = getCenterItem();
        if (centerItem != null) {
            float centerViewY = centerItem.mView.getY();
            if (moveToPre) { //move to pre
                if (mCenterDataPosition == 0 && centerViewY == 0) {
                    Log.d(TAG, "can not move to pre");
                    return false;
                }
            } else if (moveToNext) { //move to next
                if (mCenterDataPosition == mAdapter.getCount() - 1 && centerViewY == 0) {
                    Log.d(TAG, "can not move to next");
                    return false;
                }
            }
        }

        //adapter.getCount <= 1, return false
        //再判断位置，第几个，第一个不能上，最后一个不能下
        //还要再结合当前view的位置判断，比如当前view已经移动了，还是能上移的，直到恢复原位为止
        return !mIsInAnimation;
    }

    public int getVideoListCount() {
        return mAdapter.getCount();
    }

    @Nullable
    private ScrollItem<T> getCenterItem() {
        return mCenterItem;
    }

    public void setLoadMoreCallback(LoadMoreCallback loadMoreCallback) {
        mLoadMoreCallback = loadMoreCallback;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        mLoadMoreHandler.setCanLoadMore(canLoadMore);
        mPreLoadHelper.setCanLoadMore(canLoadMore);
    }

    public void setCanRefresh(boolean canRefresh) {
        mRefreshHandler.setCanRefresh(canRefresh);
    }

    public void completeLoadMore(boolean hasMoreData) {
        mLoadMoreHandler.completeLoadMore(hasMoreData, mPreLoadHelper.isPreLoad());
        mPreLoadHelper.completeLoadMore();
    }

    public void completeRefresh() {
        mRefreshHandler.completeRefresh();
    }

    public interface Callback<T> {
        void onSelected(@Nullable ScrollItem<T> selectedItem, @Nullable ScrollItem<T> nextItem,
                        @Nullable ScrollItem<T> preItem, MoveDirection moveDirection);

        void onScroll(float dy);

        boolean canScroll();
    }

    public interface LoadMoreCallback {
        void onLoadMore();
    }

    private static class ScrollItems<T> {
        ScrollItem<T> preItem, selectedItem, nextItem;
    }
}
