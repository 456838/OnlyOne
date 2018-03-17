package com.yy.live.core.channel.micinfo;

import android.util.LongSparseArray;

import com.medialib.video.MediaVideoMsg;
import com.yy.live.core.ICoreClient;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/23 21:17
 * Time: 21:17
 * Description:
 */
public interface IChannelMicClient extends ICoreClient {

    void onUpdateMicQueueListInfo(List<MicTopInfo> micTopInfos);

    void onChannelSpeakList(LongSparseArray<Integer> channelSpeakList);

    //通知正在说话人的uid
    void onUpdateSpeakerInfo(List<MicTopInfo> micTopInfos, MediaVideoMsg.AudioVolumeInfo audioVolumeInfo, boolean isChanged);
}
