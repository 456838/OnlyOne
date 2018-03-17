package com.yy.live.model.proxy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.medialib.video.MediaVideoMsg;
import com.salton123.util.LogUtils;
import com.salton123.util.StringUtils;
import com.salton123.util.log.MLog;
import com.yy.live.model.bean.channel.audience.IMAudienceInfoList;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;
import com.yy.live.model.bean.core.ChannelInfo;
import com.yy.live.model.engine.YYEngine;
import com.yy.live.utils.IntToLongUtils;
import com.yy.live.utils.YYHintUtils;
import com.yy.mobile.YYHandler;
import com.yy.mobile.YYMessage;
import com.yyproto.outlet.LoginEvent;
import com.yyproto.outlet.SDKParam;
import com.yyproto.outlet.SessEvent;
import com.yyproto.outlet.SessMicEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/12
 * Time: 9:14
 * Description:
 */
public class MediaProxy extends BaseProxy {
    private String TAG = MediaProxy.class.getSimpleName();
    static volatile MediaProxy defaultInstance;

    public static MediaProxy getInstance() {
        if (defaultInstance == null) {
            synchronized (MediaProxy.class) {
                if (defaultInstance == null) {
                    defaultInstance = new MediaProxy();
                }
            }
        }
        return defaultInstance;
    }

    public ChannelInfo currentChannelInfo; // 当前频道信息

    private MediaProxy() {

    }

    public void init() {
        YYEngine.getInstance().mYYHandlerMgr.add(signalHandler);
        YYEngine.getInstance().mIMediaVideo.addMsgHandler(mMediaHandler);
        currentChannelInfo = new ChannelInfo();
//        setMediaParam();
    }

    public void deInit() {
        YYEngine.getInstance().mYYHandlerMgr.remove(signalHandler);
        YYEngine.getInstance().mIMediaVideo.removeMsgHandler(mMediaHandler);
    }

    public void setMediaParam() {
        Map<Integer, Integer> configs = new HashMap<Integer, Integer>();
//        configs.put(MediaVideoMsg.MediaConfigKey.AUDIO_ENCODE_QUALITY, 5);      //5表示高音质
//        configs.put(MediaVideoMsg.MediaConfigKey.CCK_JOIN_FETCH_ENABLE, 1);
        configs.put(MediaVideoMsg.MediaConfigKey.CCK_IS_VIP_USER, 1);
//        configs.put(MediaVideoMsg.MediaConfigKey.CCK_ENABLE_FASTPLAY_HIGHT_QUALITY_MODE_NEW, 1);
        YYEngine.getInstance().mIMediaVideo.setConfigs(0, configs);
    }

