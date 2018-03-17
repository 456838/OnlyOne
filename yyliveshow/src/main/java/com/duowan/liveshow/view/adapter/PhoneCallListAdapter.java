package com.duowan.liveshow.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duowan.liveshow.R;
import com.salton123.base.AdapterBase;
import com.salton123.base.ViewHolder;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/24 17:46
 * Time: 17:46
 * Description:
 */
public class PhoneCallListAdapter extends AdapterBase<MicTopInfo> {
    public PhoneCallListAdapter(Context pContext) {
        super(pContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = GetLayoutInflater().inflate(R.layout.adapter_item_caller, null);
        }
        // CircleImageView sdv_actor_header = ViewHolder.get(convertView, R.id.sdv_actor_header);
        TextView tv_actor_name = ViewHolder.get(convertView, R.id.tv_actor_name);
        tv_actor_name.setText("" + getItem(position).name);
        // display(sdv_actor_header, getItem(position).portraitUrl);
        View view_speak_status = ViewHolder.get(convertView, R.id.iv_speaker_off);
        if (getItem(position).isSpeaking) {//正在说话
            view_speak_status.setBackgroundResource(R.drawable.shape_circle_green);
        } else {
            view_speak_status.setBackgroundResource(R.drawable.shape_circle_gray);
        }
        return convertView;
    }

    public static void display(ImageView p_ImageView, String p_Uri) {
        int screenWidth = DensityUtil.getScreenWidth();
        int screenHeight = DensityUtil.getScreenHeight();
        ImageOptions options = new ImageOptions.Builder().setFadeIn(true)
                .setConfig(Bitmap.Config.ALPHA_8)
                .setSize(screenWidth / 2, screenHeight / 2)
                // .setFailureDrawableId(R.mipmap.default_pic_load_error)
                .setLoadingDrawableId(R.drawable.yy_bear_logo)
                .build();
        if (!TextUtils.isEmpty(p_Uri)) {
            x.image().bind(p_ImageView, p_Uri, options);
        } else {
            p_ImageView.setImageResource(R.drawable.yy_bear_logo);
        }
    }
}
