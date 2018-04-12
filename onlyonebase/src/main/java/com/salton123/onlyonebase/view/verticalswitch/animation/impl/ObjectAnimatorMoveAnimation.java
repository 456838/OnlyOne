package com.salton123.onlyonebase.view.verticalswitch.animation.impl;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.salton123.onlyonebase.view.verticalswitch.ScrollItem;
import com.salton123.onlyonebase.view.verticalswitch.animation.AnimParam;
import com.salton123.onlyonebase.view.verticalswitch.animation.IMoveAnimation;
import com.salton123.onlyonebase.view.verticalswitch.animation.MoveAnimCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZZB on 2017/9/2.
 */

public class ObjectAnimatorMoveAnimation implements IMoveAnimation {

    private int mScreenHeight;
    private MoveAnimCallback mCallback;

    private List<ScrollItem> mItems;
    private List<View> viewsToMoveWithCenterView;
    private Interpolator mInterpolator = new DecelerateInterpolator();

    public ObjectAnimatorMoveAnimation(int screenHeight, MoveAnimCallback callback, List scrollItems, List<View> viewsToMoveWithCenterView) {
        mScreenHeight = screenHeight;
        mCallback = callback;
        this.mItems = scrollItems;
        this.viewsToMoveWithCenterView = viewsToMoveWithCenterView;
    }

    @Override
    public void moveToPre(AnimParam param) {
        List<Animator> animators = new ArrayList<>();
        for (ScrollItem item : mItems) {
            item.viewY += mScreenHeight;
            item.viewIndex++;
            animators.add(translateAnimator(item.mView, item.viewY));
        }
        for (View view : viewsToMoveWithCenterView) {
            animators.add(translateAnimator(view, mScreenHeight));
        }
        doTranslateAnimation(animators, param);

    }

    @Override
    public void moveToNext(AnimParam param) {
        List<Animator> animators = new ArrayList<>();
        for (ScrollItem item : mItems) {
            item.viewY -= mScreenHeight;
            item.viewIndex--;
            animators.add(translateAnimator(item.mView, item.viewY));
        }
        for (View view : viewsToMoveWithCenterView) {
            animators.add(translateAnimator(view, -mScreenHeight));
        }
        doTranslateAnimation(animators, param);

    }

    @Override
    public void restoreLayout(AnimParam param) {
        float restoreTo = param.extraViewY;
        List<Animator> animators = new ArrayList<>();
        for (ScrollItem item : mItems) {
            animators.add(translateAnimator(item.mView, item.viewY + restoreTo));
        }
        for (View view : viewsToMoveWithCenterView) {
            animators.add(translateAnimator(view, restoreTo));
        }
        doTranslateAnimation(animators, param);

    }

    private Animator translateAnimator(View view, float to) {
        Animator animator = ObjectAnimator
                .ofFloat(view, "translationY", to);
        animator.setInterpolator(mInterpolator);
        return animator;
    }

    private void doTranslateAnimation(List<Animator> animators, final AnimParam param) {
//        mIsInAnimation = true;
        AnimatorSet set = new AnimatorSet();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mCallback.onAnimationEnd(param);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mCallback.onAnimationEnd(param);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        set.playTogether(animators);
        set.setDuration(param.animDuration);
        set.start();
    }
}