    private YYHandler signalHandler = new YYHandler(Looper.getMainLooper()) {

        @MessageHandler(message = YYMessage.ChannelMessage.onJoin)
        public void onJoin(SessEvent.ETSessJoinRes etSessJoinRes) {
            LogUtils.e("YYHandler,onJoin,mSuccess:" + etSessJoinRes.mSuccess + "subSid:" + etSessJoinRes.mRootSid + "ssid:" + etSessJoinRes.mSubSid);
            if (etSessJoinRes.mSuccess && etSessJoinRes.mErrId == SDKParam.SessJoinResCode.JOIN_SUCCESS) {      //表示登录成功
                onJoinChannelSuccess(etSessJoinRes);
            } else {        //登录失败,通知业务

            }
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onText)
        public void onChatText(final SessEvent.ETSessOnText et) {       //这里可能会有让cpu占用标高
        }


        @MessageHandler(message = YYMessage.ChannelMessage.onUpdateChInfoFail)
        public void onUpdateChInfo(SessEvent.EUpdateChInfo etUpdateChInfo) {
            LogUtils.e("YYHandler,onUpdateChInfoFail");
            int resCode = etUpdateChInfo.mResCode;
            if (resCode == SessEvent.EUpdateChInfo.RES_SUCCESS) {
                LogUtils.e("onUpdateChInfo success,SIT_STYLE:" + etUpdateChInfo.mProps.get(SDKParam.SessInfoItem.SIT_STYLE));
            } else {
                LogUtils.e("onUpdateChInfo failed: " + resCode);
            }
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onTextChatSvcResultRes)
        public void onUpdateChInfo(SessEvent.ETTextChatSvcResultRes etTextChatSvcResultRes) {
            LogUtils.e("YYHandler,onTextChatSvcResultRes:" + YYHintUtils.getChatSvcResult(etTextChatSvcResultRes.reason));

        }

        @MessageHandler(message = YYMessage.ChannelMessage.onChannelRolers)
        public void onChannelRolers(SessEvent.ETSessChannelRolers etSessChannelRolers) {
            p(etSessChannelRolers);
        }


        @MessageHandler(message = YYMessage.ChannelMessage.onUpdateMaixu)
        public void onUpdateMaixu(SessMicEvent.ETSessMic et) {
            p(et);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onTextChatSvcResultRes)
        public void onTextChatSvcResultRes(SessEvent.ETTextChatSvcResultRes et) {
            LogUtils.e("YYHandler,onTextChatSvcResultRes,sid:" + et.sid + ",topsid:" + et.topSid + "," + YYHintUtils.getChatSvcResult(et.reason));
            p(et);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onUInfo)
        public void onUInfo(SessEvent.ETSessUInfo et) {
            LogUtils.e("YYHandler,onUInfo");
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onKickoff)
        public void onKickoff(SessEvent.ETSessKickoff et) {
            LogUtils.e("YYHandler,onKickoff");
            p(et);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onKickOffChannel)
        public void onKickoff(SessEvent.EKickOffChannel et) {
            LogUtils.e("YYHandler,onKickOffChannel");
            p(et);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onMultiKick)
        public void onKickoff(SessEvent.ETSessMultiKick et) {
            LogUtils.e("YYHandler,onMultiKick");
            p(et);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onMultiKickNtf)
        public void onKickoff(SessEvent.ETSessMultiKickNtf et) {
            LogUtils.e("YYHandler,onMultiKickNtf");
            p(et);
        }

        //分页频道内用户信息
        @MessageHandler(message = YYMessage.ChannelMessage.onUInfoPage)
        public void onUInfoPage(final SessEvent.ETSessUInfoPage et) {
            p(et);
//
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onLineStat)
        public void onLineStat(SessEvent.ETSessOnlineCount et) {
            p(et);
        }


        @MessageHandler(message = YYMessage.ChannelMessage.onChInfo)
        public void onChInfo(SessEvent.ETGetChInfoKeyVal p_ETGetSubChInfoKeyVal) {
            LogUtils.e("YYHandler,onChInfo,length:" + p_ETGetSubChInfoKeyVal.chInfos.length);
            p(p_ETGetSubChInfoKeyVal);
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onSubChInfo)
        public void onSubChInfo(SessEvent.ETGetSubChInfoKeyVal p_ETGetSubChInfoKeyVal) {        //该请求必须在进频道成功以后发送有效
//            LogUtils.e("onSubChInfo,length:" + p_ETGetSubChInfoKeyVal.chInfos.length);
//            SessEvent.ChInfoKeyVal[] ChInfoKeyValArr = p_ETGetSubChInfoKeyVal.chInfos;
////            Logger.e("收到onSubChInfo,len:"+ChInfoKeyValArr.length);
//            if (ChInfoKeyValArr != null && ChInfoKeyValArr.length > 0) {
//                SessEvent.ChInfoKeyVal item = ChInfoKeyValArr[0];
//                String chSid = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_SID));
//                String chName = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_NAME));
//                String chStyle = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_STYLE));
//                String chHasPassword = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_HASPASSWD));
//                String chLogo = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_LOGO_URL));
//                String chPid = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_PID));
//                String chTemplateId = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_TEMPLATE_ID));
//                String chOrder = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_CHANNEL_ORDER));
//                String chJieDai = new String(item.getStrVal(SessEvent.ChInfoKeyVal.CHINFO_SIT_JIEDAI));
//                ChInfoKeyValBean bean = new ChInfoKeyValBean(chSid, chName, chStyle, chHasPassword, chLogo, chPid, chTemplateId, chOrder, chJieDai);
//                p(bean);
//            }
        }

        @MessageHandler(message = YYMessage.ChannelMessage.onMultiKickNtf)
        public void onMultiKickNtf(SessEvent.ETSessMultiKickNtf et) {
            MLog.info(TAG,"ETSessMultiKickNtf="+et);
            p(et);
        }

        @MessageHandler(message = YYMessage.LoginMessage.onIMUInfo)
        public void onIMUInfo(final LoginEvent.ETIMUInfoKeyVal evt) {

            List<MicTopInfo> micTopInfos = new ArrayList<>();
            for (LoginEvent.IMUInfo uinfo : evt.uinfos) {
                Iterator iter = uinfo.val.entrySet().iterator();
                MicTopInfo micTopInfo = new MicTopInfo();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    byte[] val = (byte[]) entry.getValue();
                    String valStr = new String(val);
                    if (!StringUtils.isBlank(valStr)) {
                        if (key.equals(SDKParam.IMUInfoPropSet.uid)) {
                            long userId = Long.valueOf(valStr);
                            micTopInfo.uid = userId;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.nick)) {
                            micTopInfo.name = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.sex)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.birthday)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.area)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.province)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.city)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.sign)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.intro)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.jifen)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.yyno)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.logo_index)) {
                            micTopInfo.portraitIndex = Integer.parseInt(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.custom_logo)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_100)) {
                            micTopInfo.portraitUrl = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_144)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_640)) {
                        } else if (key.equals(SDKParam.IMUInfoPropSet.stage_name)) {
                        }
                    }
                }
                micTopInfos.add(micTopInfo);
            }
            p(new IMAudienceInfoList<MicTopInfo>(evt.getCtx(), micTopInfos));

        }
    };


    private Handler mMediaHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LogUtils.d("MediaHandler:" + msg.what);
            switch (msg.what) {
                case MediaVideoMsg.MsgType.onAudioSpeakerInfoNotity: {
                    MediaVideoMsg.AudioSpeakerInfo mAudioSpeakerInfo = (MediaVideoMsg.AudioSpeakerInfo) msg.obj;
                    LogUtils.e("onAudioSpeakerInfoNotity:" + mAudioSpeakerInfo.state + ":" + mAudioSpeakerInfo.uid);//1118898029    //1196403803
                    p(mAudioSpeakerInfo);
//                    if (mAudioSpeakerInfo.state == MediaVideoMsg.AudioSpeakerInfo.Start && micInfoBus.micTopInfos != null) {
//                        for (MicTopInfo micTopInfo : micInfoBus.micTopInfos) {
//                            if (micTopInfo.uid == mAudioSpeakerInfo.uid) {
//                                micTopInfo.isSpeaking = mAudioSpeakerInfo.state == MediaVideoMsg.AudioSpeakerInfo.Start ? true : false;
//                            }
//                        }
//                        p(micInfoBus);
//                    }
                    //这里应该要不断刷新界面
                    break;
                }

                case MediaVideoMsg.MsgType.onAudioSpeakerStopMicNotity: {
                    MediaVideoMsg.AudioSpeakerStopMic audioSpeakerStopMic = (MediaVideoMsg.AudioSpeakerStopMic) msg.obj;
                    p(audioSpeakerStopMic);
                    break;
                }

                case MediaVideoMsg.MsgType.onChannelAudioStateNotify: {     //205
                    MediaVideoMsg.ChannelAudioStateInfo micStateInfo = (MediaVideoMsg.ChannelAudioStateInfo) msg.obj;
                    break;
                }
                case MediaVideoMsg.MsgType.onPlayAudioStateNotify: {        //206
                    MediaVideoMsg.PlayAudioStateInfo micStateInfo = (MediaVideoMsg.PlayAudioStateInfo) msg.obj;
                    break;
                }
                case MediaVideoMsg.MsgType.onAudioLinkInfoNotity: {        //201
                    MediaVideoMsg.MediaLinkInfo mediaLinkInfo = (MediaVideoMsg.MediaLinkInfo) msg.obj;
                    break;
                }
                case MediaVideoMsg.MsgType.onAudioStreamVolume: {       //204
//                    MediaVideoMsg.AudioVolumeInfo audioVolumeInfo = (MediaVideoMsg.AudioVolumeInfo) msg.obj;
//                    p(audioVolumeInfo);
//                    LogUtils.d("onAudioSpeakerInfoNotity,volume:" + audioVolumeInfo.volume + ",uid:" + audioVolumeInfo.uid);//1118898029    //1196403803
//                    if (micInfoBus.micTopInfos != null) {
//                        for (MicTopInfo micTopInfo : micInfoBus.micTopInfos) {
//                            micTopInfo.isSpeaking = false;
//                            if (micTopInfo.uid == audioVolumeInfo.uid) {
//                                micTopInfo.isSpeaking = audioVolumeInfo.volume < 20 ? false : true;
//                            }
//                        }
////                        LogUtils.e("size:" + micInfoBus.micTopInfos.size());
//                        p(micInfoBus);
//                    }
                    break;
                }
                case MediaVideoMsg.MsgType.onMicStateInfoNotify: {   //mic打开关闭事件,这个方法没什么用，不知道uid    203
                    MediaVideoMsg.MicStateInfo micStateInfo = (MediaVideoMsg.MicStateInfo) msg.obj;
                    break;
                }
            }
        }
    };
