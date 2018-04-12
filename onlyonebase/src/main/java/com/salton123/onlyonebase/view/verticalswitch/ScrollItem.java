package com.salton123.onlyonebase.view.verticalswitch;

import android.view.View;

/**
 * Created by ZZB on 2017/8/7.
 */

public class ScrollItem<T> {

    public View mView;
    public T data;
    public int viewIndex;
    public int dataPosition;
    public float viewY; //移动之前的viewY
    public ViewSwitchAdapter.ViewHolder viewHolder;

    public ScrollItem(View view, int viewIndex, ViewSwitchAdapter.ViewHolder vh) {
        mView = view;
        this.viewIndex = viewIndex;
        this.viewHolder = vh;
    }

    @Override
    public String toString() {
        return "ScrollItem{" +
                "mView=" + mView.getY() +
                ", data=" + data +
                ", viewIndex=" + viewIndex +
                ", dataPosition=" + dataPosition +
                '}';
    }
}
