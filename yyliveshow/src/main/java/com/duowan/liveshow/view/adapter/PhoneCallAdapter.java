package com.duowan.liveshow.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duowan.liveshow.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.salton123.common.image.FrescoImageLoader;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/24 18:17
 * Time: 18:17
 * Description:
 */
public class PhoneCallAdapter extends BGARecyclerViewAdapter<MicTopInfo> {

    public PhoneCallAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_item_caller);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, MicTopInfo model) {
        SimpleDraweeView sdv_actor_header = helper.getView(R.id.sdv_actor_header);
        helper.setText(R.id.tv_actor_name, model.name);
        FrescoImageLoader.display(sdv_actor_header, model.portraitUrl);
        View view_speak_status = helper.getView(R.id.iv_speaker_off);
        if (getItem(position).isSpeaking) {//正在说话
            view_speak_status.setBackgroundResource(R.drawable.shape_circle_green);
        } else {
            view_speak_status.setBackgroundResource(R.drawable.shape_circle_gray);
        }
    }

    public void updateUi(int pos, boolean isSpeaking) {
        if (pos < 0) {
            notifyDataSetChanged();
        } else {
            getItem(pos).isSpeaking = isSpeaking;
            notifyItemChanged(pos);
        }
    }
}