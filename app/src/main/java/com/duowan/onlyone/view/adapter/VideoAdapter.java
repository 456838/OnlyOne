package com.duowan.onlyone.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.duowan.onlyone.R;
import com.duowan.onlyone.model.entity.kaiyan.DataBean;
import com.duowan.onlyone.model.entity.kaiyan.ItemListBean;
import com.duowan.onlyone.model.entity.utils.DateUtil;
import com.salton123.onlyonebase.ImageLoader;

import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper;

public class VideoAdapter extends BGARecyclerViewAdapter<ItemListBean> {
    public VideoAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

//    public VideoAdapter(RecyclerView recyclerView) {
//        super(recyclerView, R.layout.video_item);
//    }


    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getType().equals("video")) {
            return R.layout.video_item;
        } else {
            return R.layout.null_item;
        }
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, ItemListBean model) {
        if (getItem(position).getType().equals("video")) {
            DataBean dataBean = model.getData();
            if (dataBean != null) {
                ImageLoader.Companion.display(helper.getImageView(R.id.img), dataBean.getCover() != null ? dataBean.getCover().getDetail() : "");
                helper.setText(R.id.title, model.getData().getTitle());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("#").append(dataBean.getCategory())
                        .append(" ")
                        .append(" / ")
                        .append(" ")
                        .append(DateUtil.formatTime2(dataBean.getDuration()));
                helper.setText(R.id.description, stringBuilder.toString());
            }
        } else {
            helper.setVisibility(R.id.ll_holder, View.GONE);
        }
    }
}