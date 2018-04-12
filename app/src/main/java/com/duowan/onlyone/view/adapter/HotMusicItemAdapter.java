package com.duowan.onlyone.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duowan.onlyone.R;
import com.duowan.onlyone.model.entity.yinyuetai.VideoBean;
import com.salton123.onlyonebase.ImageLoader;
import com.xiao.nicevideoplayer.Clarity;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/11
 * YinYueTai
 */
public class HotMusicItemAdapter extends RecyclerView.Adapter<HotMusicItemAdapter.ViewHolder> {

    private ArrayList<VideoBean> videoList = new ArrayList<>();

    public HotMusicItemAdapter(ArrayList<VideoBean> videoList) {
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_hot_music_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoBean videoBean = videoList.get(position);
        TxVideoPlayerController controller = new TxVideoPlayerController(holder.videoplayer.getContext());
        holder.setController(controller);
        ImageLoader.Companion.display(controller.imageView(), videoBean.getAlbumImg());
        // x.image().bind(controller.imageView(), videoBean.getAlbumImg());
        List<Clarity> mClarities = new ArrayList<>();
        mClarities.add(new Clarity("标清", "270p", videoBean.getUrl()));
        mClarities.add(new Clarity("高清", "480p", videoBean.getHdUrl()));
        mClarities.add(new Clarity("超清", "720p", videoBean.getUhdUrl()));
        mClarities.add(new Clarity("蓝光", "1080p", videoBean.getShdUrl()));
        controller.setClarity(mClarities, 1);
        controller.setTitle(videoBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NiceVideoPlayer videoplayer;
        public TxVideoPlayerController mController;

        public ViewHolder(View view) {
            super(view);
            videoplayer = view.findViewById(R.id.videoplayer);
            // 将列表中的每个视频设置为默认16:9的比例
            ViewGroup.LayoutParams params = videoplayer.getLayoutParams();
            params.width = itemView.getResources().getDisplayMetrics().widthPixels; // 宽度为屏幕宽度
            params.height = (int) (params.width * 9f / 16f);    // 高度为宽度的9/16
            videoplayer.setLayoutParams(params);
        }

        public void setController(TxVideoPlayerController controller) {
            mController = controller;
            videoplayer.setController(mController);
        }
    }
}
