package com.yy.live.model.proxy;

import android.os.Looper;


import com.salton123.util.LogUtils;
import com.salton123.util.log.MLog;
import com.yy.live.model.engine.YYEngine;
import com.yy.mobile.YYHandler;
import com.yy.mobile.YYMessage;
import com.yyproto.outlet.LoginEvent;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/8
 * Time: 22:57
 * Description:
 */
public class LoginProxy extends BaseProxy {
    private String TAG = LoginProxy.class.getSimpleName();

    static volatile LoginProxy defaultInstance;

    public static LoginProxy getInstance() {
        if (defaultInstance == null) {
            synchronized (LoginProxy.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LoginProxy();
                }
            }
        }
        return defaultInstance;
    }

    public void init() {
        YYEngine.getInstance().mYYHandlerMgr.add(loginHandler);
    }

    private YYHandler loginHandler = new YYHandler(Looper.getMainLooper()) {
        @MessageHandler(message = YYMessage.LoginMessage.onLoginNGRes)
        public void onAuthRes(LoginEvent.LoginResNGEvent res) {
            MLog.info(TAG,"onAuthRes,res="+res.uSrvResCode);
            p(res);     //因为暂时不知道怎么抛数据，全部抛到主线程
        }

        @MessageHandler(message = YYMessage.LoginMessage.onUInfoMod)
        public void onUInfoModRes(LoginEvent.ETUInfoModRes evt) {
            LogUtils.e("loginHandler,onUInfoMod");
            // Logger.i("onUInfoModRes resCode=" + evt.resCode + " limit_end_time=" + new String(evt.limit_end_time));
            int size = evt.props.size();
            for (int i = 0; i < size; i++) {
                int key = evt.props.keyAt(i);
                // Logger.i("onUInfoModRes key=" + key + " val=" + new String(evt.props.get(key)));
            }
        }
//         该消息被废弃
//        @MessageHandler(message = YYMessage.LoginMessage.onAnonymLoginRes)
//        public void onUInfoModRes(LoginEvent. evt) {
//            Logger.i("onUInfoModRes resCode=" + evt.resCode + " limit_end_time=" + new String(evt.limit_end_time));
//            int size = evt.props.size();
//            for (int i = 0; i < size; i++) {
//                int key = evt.props.keyAt(i);
//                Logger.i("onUInfoModRes key=" + key + " val=" + new String(evt.props.get(key)));
//            }
//        }

        @MessageHandler(message = YYMessage.LoginMessage.onSyncList)
        public void onSyncList(LoginEvent.ETListKeyVal etListKeyVal) {      //我的频道
            LogUtils.e("loginHandler,onSyncList");
            LogUtils.i("onSyncList chLists length=" + etListKeyVal.chLists.length);
            p(etListKeyVal);
        }

        @MessageHandler(message = YYMessage.LoginMessage.onMyChanList)
        public void onMyChanList(LoginEvent.ETMyChanList etMyChanList) {      //我的频道
            LogUtils.e("loginHandler,onMyChanList");
            LogUtils.i("onMyChanList chLists length=" + etMyChanList.chId);
            p(etMyChanList);
        }

        @MessageHandler(message = YYMessage.LoginMessage.onKickoff)
        public void onKickoff(LoginEvent.ETLoginKickoff etLoginKickoff) {     //登录被踢，处理放在YYLiveBaseActivity
            LogUtils.e("loginHandler,onKickoff");
            p(etLoginKickoff);
        }

        //        ETMyInfoAnonym
        @MessageHandler(message = YYMessage.LoginMessage.onMyInfoAnonym)
        public void onMyInfoAnonym(LoginEvent.ETMyInfoAnonym evt) {
            LogUtils.e("loginHandler,onMyInfoAnonym");
//            CustomApplication.getInstance().setAnonymousLogin(true);
//            Logger.e("onMyInfoAnonym");
        }

        @MessageHandler(message = YYMessage.LoginMessage.onIMUInfo)
        public void onIMUInfo(final LoginEvent.ETIMUInfoKeyVal evt) {       //匿名登录的用户会自动下发该消息
            p(evt);
//            if (evt != null && evt.resCode == 0 && evt.getCtx().equals("loginUserInfo")) {
//                LoginUserInfo mUserInfo;
//                for (LoginEvent.IMUInfo uinfo : evt.uinfos) {
//                    mUserInfo = new LoginUserInfo();
//                    Iterator iter = uinfo.val.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        Map.Entry entry = (Map.Entry) iter.next();
//                        String key = (String) entry.getKey();
//                        byte[] val = (byte[]) entry.getValue();
//                        String valStr = new String(val);
//                        if (!StringUtils.isBlank(valStr)) {
//                            if (key.equals(SDKParam.IMUInfoPropSet.uid)) {
//                                mUserInfo.userId = Long.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.nick)) {
//                                mUserInfo.nickName = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.sex)) {
//                                mUserInfo.gender = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.birthday)) {
//                                mUserInfo.birthday = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.area)) {
//                                mUserInfo.area = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.province)) {
//                                mUserInfo.province = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.city)) {
//                                mUserInfo.city = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.sign)) {
//                                mUserInfo.signature = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.intro)) {
//                                mUserInfo.description = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.jifen)) {
//                                mUserInfo.credits = (int) Math.floor(Integer.valueOf(valStr) / 60);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.yyno)) {
//                                mUserInfo.yyId = Long.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.logo_index)) {
//                                mUserInfo.iconIndex = Integer.valueOf(valStr);
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.custom_logo)) {
//                                mUserInfo.iconUrl = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_100)) {
//                                mUserInfo.iconUrl_100_100 = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_144)) {
//                                mUserInfo.iconUrl_144_144 = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_640)) {
//                                mUserInfo.iconUrl_640_640 = valStr;
//                            } else if (key.equals(SDKParam.IMUInfoPropSet.stage_name)) {
//                                mUserInfo.reserve1 = valStr;
//                            }
//                        }
//                    }
//                    //保存用户信息到数据库
//                    if (mUserInfo.yyId != 0) {
//                        p(mUserInfo);
//                    }
//                }
//            }
        }
    };

    public void destory() {
        YYEngine.getInstance().mYYHandlerMgr.remove(loginHandler);
    }
}
