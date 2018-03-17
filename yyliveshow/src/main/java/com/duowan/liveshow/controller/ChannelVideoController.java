package com.duowan.liveshow.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.duowan.liveshow.view.widget.DoubleLayout;
import com.duowan.mobile.Constant;
import com.duowan.mobile.mediaproxy.YSpVideoView;
import com.duowan.mobile.mediaproxy.YVideoViewLayout;
import com.duowan.mobile.utils.YLog;
import com.medialib.video.MediaVideoMsg;
import com.yy.live.model.bean.media.StreamConfigInfo;
import com.yy.live.model.bean.media.YYVideoStreamInfo;
import com.yyproto.outlet.IMediaVideo;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class ChannelVideoController {

    private Context mContext;
    YVideoViewLayout mViewLay;
    YVideoViewLayout mViewLay2;
    //    TextView mTextNote;
    DoubleLayout dlo;
    private IMediaVideo mChannel = null;
    private long mViewStreamid = 0;
    private long mViewStreamid2 = 0;
    private boolean isActiveStop = false;
    private int mLastSelectedCodeRate = 0;
    private boolean mHasCropVideo = false;
    private boolean mIsCropVideo = false;

    private int mAppId = 0;
    private long mChannelId = 0;

    private static final Object TAG = ChannelVideoController.class;


    private List<MediaVideoMsg.VideoStreamInfo> mCurStreamList = new ArrayList<MediaVideoMsg.VideoStreamInfo>();

    private MediaVideoMsg.VideoCodeRateInfo mVideoCodeRateInfo = null;
    private Map<Long, MediaVideoMsg.SpeakerStreamConfigInfo> mStreamKeyToConfig = new HashMap<Long, MediaVideoMsg.SpeakerStreamConfigInfo>();


    private final Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            int timeStart = mChannel.getTickCount();
            switch (msg.what) {
                case MediaVideoMsg.MsgType.onVideoStreamInfoNotify: {
                    MediaVideoMsg.VideoStreamInfo streamInfo = (MediaVideoMsg.VideoStreamInfo) msg.obj;
                    onVideoStreamInfoNotify(new YYVideoStreamInfo(streamInfo));
                }
                break;

                case MediaVideoMsg.MsgType.onVideoDecoderInfo: {
                    MediaVideoMsg.VideoDecoderInfo decoderInfo = (MediaVideoMsg.VideoDecoderInfo) msg.obj;
                    onVideoDecoderInfoNotify(decoderInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onVideoliveBroadcastNotify: {
                    MediaVideoMsg.VideoliveBroadcastInfo broadcastInfo = (MediaVideoMsg.VideoliveBroadcastInfo) msg.obj;
                    onVideoliveBroadcastNotify(broadcastInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onVideoRenderInfoNotify: {
                    MediaVideoMsg.VideoRenderInfo renderInfo = (MediaVideoMsg.VideoRenderInfo) msg.obj;
                    onVideoRenderInfo(renderInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onVideoDownlinkPlrNotify: {
                    MediaVideoMsg.VideoDownlinkPlrInfo plrInfo = (MediaVideoMsg.VideoDownlinkPlrInfo) msg.obj;
                    onVideoDownlinkPlr(plrInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onAudioLinkInfoNotity: {
                    MediaVideoMsg.MediaLinkInfo linkInfo = (MediaVideoMsg.MediaLinkInfo) msg.obj;
                    if (linkInfo.state == MediaVideoMsg.MediaLinkInfo.Connected) {
                        //mChannel.setMinBuffer(1000);
                    }
                }
                break;

                case MediaVideoMsg.MsgType.onAudioSpeakerInfoNotity: {
                    MediaVideoMsg.AudioSpeakerInfo speakerInfo = (MediaVideoMsg.AudioSpeakerInfo) msg.obj;
                    onAudioSpeakerInfo(speakerInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onAudioSpeakerStopMicNotity: {
                    MediaVideoMsg.AudioSpeakerStopMic speakerStopMic = (MediaVideoMsg.AudioSpeakerStopMic) msg.obj;
                    onAudioSpeakerStopMic(speakerStopMic);
                }
                break;

                case MediaVideoMsg.MsgType.onAudioStreamVolume: {
                    MediaVideoMsg.AudioVolumeInfo audioVolumeInfo = (MediaVideoMsg.AudioVolumeInfo) msg.obj;
                    onAudioVolumeArrived(audioVolumeInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onPlayAudioStateNotify: {
                    MediaVideoMsg.PlayAudioStateInfo playAudioStateInfo = (MediaVideoMsg.PlayAudioStateInfo) msg.obj;
                    onPlayAudioStateArrived(playAudioStateInfo);
                }
                break;

                case MediaVideoMsg.MsgType.onVideoCodeRateNotify: {
                    MediaVideoMsg.VideoCodeRateInfo rateInfo = ((MediaVideoMsg.VideoCodeRateInfo) msg.obj);
                    onVideoCodeRateNotify(rateInfo);
                }
                break;
                case MediaVideoMsg.MsgType.onAnchorBrocastData: {
                    MediaVideoMsg.AnchorBroadcastData info = ((MediaVideoMsg.AnchorBroadcastData) msg.obj);
                    onVideoAnchorBroadcastData(info);
                }
                break;
                case MediaVideoMsg.MsgType.onVideoP2PStatNotify: {
                    MediaVideoMsg.VideoP2PStatInfo info = (MediaVideoMsg.VideoP2PStatInfo) msg.obj;
                    onVideoP2PStat(info);
                }
                break;
                case MediaVideoMsg.MsgType.onEncodeSlowNotify: {
                    MediaVideoMsg.EncodeSlowInfo slowInfo = ((MediaVideoMsg.EncodeSlowInfo) msg.obj);
                    onEncodeSlowNotify(slowInfo);
                }
                break;
                case MediaVideoMsg.MsgType.onVideoBroadCastGroupNotify: {
                    MediaVideoMsg.VideoBroadcastGroupInfo info = (MediaVideoMsg.VideoBroadcastGroupInfo) msg.obj;
                    onVideoBroadCastGroup(info);
                }
                break;
                case MediaVideoMsg.MsgType.onAudienceStreamConfigInfo: {
                    MediaVideoMsg.AudienceStreamConfigInfo info = (MediaVideoMsg.AudienceStreamConfigInfo) msg.obj;
                    onAudienceStreamConfig(info);
                    updateStreamConfigInfo();
                }
                break;
                case MediaVideoMsg.MsgType.onVideoMetaDataInfo: {
                    MediaVideoMsg.VideoMetaDataInfo metaInfo = ((MediaVideoMsg.VideoMetaDataInfo) msg.obj);
                    onVideoPublisherMetaData(metaInfo);
                }
                break;
                case MediaVideoMsg.MsgType.onRtmpPublishStatusInfo: {
                    MediaVideoMsg.RtmpPublishStatusInfo info = (MediaVideoMsg.RtmpPublishStatusInfo) msg.obj;
                    onRtmpPublishStatus(info);
                }
            }//end of switch
            int timeEnd = mChannel.getTickCount();
            YLog.info(this, "mUIHandler handleMessage is called,what:%d, time period:%d", msg.what, (timeEnd - timeStart));
        } //end of handleMessage

    };

    private void onVideoCodeRateNotify(MediaVideoMsg.VideoCodeRateInfo rateInfo) {
        mVideoCodeRateInfo = rateInfo;
        for (Map.Entry<Integer, Integer> entry : mVideoCodeRateInfo.codeRateList.entrySet()) {
            if (entry.getValue() == 1) {
                mHasCropVideo = true;
                mIsCropVideo = true;
                break;
            }
        }
    }

    private void onVideoStreamInfoNotify(YYVideoStreamInfo streamInfo) {
        switch (streamInfo.state) {
            case MediaVideoMsg.VideoStreamInfo.Arrive: {
                if (streamInfo.encodeType != MediaVideoMsg.VideoEncodecType.ENCODE_H264) {
                    if (streamConfigInfo != null && streamConfigInfo.size() > 0) {
                        StreamConfigInfo info = streamConfigInfo.poll();
                        changeVideoBroadCastGroup(info.appId, info.channelId);
                    }

                } else {
                    onVideoStreamArrive(streamInfo);
                }
            }
            break;
            case MediaVideoMsg.VideoStreamInfo.Start: {
            }
            break;
            case MediaVideoMsg.VideoStreamInfo.Stop: {
                onVideoStreamStop(streamInfo);
            }
            break;
        }
    }

    private Queue<StreamConfigInfo> streamConfigInfo = new LinkedList();

    public void updateStreamConfigInfo() {
        Map<Long, MediaVideoMsg.SpeakerStreamConfigInfo> streamKeyToConfig = getStreamKeyToConfig();
        for (Map.Entry<Long, MediaVideoMsg.SpeakerStreamConfigInfo> configEntry : streamKeyToConfig.entrySet()) {
            MediaVideoMsg.SpeakerStreamConfigInfo configInfo = configEntry.getValue();
            for (Map.Entry<Integer, MediaVideoMsg.ChannelConfigInfo> channelEntry : configInfo.channelConfigs.entrySet()) {
                MediaVideoMsg.ChannelConfigInfo channelInfo = channelEntry.getValue();
                StreamConfigInfo info = new StreamConfigInfo();
                info.appId = configInfo.appId;
                info.channelId = channelEntry.getKey();
                streamConfigInfo.add(info);

//                changeVideoBroadCastGroup(configInfo.appId,channelEntry.getKey());
            }
        }
    }

    private void onVideoDecoderInfoNotify(MediaVideoMsg.VideoDecoderInfo decoderInfo) {
        long userGroupId = decoderInfo.userGroupId;
        long streamid = decoderInfo.streamId;
        YLog.info(TAG, "videoViewControler streamNotify recv userGroup:%d,streamId:%d, type:%d", userGroupId, streamid, decoderInfo.type);
//		if (isStreamExist(streamid))
//		{
//			YLog.info(TAG, "videoViewControler streamNotify streamId:%d exist,do nothing",streamid);
//			return;
//		}
        YVideoViewLayout yl = getAvailLayout(streamid);
        if (yl == null) {
            YLog.info(TAG, "videoViewControler streamNotify recv userGroup:%d,streamId:%d, type:%d, layout is not available", userGroupId, streamid, decoderInfo.type);
            return;
        }
        YSpVideoView mVideoView = yl.getExistingView();
        if (mVideoView != null) {
            if ((decoderInfo.type == Constant.DecoderType.SOFT_DEOCDER && mVideoView.getViewType() == YSpVideoView.ViewType.GLView)
                    || (decoderInfo.type == Constant.DecoderType.ANDROID_HARD_DECODER1 && mVideoView.getViewType() != YSpVideoView.ViewType.GLView)) {

                YLog.info(TAG, "videoViewControler streamNotify recv userGroup:%d,streamId:%d, type:%d, current view type:%d", userGroupId, streamid, decoderInfo.type, mVideoView.getViewType());
//				return;
            }
            mChannel.removeSpVideoView(mVideoView);
            mVideoView.unLinkFromStream(userGroupId, streamid);
        }
        mVideoView = yl.clearAndCreateNewView(decoderInfo.type);
        if (mVideoView != null) {
            mVideoView.setScaleMode(Constant.ScaleMode.AspectFit);
            addSpeakerInfoTextView((streamid >> 32), yl);
            mChannel.addSpVideoView(mVideoView);
            mVideoView.linkToStream(userGroupId, streamid);
            YLog.info(TAG, "videoViewControler streamNotify subscribe streamId:%d", streamid);
        } else {
            YLog.info(TAG, "videoViewControler streamNotify no view for streamId:%d", streamid);
        }
        updateLayout(0);
    }

    private void onVideoStreamArrive(MediaVideoMsg.VideoStreamInfo streamInfo) {
        long userGroupId = streamInfo.userGroupId;
        long streamid = streamInfo.streamId;
        mCurStreamList.add(streamInfo);
        mChannel.startVideo(userGroupId, streamid);

        YVideoViewLayout yl = getAvailLayout(streamid);
        if (yl == null) {
            return;
        }
        YSpVideoView mVideoView = yl.clearAndCreateNewView();
        if (mVideoView != null) {
            mVideoView.setScaleMode(Constant.ScaleMode.AspectFit);
            addSpeakerInfoTextView((streamid >> 32), yl);
            mChannel.addSpVideoView(mVideoView);
            mVideoView.linkToStream(userGroupId, streamid);
            YLog.info(TAG, "videoViewControler streamNotify subscribe streamId:%d", streamid);
        } else {
            YLog.info(TAG, "videoViewControler streamNotify no view for streamId:%d", streamid);
        }
        updateLayout(0);
    }

    private void addSpeakerInfoTextView(long uid, YVideoViewLayout viewLoayout) {
        TextView tvSpeakerInfo = new TextView(mContext);
        tvSpeakerInfo.setId((int) uid);
        tvSpeakerInfo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        tvSpeakerInfo.setPadding(1, 1, 0, 0);
        tvSpeakerInfo.setGravity(Gravity.LEFT);
        tvSpeakerInfo.setTextColor(Color.BLUE);
        tvSpeakerInfo.setTextSize(12);
        viewLoayout.addView(tvSpeakerInfo);
        viewLoayout.bringChildToFront(tvSpeakerInfo);
    }

    public void GetPics() {
//		if (mFastView != null) {
//			Bitmap bmp = mFastView.getVideoScreenshot();
//			if (bmp != null) {
//				try {
//					FileOutputStream fos = new FileOutputStream("/storage/emulated/0/0.png");
//					bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
//					fos.flush();
//					fos.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		else {
        if (mViewLay != null) {
            YSpVideoView videoView = mViewLay.getExistingView();
            if (videoView != null) {
                Bitmap bmp1 = videoView.getVideoScreenshot();
                if (bmp1 != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream("/storage/emulated/0/1.png");
                        bmp1.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (mViewLay2 != null) {
            YSpVideoView videoView = mViewLay2.getExistingView();
            if (videoView != null) {
                Bitmap bmp2 = videoView.getVideoScreenshot();
                if (bmp2 != null) {
                    try {
                        FileOutputStream fos = new FileOutputStream("/storage/emulated/0/2.png");
                        bmp2.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//		}
    }

    private void onVideoStreamStop(MediaVideoMsg.VideoStreamInfo streamInfo) {
        long userGroup = streamInfo.userGroupId;
        long streamId = streamInfo.streamId;
        YLog.info(TAG, "onVideoStreamStop userGroup:%d,streamId:%d", userGroup, streamId);

        YVideoViewLayout yl = getAvailLayout(streamId);
        YSpVideoView mVideoView = yl.getExistingView();
        if (mVideoView != null) {
            mVideoView.unLinkFromStream(userGroup, streamId);
            mVideoView.setVisibility(View.GONE); //不可见
            mChannel.removeSpVideoView(mVideoView);
            mVideoView.release();
            if (streamId == mViewStreamid) {
                mViewStreamid = 0;
            }
            if (streamId == mViewStreamid2) {
                mViewStreamid2 = 0;
            }
        }

        removeStreamInfo(streamId);
        updateLayout(0);
    }

    private void onVideoliveBroadcastNotify(MediaVideoMsg.VideoliveBroadcastInfo broadcastInfo) {
        YLog.info(this, "ideoliveBroadcast,%d %d %d", broadcastInfo.appid, broadcastInfo.subsid, broadcastInfo.hasVideo);
    }

    private void onVideoRenderInfo(MediaVideoMsg.VideoRenderInfo renderInfo) {
        YLog.info(this, "videoRederInfo,state:%d", renderInfo.state);
    }

    private void onVideoDownlinkPlr(MediaVideoMsg.VideoDownlinkPlrInfo plrInfo) {
        YLog.info(this, "videoDownlinkPlrInfo,%d %d %f", plrInfo.appid, plrInfo.uid, plrInfo.plr);
    }

    private void onAudioSpeakerInfo(MediaVideoMsg.AudioSpeakerInfo speakerInfo) {
        YLog.info(this, "audioSpeakerInfo,%d %d", speakerInfo.uid, speakerInfo.state);
    }

    private void onAudioSpeakerStopMic(MediaVideoMsg.AudioSpeakerStopMic speakerStopMic) {
        YLog.info(this, "onAudioSpeakerStopMic,uid:%d sid:%d", speakerStopMic.uid, speakerStopMic.sid);
    }

    private void onAudioVolumeArrived(MediaVideoMsg.AudioVolumeInfo audioVolumeInfo) {
        YLog.info(this, "audioVolume uid:%d,volume:%d", audioVolumeInfo.uid, audioVolumeInfo.volume);
    }

    private void onPlayAudioStateArrived(MediaVideoMsg.PlayAudioStateInfo playAudioStateInfo) {
        YLog.info(this, "playAudioState finished");
    }

    private void onVideoAnchorBroadcastData(MediaVideoMsg.AnchorBroadcastData info) {
        TextView textView = null;
        if (mViewLay != null) {
            textView = (TextView) mViewLay.findViewById((int) info.uid);
        }

        if (textView == null && mViewLay2 != null) {
            textView = (TextView) mViewLay2.findViewById((int) info.uid);
        }

        Iterator it = info.intDatas.keySet().iterator();
        if (textView != null && it.hasNext()) {
            int frameRate = info.intDatas.get(MediaVideoMsg.AnchorBroadcastDataKey.ABDK_ACTUAL_FRAME_RATE);
            int bitRate = info.intDatas.get(MediaVideoMsg.AnchorBroadcastDataKey.ABDK_ACTUAL_BIT_RATE);
            textView.setText(String.format("speaker info\nframeRate: %dfps\nbitRate: %dbps", frameRate, bitRate));
        }
    }

    private void onVideoP2PStat(MediaVideoMsg.VideoP2PStatInfo info) {
        int downLinkStreamFlow = info.statItems.get(MediaVideoMsg.VideoP2PStataticsKey.kVideoP2PDownLinkStreamFlow);
        int downLinkStreamFlowByServer = info.statItems.get(MediaVideoMsg.VideoP2PStataticsKey.kVideoP2PDownLinkStreamFlowByServer);
        int uplinkStreamFlow = info.statItems.get(MediaVideoMsg.VideoP2PStataticsKey.kVideoP2PUplinkStreamFlow);

        //YLog.info(this, "onVideoP2PStat,%d %d %d",downLinkStreamFlow, downLinkStreamFlowByServer, uplinkStreamFlow);

        String p2pInfo = String.format("[p2p info] R:%.2fKB %.2fKB S:%.2fKB",
                (float) downLinkStreamFlowByServer / 100, (float) downLinkStreamFlow / 100, (float) uplinkStreamFlow / 100);


//        if(mTextNote.getVisibility() == View.GONE)
//        {
//            mTextNote.setVisibility(View.VISIBLE);
//            mTextNote.setTextColor(Color.RED);
//        }
//
//        mTextNote.setText(p2pInfo);
    }

    private void onEncodeSlowNotify(MediaVideoMsg.EncodeSlowInfo info) {
        float encodeRate = info.encodeRate;
        String p2pInfo = String.format("encode info\nEncode Rate: %.2f", encodeRate);

//        if(mTextNote.getVisibility() == View.GONE)
//        {
//            mTextNote.setVisibility(View.VISIBLE);
//            mTextNote.setTextColor(Color.RED);
//        }
//
//        mTextNote.setText(p2pInfo);
    }

    private void onVideoPublisherMetaData(MediaVideoMsg.VideoMetaDataInfo info) {
        //YLog.info(this, "[callBack] onVideoPublisherMetaData");
    }

    private void onRtmpPublishStatus(MediaVideoMsg.RtmpPublishStatusInfo info) {
        YLog.info(this, "[callBack] onRtmpPublishStatus, appId:%d, status:%d", info.appId, info.status);
    }

    private void onVideoBroadCastGroup(MediaVideoMsg.VideoBroadcastGroupInfo info) {
        YLog.info(this, "onVideoBroadCastGroup, %d %d ", info.appId, info.isNewBroadCastGroup);
    }

    private void onAudienceStreamConfig(MediaVideoMsg.AudienceStreamConfigInfo info) {
        //YLog.info(this, "[callBack] onAudienceStreamConfig");

        mStreamKeyToConfig = info.streamKeyToConfig;
    }

    private boolean isStreamExist(Long streamId) {
        for (MediaVideoMsg.VideoStreamInfo streamInfo : mCurStreamList) {
            if (streamInfo.streamId == streamId) {
                return true;
            }
        }
        return false;
    }

    private void removeStreamInfo(Long streamId) {
        for (int i = 0; i < mCurStreamList.size(); i++) {
            if (mCurStreamList.get(i).streamId == streamId) {
                mCurStreamList.remove(i);
                return;
            }
        }
    }

    public ChannelVideoController(Context context, IMediaVideo channel, YVideoViewLayout mViewLay, YVideoViewLayout mViewLay2, DoubleLayout dlo) {
        mContext = context;
        mChannel = channel;
        this.mViewLay = mViewLay;
        this.mViewLay2 = mViewLay2;
//        this.mTextNote = mTextNote;
        this.dlo = dlo;
        makeSurface(true);
        mChannel.addMsgHandler(mUIHandler);
    }


    public void destroy() {
        unsubscribeVideoStreams();
        mChannel.removeMsgHandler(mUIHandler);
    }


    private void makeSurface(boolean hasVideo) {
    }

    public void onChangeSubChannel(long sid, long subsid) {
        YLog.debug(TAG, "channelControler onChangeSubChannel,sid:%d,subsid:%d", sid, subsid);
        unsubscribeVideoStreams();
    }

    public void doVoiceAction() {
//        mChannel.mute();
    }

    public void unsubscribeVideoStreams() {
        if (mChannel == null) {
            return;
        }
        stopSubscribeVideo();
        mCurStreamList.clear();
    }

    /**
     * 恢复订阅视频流
     */
    public void resumeSubscribeVideo() {
        synchronized (this) {
            for (MediaVideoMsg.VideoStreamInfo streamInfo : mCurStreamList) {
                long userGroupId = streamInfo.userGroupId;
                long streamId = streamInfo.streamId;
                YLog.info(this, "[call] ChannelVideoController resumeSubscribeVideo for userGroup:%d, streamId:%d", userGroupId, streamId);
                mChannel.startVideo(userGroupId, streamId);
                YVideoViewLayout yl = getAvailLayout(streamId);
                if (yl == null) {
                    return;
                }
                YSpVideoView mVideoView = yl.clearAndCreateNewView();
                if (mVideoView != null) {
                    mVideoView.setScaleMode(Constant.ScaleMode.AspectFit);
                    mChannel.addSpVideoView(mVideoView);
                    mVideoView.linkToStream(userGroupId, streamId);
                    YLog.info(TAG, "videoViewControler streamNotify subscribe streamId:%d", streamId);
                }
            }
            isActiveStop = false;
            updateLayout(0);
        }
    }

    /**
     * 暂停订阅视频流
     */
    public void stopSubscribeVideo() {
        synchronized (this) {
            for (MediaVideoMsg.VideoStreamInfo streamInfo : mCurStreamList) {
                try {
                    long userGroupId = streamInfo.userGroupId;
                    long streamId = streamInfo.streamId;
                    YLog.info(this, "[call] ChannelVideoController pauseSubscribeVideo for userGroupId:%d, streamId:%d", userGroupId, streamId);

                    YVideoViewLayout yl = getAvailLayout(streamId);
                    YSpVideoView mVideoView = yl.getExistingView();
                    mChannel.stopVideo(userGroupId, streamId);
                    mVideoView.unLinkFromStream(userGroupId, streamId);
                    mChannel.removeSpVideoView(mVideoView);
                    mVideoView.setVisibility(View.GONE); //不可见
                    mVideoView.release();
                    if (streamId == mViewStreamid) {
                        mViewStreamid = 0;
                    }
                    if (streamId == mViewStreamid2) {
                        mViewStreamid2 = 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            isActiveStop = true;
            updateLayout(0);
        }
    }

    public MediaVideoMsg.VideoCodeRateInfo getVideoCodeRateInfo() {
        return mVideoCodeRateInfo;
    }

    public Map<Long, MediaVideoMsg.SpeakerStreamConfigInfo> getStreamKeyToConfig() {
        return mStreamKeyToConfig;
    }

    public void changeVideoCodeRate(int rate) {
        stopSubscribeVideo();
        mCurStreamList.clear();
        mViewStreamid = 0;
        mViewStreamid2 = 0;
        mChannel.changeCodeRate(rate, mVideoCodeRateInfo.appid);
        mLastSelectedCodeRate = rate;
    }

    public void changeVideoBroadCastGroup(int appId, long channelId) {
        if (mAppId == appId && mChannelId == channelId) {
            return;
        }

        mAppId = appId;
        mChannelId = channelId;

        stopSubscribeVideo();
        mCurStreamList.clear();
        mViewStreamid = 0;
        mViewStreamid2 = 0;
        mChannel.changeVideoBroadCastGroup(appId, channelId);
    }

    public void changeCropVideoCodeRate() {
        if (!mHasCropVideo)
            return;

        for (Map.Entry<Integer, Integer> entry : mVideoCodeRateInfo.codeRateList.entrySet()) {
            if (entry.getValue() == 1) {
                int oldCodeRate = mLastSelectedCodeRate;
                changeVideoCodeRate(entry.getKey());
                mLastSelectedCodeRate = oldCodeRate;
                mIsCropVideo = true;
                break;
            }
        }
    }

    public void changeCustomVideoCodeRate() {
        if (mHasCropVideo && mIsCropVideo) {
            changeVideoCodeRate(mLastSelectedCodeRate);
            mIsCropVideo = false;
        }
    }

    public void updateLayout(long delay) {
        if (mViewStreamid != 0) {
            mViewLay.setVisibility(View.VISIBLE);
        } else {
            mViewLay.setVisibility(View.GONE);
        }
        if (mViewStreamid2 != 0) {
            mViewLay2.setVisibility(View.VISIBLE);
        } else {
            mViewLay2.setVisibility(View.GONE);
        }
//        if(mViewStreamid == 0 && mViewStreamid2 == 0 && isActiveStop == false)
//        {
//            mTextNote.setVisibility(View.VISIBLE);
//            mTextNote.setTextColor(Color.WHITE);
//            mTextNote.setText("该主播没有视频直播");
//        }
//        else
//        {
//            mTextNote.setVisibility(View.GONE);
//        }
        dlo.postDelayed(new Runnable() {
            @Override
            public void run() {
                dlo.reArrageLayout();
            }
        }, delay);
    }

    private YVideoViewLayout getAvailLayout(long streamid) {
        if (streamid == mViewStreamid) {
            return mViewLay;
        }
        if (streamid == mViewStreamid2) {
            return mViewLay2;
        }
        if (mViewStreamid == 0) {
            mViewStreamid = streamid;
            return mViewLay;
        }
        if (mViewStreamid2 == 0) {
            mViewStreamid2 = streamid;
            return mViewLay2;
        }
        return null;
    }

    public void onResume() {
        for (MediaVideoMsg.VideoStreamInfo streamInfo : mCurStreamList) {
            try {
                long userGroupId = streamInfo.userGroupId;
                long streamId = streamInfo.streamId;

                YVideoViewLayout yl = getAvailLayout(streamId);
                YSpVideoView mVideoView = yl.getExistingView();
                mVideoView.onResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onPause() {
        for (MediaVideoMsg.VideoStreamInfo streamInfo : mCurStreamList) {
            try {
                long userGroupId = streamInfo.userGroupId;
                long streamId = streamInfo.streamId;

                YVideoViewLayout yl = getAvailLayout(streamId);
                YSpVideoView mVideoView = yl.getExistingView();
                mVideoView.onPause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
