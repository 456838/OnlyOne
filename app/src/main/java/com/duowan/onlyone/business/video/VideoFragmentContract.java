package com.duowan.onlyone.business.video;

import com.duowan.onlyone.model.entity.VideoListBean;
import com.salton123.mvp.presenter.BasePresenter;
import com.salton123.mvp.view.BaseView;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/7/18 11:09
 * Time: 11:09
 * Description:
 */
public interface VideoFragmentContract {

    interface View extends BaseView {
        void showLoading();

        void dismissLoading();

        void showData(VideoListBean datalist);
        void showError(String errorMsg);
    }

    interface Presenter extends BasePresenter<View> {
        void getData(String date);
    }
}
