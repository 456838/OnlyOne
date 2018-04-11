package com.salton123.onlyonebase.view.widget.verticalswitch.animation.impl;

import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.salton123.onlyonebase.view.widget.verticalswitch.ScrollItem;
import com.salton123.onlyonebase.view.widget.verticalswitch.animation.AnimParam;
import com.salton123.onlyonebase.view.widget.verticalswitch.animation.IMoveAnimation;
import com.salton123.onlyonebase.view.widget.verticalswitch.animation.MoveAnimCallback;
import com.salton123.onlyonebase.view.widget.verticalswitch.animation.SimpleAnimatorListener;

import java.util.List;

/**
 * Created by ZZB on 2017/9/2.
 */

public class ViewPropertyMoveAnimation implements IMoveAnimation {

    private int mScreenHeight;
    private MoveAnimCallback mCallback;

    private List<ScrollItem> scrollItems;
    private List<View> viewsToMoveWithCenterView;

    public ViewPropertyMoveAnimation(int screenHeight, MoveAnimCallback callback, List scrollItems, List<View> viewsToMoveWithCenterView) {
        mScreenHeight = screenHeight;
        mCallback = callback;
        this.scrollItems = scrollItems;
        this.viewsToMoveWithCenterView = viewsToMoveWithCenterView;
    }

    @Override
    public void moveToPre(final AnimParam param) {
        ViewPropertyAnimator lastAnimator = null;
        for (ScrollItem item : scrollItems) {
            item.viewY += mScreenHeight;
            item.viewIndex++;
            lastAnimator = item.mView.animate().translationY(item.viewY);
        }
        for (View view : viewsToMoveWithCenterView) {
            lastAnimator = view.animate().translationY(mScreenHeight);
        }
        onAnimationEnd(param, lastAnimator);

    }

    private void onAnimationEnd(final AnimParam param, ViewPropertyAnimator lastAnimator) {
        if (lastAnimator != null) {
            lastAnimator.setListener(new SimpleAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    mCallback.onAnimationEnd(param);
                }
            });
        }
    }

    @Override
    public void moveToNext(AnimParam param) {
        ViewPropertyAnimator lastAnimator = null;
        for (ScrollItem item : scrollItems) {
            item.viewY -= mScreenHeight;
            item.viewIndex--;
            lastAnimator = item.mView.animate().translationY(item.viewY);
        }
        for (View view : viewsToMoveWithCenterView) {
            lastAnimator = view.animate().translationY(-mScreenHeight);
        }
        onAnimationEnd(param, lastAnimator);
    }

    @Override
    public void restoreLayout(AnimParam param) {
        ViewPropertyAnimator lastAnimator = null;
        for (ScrollItem item : scrollItems) {
            item.viewY += param.extraViewY;
            lastAnimator = item.mView.animate().translationY(item.viewY);
        }
        for (View view : viewsToMoveWithCenterView) {
            lastAnimator = view.animate().translationY(param.extraViewY);
        }
        onAnimationEnd(param, lastAnimator);
    }

}
