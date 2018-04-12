package com.salton123.onlyonebase.view.verticalswitch.animation;

/**
 * Created by ZZB on 2017/9/2.
 */

public interface IMoveAnimation {


    void moveToPre(AnimParam param);

    void moveToNext(AnimParam param);

    void restoreLayout(AnimParam param);
}
