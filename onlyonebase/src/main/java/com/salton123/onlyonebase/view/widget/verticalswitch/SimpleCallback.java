package com.salton123.onlyonebase.view.widget.verticalswitch;

import android.support.annotation.Nullable;

/**
 * Created by ZZB on 2017/8/18.
 */

public class SimpleCallback<T> implements VerticalSwitchManager.Callback<T> {

    @Override
    public void onSelected(@Nullable ScrollItem<T> selectedItem, @Nullable ScrollItem<T> nextItem,
                           @Nullable ScrollItem<T> preItem, MoveDirection moveDirection) {

    }

    @Override
    public void onScroll(float dy) {

    }

    @Override
    public boolean canScroll() {
        return true;
    }
}
