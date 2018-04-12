package com.salton123.onlyonebase.view.verticalswitch.loadmore;

import android.view.View;

/**
 * Created by ZZB on 2017/8/31.
 */

public interface ILoadMoreAdapter {


    int getMinHeight();

    int getMaxHeight();

    View getView();

    void showLoading();

    void showNoMore();

    void hide();

    boolean isLoading();
}
