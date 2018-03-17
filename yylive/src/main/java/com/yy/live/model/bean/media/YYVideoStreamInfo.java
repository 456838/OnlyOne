package com.yy.live.model.bean.media;

import com.medialib.video.MediaVideoMsg;

import java.util.Map;

/**
 * Created by Ping on 16/7/17.
 */
public class YYVideoStreamInfo extends MediaVideoMsg.VideoStreamInfo {
    public int streamFlag = 0; //表示左边的流
    // 以下字段仅在state为Arrive时有效
    public int publisherClientType = MediaVideoMsg.PublisherClientType.KNOWN_TYPE;		// 参考MediaVideoMsg。PublisherClientType
    public int width = 0;
    public int height = 0;
    public int bitRate = 0;
    public int frameRate = 0;
    public int encodeType = 0;		// 参考MediaVideoMsg.VideoEncodecType
    public int autoSubscribe = 0;	// 表示该视频是否已经自动订阅  0 否 1是
    public Integer liveLevel; //高清开播，码流：标清、高清
    public Integer originalScreen; //流的原始形状
    public Integer verticalStyle; //手Y竖屏观看时的形状
    public Integer horizontalStyle;  //手Y横屏观看时的形状
    public long streamToUid; //视频流的UID


    public YYVideoStreamInfo(MediaVideoMsg.VideoStreamInfo info) {
        if (info != null) {
            userGroupId = info.userGroupId;
            streamId = info.streamId;
            streamToUid=(streamId >> 32);
            publishId = info.publishId;
            state = info.state;
            metaDatas = info.metaDatas; //key见 VideoMetaDataKey
            parseMetaData(metaDatas);
        }
    }

    private void parseMetaData(Map<Byte, Integer> metaDatas) {
        if (metaDatas != null && metaDatas.size() > 0) {
            streamFlag = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_STREAM_FLAG);
            publisherClientType = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_CLIENNT_TYPE);
            int resolution = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_RESOLUTION);
            width = resolution >>> 16;
            height = resolution & 0x0000ffff;
            bitRate = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_BIT_RATE);
            frameRate = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_FRAME_RATE);
            encodeType = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_ENCODE_TYPE);
            autoSubscribe = metaDatas.get((short) MediaVideoMsg.VideoMetaDataKey.MST_VIDEO_AUTO_SUBSCRIBE);
            liveLevel = metaDatas.get((short) MediaVideoMsg.MetaDataInfoId.PUBLISHER_APP_RESERVE1);
            originalScreen = metaDatas.get((short) MediaVideoMsg.MetaDataInfoId.PUBLISHER_RESERVE_META1);
            //手Y竖屏观看时的形状
            verticalStyle = metaDatas.get((short) MediaVideoMsg.MetaDataInfoId.PUBLISHER_RESERVE_META2);
            //手Y横屏观看时的形状
            horizontalStyle = metaDatas.get((short) MediaVideoMsg.MetaDataInfoId.PUBLISHER_RESERVE_META3);
        }
    }

    public void updateMetaData(Map<Byte, Integer> updateMetaDatas) {
        this.metaDatas.putAll(updateMetaDatas);
        parseMetaData(this.metaDatas);
    }

    public void updateStreamInfo(MediaVideoMsg.VideoStreamInfo info) {
       this.state=info.state;
    }

    @Override
    public String toString() {
        return "YYVideoStreamInfo{" +
                "userGroupId=" + userGroupId +
                ", streamId=" + streamId +
                ", streamToUid=" + streamToUid +
                ", publishId=" + publishId +
                ", state=" + state +
                ", streamFlag=" + streamFlag +
                ", publisherClientType=" + publisherClientType +
                ", width=" + width +
                ", height=" + height +
                ", bitRate=" + bitRate +
                ", frameRate=" + frameRate +
                ", encodeType=" + encodeType +
                ", autoSubscribe=" + autoSubscribe +
                ", liveLevel=" + liveLevel +
                ", originalScreen=" + originalScreen +
                ", verticalStyle=" + verticalStyle +
                ", horizontalStyle=" + horizontalStyle +
                ", metaDatas=" + metaDatas +
                '}';
    }

}
