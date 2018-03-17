package com.salton123.xm.business;

import com.salton123.mvp.presenter.RxPresenter;
import com.salton123.xm.wrapper.TrackUtil;

/**
 * User: newSalton@outlook.com
 * Date: 2017/10/18 19:04
 * ModifyTime: 19:04
 * Description:
 */
public class MusicPlayerPresenter extends RxPresenter<MusicPlayerContract.IView> implements MusicPlayerContract.IPresenter {

    @Override
    public void loadCachedTrackList() {
        mView.loadTrackList(TrackUtil.loadTrackList());
    }
}
