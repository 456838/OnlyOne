package com.duowan.onlyone.func.video.view.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.duowan.onlyone.func.video.view.widget.SmallVideoPlayView;
import com.duowan.onlyone.model.entity.kaiyan.ItemListBean;
import com.salton123.onlyonebase.view.verticalswitch.ViewSwitchAdapter;
import com.salton123.util.log.FP;

import java.util.List;

/**
 * Created by ZZB on 2017/8/16.
 */

public class VerticalSwitchAdapter extends ViewSwitchAdapter<ItemListBean, VerticalSwitchAdapter.VH> {

    private int mItemCount;

    @Override
    public void onBindView(VH viewHolder, int pos) {
        ItemListBean info = getItemData(pos);
        if (info != null) {
            ((SmallVideoPlayView) viewHolder.itemView).updateData(info, false);
        }
    }

    @Override
    public VH createViewHolder(ViewGroup parent) {
        View view = new SmallVideoPlayView(parent.getContext());
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_small_video_play_v2, parent, false);
        return new VH(view);
    }

    @Override
    public ItemListBean getItemData(int pos) {
        if (pos >= 0 && pos < FP.size(mData)) {
            return mData.get(pos);
        }
        return null;
    }

    @Override
    public List<ItemListBean> getData() {
        return mData;
    }

    @Override
    public void setData(List<ItemListBean> data) {
        mData.clear();
        if (!FP.empty(data)) {
            mData.addAll(data);
        }
        mItemCount = FP.size(mData);
    }

    @Override
    public int getCount() {
        return mItemCount;
    }

    @Override
    public void addData(List<ItemListBean> data) {
        if (!FP.empty(data)) {
            for (ItemListBean info : data) {
                if (!mData.contains(info)) {
                    mData.add(info);
                } else {
                    Log.d("zzb1", "duplicate:" + info);
                }
            }
        }
        mItemCount = FP.size(mData);
    }

    protected static class VH extends ViewSwitchAdapter.ViewHolder {

        public VH(View itemView) {
            super(itemView);
        }
    }
}
