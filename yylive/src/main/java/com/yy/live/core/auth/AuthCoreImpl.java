package com.yy.live.core.auth;


import com.salton123.util.StringUtils;
import com.salton123.util.log.MLog;
import com.yy.live.core.AbstractBaseCore;
import com.yy.live.core.CoreError;
import com.yy.live.core.CoreManager;
import com.yy.live.core.Uint32;
import com.yy.live.model.bean.LoginUserInfo;
import com.yy.live.model.engine.YYEngine;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.info.AccountInfo;
import com.yyproto.db.DCHelper;
import com.yyproto.db.IDatabase;
import com.yyproto.db.IRow;
import com.yyproto.db.ITable;
import com.yyproto.db.ProtoTable;
import com.yyproto.outlet.LoginEvent;
import com.yyproto.outlet.SDKParam;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.Map;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/23 21:21
 * Time: 21:21
 * Description:
 */
public class AuthCoreImpl extends AbstractBaseCore implements IAuthCore {
    private String TAG = "AuthCoreImpl";
    private int loginState;    //1 登录成功 0 登录失败

    public AuthCoreImpl() {
        CoreManager.addClient(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onAuthRes(LoginEvent.LoginResNGEvent res) {
        MLog.info(TAG, "onAuthRes: LoginEvent.LoginResNGEvent.uSrvResCode=" + res.uSrvResCode);
        if (res.uSrvResCode != LoginEvent.LoginResNGEvent.LOGIN_SUCESS
                && res.uSrvResCode != LoginEvent.LoginResNGEvent.RES_UAUTH) {
                /*
                 * 对于进频道类APP，登录过程分为两步：第一步是验证登录信息，第二步是登录AP
				 * 两个步骤都成功，才算是登录成功。
				 * 这里是登录AP失败，需要做相关处理
				 */
            MLog.warn(AuthCoreImpl.this, "onAuthRes: Login AP failed");
            String resMsg;
            CoreError coreError;
            switch (res.uSrvResCode) {
                case LoginEvent.LoginResNGEvent.NET_BROKEN:
                    resMsg = "网络错误";
                    break;
                case LoginEvent.LoginResNGEvent.APKICKOFF:
                    resMsg = "强制踢人错误";
                    break;
                case LoginEvent.LoginResNGEvent.TIMEOUT:
                    resMsg = "超时错误";
                    return;//业务层自己已经处理超时,这里跳过
                default:
                    resMsg = "未知错误";
                    break;
            }
            coreError = new CoreError(CoreError.Domain.Auth, res.uSrvResCode, resMsg);
            onLoginFail(coreError);
            return;
        }

        //其它情况，表示需要解析Auth事件，这里进行“Auth回调事件转换”
        AuthEvent.AuthBaseEvent base = AuthSDK.toAuthEvent(res.strAuthData);
//        MLog.info(TAG, "toAuthEvent=%s", base.getClass().getSimpleName());
        if (base instanceof AuthEvent.LoginEvent) {
            AuthEvent.LoginEvent et = (AuthEvent.LoginEvent) base;
            if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
                //登录成功,留给onMyInfo事件回调处理
                MLog.info(TAG, "onLoginResult:UIAction.SUCCESS,uid=%s,yyid=%s", et.uid, et.yyid);
                notifyClients(IAuthClient.class, "onLoginSucceed", false, et.uid);
                YYEngine.GetOneIMUInfoReq("loginUserInfo", Long.parseLong(et.uid));
                loginState = 1;
            } else {
                CoreError coreError = new CoreError(CoreError.Domain.Auth, et.errCode, "LoginEvent failed:" + et.description);
                onLoginFail(coreError);
            }
        } else if (base instanceof AuthEvent.AnonymousEvent) {
            AuthEvent.AnonymousEvent et = (AuthEvent.AnonymousEvent) base;
            if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
                notifyClients(IAuthClient.class, "onLoginSucceed", true, et.uid);
//                YYEngine.GetOneIMUInfoReq("loginUserInfo", Long.parseLong(et.uid));
                loginState = 1;
            } else {
                CoreError coreError = new CoreError(CoreError.Domain.Auth, et.errCode, "AnonymousEvent failed:" + et.description);
                onLoginFail(coreError);
            }
        } else {
            CoreError coreError = new CoreError(CoreError.Domain.Auth, res.uSrvResCode, base.getClass().getSimpleName() + " failed");
            onLoginFail(coreError);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onIMUInfo(final LoginEvent.ETIMUInfoKeyVal evt) {
        MLog.info(TAG,"onIMUInfo"+evt.uinfos);
        if (evt != null && evt.resCode == 0 && evt.getCtx().equals("loginUserInfo")) {
            LoginUserInfo mUserInfo = null;
            for (LoginEvent.IMUInfo uinfo : evt.uinfos) {
                mUserInfo = new LoginUserInfo();
                Iterator iter = uinfo.val.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    byte[] val = (byte[]) entry.getValue();
                    String valStr = new String(val);
                    if (!StringUtils.isBlank(valStr)) {
                        if (key.equals(SDKParam.IMUInfoPropSet.uid)) {
                            mUserInfo.userId = Long.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.nick)) {
                            mUserInfo.nickName = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.sex)) {
                            mUserInfo.gender = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.birthday)) {
                            mUserInfo.birthday = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.area)) {
                            mUserInfo.area = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.province)) {
                            mUserInfo.province = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.city)) {
                            mUserInfo.city = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.sign)) {
                            mUserInfo.signature = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.intro)) {
                            mUserInfo.description = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.jifen)) {
                            mUserInfo.credits = (int) Math.floor(Integer.valueOf(valStr) / 60);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.yyno)) {
                            mUserInfo.yyId = Long.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.logo_index)) {
                            mUserInfo.iconIndex = Integer.valueOf(valStr);
                        } else if (key.equals(SDKParam.IMUInfoPropSet.custom_logo)) {
                            mUserInfo.iconUrl = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_100)) {
                            mUserInfo.iconUrl_100_100 = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_144)) {
                            mUserInfo.iconUrl_144_144 = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.hd_logo_640)) {
                            mUserInfo.iconUrl_640_640 = valStr;
                        } else if (key.equals(SDKParam.IMUInfoPropSet.stage_name)) {
                            mUserInfo.reserve1 = valStr;
                        }
                    }
                }
            }
            mLoginUserInfo = mUserInfo;
        }
    }


    /**
     * 登录失败
     *
     * @param coreError
     */
    private void onLoginFail(CoreError coreError) {
        loginState = 0;
        notifyClients(IAuthClient.class, "onLoginFail", coreError);
    }


    @Override
    public void logout() {
        YYEngine.Logout();
        loginState = 0;
    }

    @Override
    public long getUserId() {
        return YYEngine.GetCurrentUid();
    }


    @Override
    public boolean isLogined() {
        return loginState == 1;
    }

    @Override
    public boolean isAnoymousLogined() {
        return isAnonymousUser(getUserId());
    }


    @Override
    public long getAnoymousUid() {
        long uid = -1L;
        try {
            IDatabase db = DCHelper.openOrCreateDatabase(ProtoTable.PROTO_DB_ID);
            if (db != null) {
                ITable table = db.getTable(ProtoTable.TABLE_ID.E_TBL_LOGINUINFO.ordinal());
                if (table != null) {
                    IRow row = table.getRow(ProtoTable.ONE_DIM_KEY);
                    if (row != null) {
                        Uint32 uint32 = new Uint32(row.getInt32(ProtoTable.LOGINUINFO.dwUid.ordinal()));
                        uid = uint32.longValue();
                    }
                }
            }
        } catch (Throwable throwable) {
            MLog.error(TAG, throwable);
        }
        MLog.info(TAG, "getAnoymousUid uid=%d", uid);
        return uid;
    }


    @Override
    public boolean isAnonymousUser(long uid) {
        return uid >= 2000000000 || uid == 0;
    }


    @Override
    public AccountInfo getActivitedAccount() {
        return AuthUI.getInstance().getDatabase().getActivitedAccount();
    }

    LoginUserInfo mLoginUserInfo = new LoginUserInfo();

    @Override
    public LoginUserInfo getLoginUserInfo() {
        return mLoginUserInfo;
    }
}
