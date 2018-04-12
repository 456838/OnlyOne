package com.duowan.onlyone.fm.video;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.duowan.onlyone.R;
import com.duowan.onlyone.model.entity.kaiyan.DataBean;
import com.duowan.onlyone.model.entity.utils.DateUtil;
import com.salton123.base.BaseSupportSwipeBackFragment;
import com.salton123.onlyonebase.ImageLoader;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerController;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;
import com.xiao.nicevideoplayer.TxVideoPlayerController;



public class VideoDetailFragment extends BaseSupportSwipeBackFragment {

    TextView title;
    TextView type;
    TextView description;
    TextView tv_title;
    NiceVideoPlayer videoplayer;
    NiceVideoPlayerController mPlayerController;
    private DataBean dataBean;

    @Override
    public int GetLayout() {
        return R.layout.video_home_detail_fragment;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
        dataBean = getArguments().getParcelable(ARG_ITEM);

    }

    @Override
    public void InitViewAndData() {
        title = f(R.id.title);
        type = f(R.id.type);
        description = f(R.id.description);
        videoplayer = f(R.id.videoplayer);
        tv_title = f(R.id.tv_title);
        mPlayerController = new TxVideoPlayerController(this.activity());
        videoplayer.setController(mPlayerController);
        videoplayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE);
        ImageLoader.Companion.display(mPlayerController.imageView(), dataBean.getCover().getDetail());
        mPlayerController.setTitle(dataBean.getTitle());
        videoplayer.setUp(dataBean.getPlayUrl(), null);
        title.setText(dataBean.getTitle());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#").append(dataBean.getCategory())
                .append(" ")
                .append(" / ")
                .append(" ")
                .append(DateUtil.formatTime2(dataBean.getDuration()));
        type.setText(stringBuilder.toString());
        description.setText(dataBean.getDescription());
        tv_title.setText(dataBean.getCategory());
    }

    @Override
    public void InitListener() {
        f(R.id.tv_title_back).setVisibility(View.VISIBLE);
        f(R.id.tv_title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        NiceVideoPlayerManager.instance().suspendNiceVideoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }
}
