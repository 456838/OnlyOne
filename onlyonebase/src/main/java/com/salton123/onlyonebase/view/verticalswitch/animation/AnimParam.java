package com.salton123.onlyonebase.view.verticalswitch.animation;

/**
 * Created by ZZB on 2017/9/2.
 */

public class AnimParam {

    public long animDuration;
    public boolean callOnSelectedOnAnimationEnd = true; //true的话，动画结束，没有onSelected的回调出去
    public boolean isLoadingMore;
    public float extraViewY;

    public AnimParam(long animDuration) {
        this.animDuration = animDuration;
    }

    public AnimParam(long animDuration, boolean callOnSelectedOnAnimationEnd, boolean isLoadingMore, float extraViewY) {
        this.animDuration = animDuration;
        this.callOnSelectedOnAnimationEnd = callOnSelectedOnAnimationEnd;
        this.isLoadingMore = isLoadingMore;
        this.extraViewY = extraViewY;
    }
}
