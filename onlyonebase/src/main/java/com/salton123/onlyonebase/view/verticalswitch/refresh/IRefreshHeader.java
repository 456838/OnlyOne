package com.salton123.onlyonebase.view.verticalswitch.refresh;

import android.view.View;

/**
 * Created by ZZB on 2017/9/26.
 */

public interface IRefreshHeader {


    int getMinHeight();

    int getMaxHeight();

    View getView();

    void showLoading();

    void showNoRefresh();

    void hide();

    boolean isLoading();

}
