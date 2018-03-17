package com.yy.udbauth.proto;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.salton123.util.EventUtil;
import com.yy.live.R;
import com.yy.mobile.YYHandler;
import com.yy.mobile.YYHandlerMgr;
import com.yy.mobile.YYMessage;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthRequest.AuthBaseReq;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.IAuthEventWatcher;
import com.yy.udbauth.ui.InitCode;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yyproto.outlet.IProtoMgr;
import com.yyproto.outlet.LoginEvent.ETLoginKickoff;
import com.yyproto.outlet.LoginEvent.LoginResNGEvent;
import com.yyproto.outlet.LoginRequest;
import com.yyproto.outlet.LoginRequest.LoginWithAuthReq;
import com.yyproto.outlet.SDKParam;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 概述：进频道类APP，收发包管理器
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年5月4日 下午5:36:52
 */
public class YYSdkProtoManager extends IProtoMagager {

	private Context mContext;
	private YYHandlerMgr mYYHandlerMgr = new YYHandlerMgr();
	private CopyOnWriteArraySet<IAuthEventWatcher> mWatcherList = new CopyOnWriteArraySet<IAuthEventWatcher>();

	@Override
	public int init(Context context, String appId, String appKey, String terminalType, boolean enableAnonymousLogin) {

		mContext = context.getApplicationContext();
		IProtoMgr.instance().getLogin().watch(mYYHandlerMgr);//IProtoMgr在SDK之外初始化
		mYYHandlerMgr.add(mYYHandler);

		boolean success = AuthSDK.init(mContext, appId, appKey, terminalType, enableAnonymousLogin);
		return success ? InitCode.SUCCESS : InitCode.AUTH_SDK_INIT_ERROR;
	}
	
	@Override
	public String getName() {
		return "yysdk";
	}

	@Override
	public void addAuthEventWatcher(IAuthEventWatcher watcher) {
		mWatcherList.add(watcher);
	}

	@Override
	public void removeAuthEventWatcher(IAuthEventWatcher watcher) {
		mWatcherList.remove(watcher);
	}

	@Override
	public int sendAuthRequest(AuthBaseReq auth) {
		LoginWithAuthReq req = new LoginWithAuthReq();
		req.mAuthData = auth.marshall();
		final int code = IProtoMgr.instance().getLogin().sendRequest(req);
		return code;
	}

	@Override
	public boolean sendAuthRequestWithToast(AuthBaseReq auth) {

		if (!AndroidHelper.isNetworkAvailable(mContext)) {
			Toast.makeText(mContext, R.string.ua_network_unavailable, Toast.LENGTH_SHORT).show();
			return false;
		}

		LoginWithAuthReq req = new LoginWithAuthReq();
		req.mAuthData = auth.marshall();
		final int code = IProtoMgr.instance().getLogin().sendRequest(req);

		if (code == SDKParam.RequestRes.REQ_SUCCESS) {
			// 提交请求成功
			return true;
		} else if (code == SDKParam.RequestRes.REQ_FAILED) {
			// 请求失败，一般都是由于指针为空引起的  
			showShortToast(R.string.ua_send_request_failed);
			return false;
		} else if (code == SDKParam.RequestRes.REQ_TOO_QUICK) {
			// 请求的速度太快 
			showShortToast(R.string.ua_send_request_too_quick);
			return false;
		} else if (code == SDKParam.RequestRes.REQ_MARSHALL_ERR) {
			// 数据打包错误 
			showShortToast(R.string.ua_send_request_marshall_err);
			return false;
		} else if (code == SDKParam.RequestRes.REQ_SDK_NOT_INIT) {
			// sdk初始化失败，有可能是动态库加载不成功 
			showShortToast(R.string.ua_send_request_not_init);
			return false;
		} else {
			// 请求失败，未知错误 
			showShortToast(R.string.ua_send_request_unknown_err);
			return false;
		}
	}

	private void showShortToast(int res) {
		Toast.makeText(mContext, res, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void logout() {
		LoginRequest.LoginReqLogout req = new LoginRequest.LoginReqLogout();
		IProtoMgr.instance().getLogin().sendRequest(req);
	}


	private YYHandler mYYHandler = new YYHandler(Looper.getMainLooper()) {

		@MessageHandler(message = YYMessage.LoginMessage.onKickoff)
		public void onKickOff(ETLoginKickoff et) {
			EventUtil.sendEvent(et);
			/* 处理被踢下线的事件 */
			for (IAuthEventWatcher listener : mWatcherList) {
				listener.onKickOff(et.uReason, new String(et.strReason));
			}
		}

		@MessageHandler(message = YYMessage.LoginMessage.onLoginNGRes)
		public void onAuthRes(LoginResNGEvent res) {
			EventUtil.sendEvent(res);
			if (res.uSrvResCode != LoginResNGEvent.LOGIN_SUCESS && res.uSrvResCode != LoginResNGEvent.RES_UAUTH) {
				/*
				 * 对于进频道类APP，登录过程分为两步：第一步是验证登录信息，第二步是登录AP，两个步骤都成功，才算是登录成功。
				 * 这里登录AP失败，需要做相关处理
				*/
				for (IAuthEventWatcher listener : mWatcherList) {
					listener.onLoginAPFalied(res.uSrvResCode);
				}
				return;
			}

			if (res.uSrvResCode == LoginResNGEvent.LOGIN_SUCESS) {
				/*
				 * 这里表示登录AP成功了 
				 */
				//onLoginAPSuccess();
				return;
			}

			//转换为Auth回调事件
			AuthEvent.AuthBaseEvent base = AuthSDK.toAuthEvent(res.strAuthData);

			for (IAuthEventWatcher listener : mWatcherList) {
				listener.onAuthRes(base);
			}
		}
	};


}
