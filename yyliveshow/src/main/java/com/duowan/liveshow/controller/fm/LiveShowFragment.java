package com.duowan.liveshow.controller.fm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.duowan.liveshow.R;
import com.duowan.liveshow.controller.ChannelVideoController;
import com.duowan.liveshow.view.widget.DoubleLayout;
import com.duowan.mobile.mediaproxy.YVideoViewLayout;
import com.salton123.base.BaseSupportSwipeBackFragment;
import com.salton123.util.ScreenRotationUtils;
import com.yy.live.model.engine.YYEngine;
import com.yy.live.model.proxy.MediaProxy;
import com.yyproto.base.YYSdkService;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/15 10:42
 * Time: 10:42
 * Description:
 */
public class LiveShowFragment extends BaseSupportSwipeBackFragment {
    YVideoViewLayout yvLayout, yvLayout2;
    DoubleLayout double_layout;
    private FloatingActionButton btn_change_orientation ;

    ChannelVideoController mChannelVideoController;

    public long topSid;
    public long subSid;
    public long mUid = YYEngine.GetCurrentUid();
    MediaProxy mMediaImpl = MediaProxy.getInstance();

    @Override
    public int GetLayout() {
        return R.layout.fm_live_show;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
        topSid = mMediaImpl.currentChannelInfo.topSid;
        subSid = mMediaImpl.currentChannelInfo.subSid;
        mUid = YYEngine.GetCurrentUid();
        YYEngine.JoinChannel(topSid, subSid, null);     //进频道请求
    }

    @Override
    public void InitViewAndData() {
        yvLayout = f(R.id.yvLayout);
        yvLayout2 = f(R.id.yvLayout2);
        btn_change_orientation = f(R.id.btn_change_orientation);
        double_layout = f(R.id.double_layout);
        mChannelVideoController = new ChannelVideoController(_mActivity, YYEngine.getInstance().mIMediaVideo, yvLayout, yvLayout2, double_layout);
    }

    @Override
    public void InitListener() {
        btn_change_orientation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (portraitFlag){
                    ScreenRotationUtils.setScreenLandscape(_mActivity);
                }else{
                    ScreenRotationUtils.setScreenPortrait(_mActivity);
                }
                portraitFlag =!portraitFlag;
            }
        });
    }

    private boolean portraitFlag ;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!portraitFlag){
            ScreenRotationUtils.setScreenPortrait(_mActivity);
        }

        if (YYEngine.HasActiveSess()) {
            YYEngine.LeaveChannel();
        }
        mChannelVideoController.destroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        Boolean isForeGround = YYSdkService.isForeGround(getContext());
        YYEngine.AppStatusReq(isForeGround);
        mChannelVideoController.resumeSubscribeVideo();
        mChannelVideoController.onResume();
    }


    @Override
    public void onStop() {
        mChannelVideoController.stopSubscribeVideo();
        Boolean isForeGround = YYSdkService.isForeGround(getContext());
        YYEngine.AppStatusReq(isForeGround);
        super.onStop();
    }

}
