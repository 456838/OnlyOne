package com.duowan.onlyone.func.video.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.duowan.onlyone.R;
import com.duowan.onlyone.func.video.model.SmallVideoPlayInfo;
import com.salton123.util.ViewUtils;
import com.yy.mobile.memoryrecycle.views.YYImageView;

/**
 * User: newSalton@outlook.com
 * Date: 2017/12/23 22:22
 * ModifyTime: 22:22
 * Description:
 */
public class SmallVideoPlayView extends RelativeLayout {

    private static final String TAG = "SmallVideoPlayView";
    private SmallVideoPlayInfo mPlayInfo;

    YYImageView imgVideoSnapshot;

    public SmallVideoPlayView(Context context) {
        super(context);
        init(context);
    }

    public SmallVideoPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SmallVideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_small_video_play_v2, this);
        imgVideoSnapshot = ViewUtils.f(this, R.id.imgVideoSnapshot);
    }

    public void updateData(SmallVideoPlayInfo info, boolean b) {
        mPlayInfo = info;
        imgVideoSnapshot.setBackgroundResource(info.snapshotUrl);
    }

    public SmallVideoPlayInfo getPlayInfo() {
        return mPlayInfo;
    }


    public void hideCoverView() {
        imgVideoSnapshot.setVisibility(INVISIBLE);
//        imgVideoSnapshot.setImageDrawableToNull();
    }

    public void showCoverView(float dpi) {
        imgVideoSnapshot.setVisibility(VISIBLE);
    }

    public void onUnSelected() {
        showCoverView(0);
    }

    public void onSelected() {

    }
}
