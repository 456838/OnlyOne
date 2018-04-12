package com.duowan.liveshow.controller.fm.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.duowan.liveshow.R;
import com.duowan.liveshow.view.adapter.PhoneCallAdapter;
import com.salton123.base.BaseSupportFragment;
import com.salton123.util.LogUtils;
import com.yy.live.core.CoreEvent;
import com.yy.live.core.CoreManager;
import com.yy.live.core.channel.micinfo.IChannelMicClient;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/24 18:02
 * Time: 18:02
 * Description:
 */
public class PhoneCallFragment extends BaseSupportFragment {
    private RecyclerView mRecyclerView;
    private PhoneCallAdapter mPhoneCallAdapter;

    @Override
    public int GetLayout() {
        return R.layout.fm_phone_call;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
        CoreManager.addClient(this);
    }

    @Override
    public void InitViewAndData() {
        mRecyclerView = f(R.id.rv_list);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        mPhoneCallAdapter = new PhoneCallAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mPhoneCallAdapter);
//        fab = f(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LogUtils.e(mPhoneCallAdapter.getData().size());
//                addData();
//            }
//        });
        mPhoneCallAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                mPhoneCallAdapter.updateUi(position, true);
            }
        });

    }

    private void addData() {
        List<MicTopInfo> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MicTopInfo micTopInfo = new MicTopInfo();
            micTopInfo.name = "张三" + i;
            micTopInfo.portraitUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
            list.add(micTopInfo);
        }
        mPhoneCallAdapter.addMoreData(list);
    }

    @Override
    public void InitListener() {

    }

    @CoreEvent(coreClientClass = IChannelMicClient.class)
    public void onUpdateMicQueueListInfo(List<MicTopInfo> micTopInfos) {
        LogUtils.e("麦序列表变化,size:" + micTopInfos.size());
        for (MicTopInfo micTopInfo : micTopInfos) {
            LogUtils.e("onUpdateMicQueueListInfo," + micTopInfo.toString());
        }
        mPhoneCallAdapter.setData(micTopInfos);
    }


    @CoreEvent(coreClientClass = IChannelMicClient.class)
    public void onChannelSpeakList(LongSparseArray<Integer> channelSpeakList) {
        LogUtils.e("onChannelSpeakList:" + channelSpeakList.size());
        List<MicTopInfo> micTopInfos = mPhoneCallAdapter.getData();
        if (channelSpeakList.size() == 0) {
            for (MicTopInfo item : micTopInfos) {
                item.isSpeaking = false;
            }
        }
        for (int i = 0; i < channelSpeakList.size(); i++) {
            for (MicTopInfo item : micTopInfos) {
                long uid = channelSpeakList.keyAt(i);
                LogUtils.e("onChannelSpeakList:uid:" + uid + "itemUid:" + item.uid);
                if (item.uid == uid) {
                    item.isSpeaking = true;
                } else {
                    item.isSpeaking = false;
                }
            }
        }
//        mPhoneCallAdapter.setData(micTopInfos);
        mPhoneCallAdapter.notifyDataSetChanged();
//        mPhoneCallAdapter.notifyDataSetChangedWrapper();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }
}
