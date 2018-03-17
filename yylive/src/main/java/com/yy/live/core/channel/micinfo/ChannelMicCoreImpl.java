package com.yy.live.core.channel.micinfo;


import android.util.LongSparseArray;

import com.medialib.video.MediaVideoMsg;
import com.salton123.util.LogUtils;
import com.yy.live.core.AbstractBaseCore;
import com.yy.live.core.CoreManager;
import com.yy.live.model.bean.channel.audience.IMAudienceInfoList;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;
import com.yy.live.model.engine.YYEngine;
import com.yyproto.base.ProtoModType;
import com.yyproto.jni.YYSdk;
import com.yyproto.outlet.SessMicEvent;
import com.yyproto.outlet.SessQuery;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/23 21:21
 * Time: 21:21
 * Description:
 */
public class ChannelMicCoreImpl extends AbstractBaseCore implements IChannelMicCore {

    private List<MicTopInfo> mMicQueueListInfo;
    private List<Long> micUidList;
    private MicTopInfo firstMicTopInfo;
    private LongSparseArray<Integer> mChannelSpeakList;

    public ChannelMicCoreImpl() {
        CoreManager.addClient(this);
        micUidList = new ArrayList<>();
        mMicQueueListInfo = new ArrayList<>();
        mChannelSpeakList = new LongSparseArray<Integer>();
        firstMicTopInfo = new MicTopInfo();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onUpdateMaixu(SessMicEvent.ETSessMic et) {
        byte[] infodata = YYSdk.queryInfo(ProtoModType.MOD_TYPE_SESSION, SessQuery.SESS_QUERY_MIC_INFO, et.channel_id);
        SessQuery.SessMicInfo micinfo = new SessQuery.SessMicInfo();
        micinfo.unmarshall(infodata);
        micUidList = micinfo.uids;
        if (micUidList.size() == 0) {
            mMicQueueListInfo = new ArrayList<>();
            notifyClients(IChannelMicClient.class, "onUpdateMicQueueListInfo", mMicQueueListInfo);
        }
        LogUtils.e("ChannelMicCoreImpl,onUpdateMaixu:micList,size:" + micUidList.size());
        ctx = "micTop_" + System.currentTimeMillis() + "";
        YYEngine.GetOnMicListUserInfo(ctx, micUidList);
    }

    private String ctx;

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onUpdateMicQueueList(IMAudienceInfoList audienceInfoList) {
        if (audienceInfoList.getContext().equals(ctx)) {
            mMicQueueListInfo = audienceInfoList.getAudienceList();
        }
        if (micUidList.size() == 0) {
            mMicQueueListInfo = new ArrayList<>();
        }
        LogUtils.e("ChannelMicCoreImpl,onUpdateMicQueueListInfo:micTopInfos,size:" + mMicQueueListInfo.size());
        notifyClients(IChannelMicClient.class, "onUpdateMicQueueListInfo", mMicQueueListInfo);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMediaAudioSpeakerInfoNotity(MediaVideoMsg.AudioSpeakerInfo speakerInfo) {
        LogUtils.e("onMediaAudioSpeakerInfoNotity,speakerInfo:" + speakerInfo.state + ":" + speakerInfo.uid);//1118898029    //1196403803
        switch (speakerInfo.state) {
            case MediaVideoMsg.AudioSpeakerInfo.Start:
                mChannelSpeakList.put(speakerInfo.uid, MediaVideoMsg.AudioSpeakerInfo.Start);
                break;
            case MediaVideoMsg.AudioSpeakerInfo.Stop:
                mChannelSpeakList.delete(speakerInfo.uid);
                break;
        }
        notifyClients(IChannelMicClient.class, "onChannelSpeakList", mChannelSpeakList);
    }

    @Override
    public MicTopInfo getTopMicInfo() {
        MicTopInfo micTopInfo;
        if (mMicQueueListInfo != null && mMicQueueListInfo.size() > 0) {
            micTopInfo = mMicQueueListInfo.get(0);
        } else {
            micTopInfo = firstMicTopInfo;
        }
        if (micTopInfo == null) {
            micTopInfo = new MicTopInfo();
        }
        if (micTopInfo.name == null) {
            micTopInfo.name = "";
        }
        return micTopInfo;
    }

    @Override
    public List<MicTopInfo> getMicQueueListInfo() {
        if (mMicQueueListInfo != null) {
            return mMicQueueListInfo;
        } else {
            return new ArrayList<MicTopInfo>();
        }
    }

    @Override
    public List<Long> getMicUidListInfo() {
        if (micUidList != null) {
            return micUidList;
        } else {
            return new ArrayList<Long>();
        }
    }

}