//
//    private void onJoinChannelError(SessEvent.ETSessJoinRes res) {
//        p(res);
//    }

    private synchronized void onJoinChannelSuccess(SessEvent.ETSessBase etSessBase) {
        // 赋予频道号
        if (etSessBase instanceof SessEvent.ETSessJoinRes) { // 加入频道
            SessEvent.ETSessJoinRes et = (SessEvent.ETSessJoinRes) etSessBase;
            currentChannelInfo.topASid = et.mAsid;
            currentChannelInfo.topSid = et.mRootSid;
            currentChannelInfo.subSid = et.mSubSid;
        } else if (etSessBase instanceof SessEvent.ETChangeFolderRes) { // 跳转子频道
            SessEvent.ETChangeFolderRes et = (SessEvent.ETChangeFolderRes) etSessBase;
            currentChannelInfo.topASid = et.getASid();
            currentChannelInfo.topSid = et.getTopSid();
            currentChannelInfo.subSid = et.mSid;
            currentChannelInfo.channelMode = ChannelInfo.ChannelMode.MicQueue_Mode;
            currentChannelInfo.channelType = ChannelInfo.ChannelType.NULL_TYPE;
        }
        //订阅频道内的广播请求（频道内人数在线变更）
        YYEngine.SessSubOnlineUserBroadcastReq(currentChannelInfo.topSid);
        //接收公屏消息
        //subscirbe floor
        int[] svcTypes = new int[3];
        svcTypes[0] = 15003;  //flower
        //公屏
        svcTypes[1] = 31;
        // 公告广播
        svcTypes[2] = 17;
        YYEngine.SvcSubscribeReq(svcTypes);
        YYEngine.SessGetOneSubChInfoReq(currentChannelInfo.topSid, currentChannelInfo.subSid);
    }

    public long toUnsignedLong(int value) {
        return IntToLongUtils.toUnsignedLong(value);
    }


}
