package com.yy.udbauth.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.yy.udbauth.AuthEvent.AuthBaseEvent;
import com.yy.udbauth.log.LogHelper;
import com.yy.udbauth.ui.tools.OnUdbAuthListener;
import com.yy.udbauth.ui.tools.OpreateType;

public class AuthCallbackProxy {

	private static Object sLock = new Object();
	private static Handler sHandler = new Handler(Looper.getMainLooper());

	private static OpreateType sPurposeOpreateType = OpreateType.NONE;
	private static OnUdbAuthListener sOnUdbAuthListener;
	private static String sAppId;
	private static long startTime = System.currentTimeMillis();

	protected static void setOnUdbAuthListener(final OnUdbAuthListener listener, final OpreateType opType, String appid) {
		sOnUdbAuthListener = listener;
		sPurposeOpreateType = opType;
		sAppId = appid;
		startTime = System.currentTimeMillis();
	}

	/** 业务方不需要调用此函数，给UdbAuth结果调用，用于把onActivityResult的结果转化为OnUdbAuthListener中*/
	private static boolean parseActivityResult(int requestCode, int resultCode, Intent data) {
		//TODO-未实现
		return false;
	}

	/** 业务方不需要调用此函数，给UdbAuth结果调用的，用于获取当前用户目录操作的类型*/
	public static OpreateType getPurposeOpreateType() {
		return sPurposeOpreateType;
	}

	/** 业务方不需要调用此函数，给UdbAuth结果调用的，表示操作结果*/
	public static void onAuthSuccess(AuthBaseEvent event, OpreateType type) {
		synchronized (sLock) {
			if (sOnUdbAuthListener != null) {
				sHandler.post(InnerRunnable.createSuccess(event, type, sOnUdbAuthListener));
				sOnUdbAuthListener = null;

				report(type, UIConstants.RESULT_SUCCESS, event.getUser(), event.getUid(), event.getContext());
			}
		}
	}

	/** 业务方不需要调用此函数，给UdbAuth结果调用的，表示操作取消*/
	public static void onCancel(OpreateType type) {
		synchronized (sLock) {
			if (sOnUdbAuthListener != null) {
				if (type == OpreateType.NEXT_VERIFY) {
					/* 
					 * 二次验证被取消，这里需要调用logout接口，
					 * 不然SDK会出现无登录状态的情况（连匿名登录状态都没有），这将导致无法进入频道
					 */
					AuthUI.getInstance().logout();
				}
				sHandler.post(InnerRunnable.createCancel(type, sOnUdbAuthListener));
				sOnUdbAuthListener = null;

				report(type, UIConstants.RESULT_ERROR, null, null, "");
			}
		}
	}

	/** 业务方不需要调用此函数，给UdbAuth结果调用的，表示操作出错*/
	public static void onError(int errCode, OpreateType type) {
		synchronized (sLock) {
			if (sOnUdbAuthListener != null) {
				sHandler.post(InnerRunnable.createError(errCode, type, sOnUdbAuthListener));
				sOnUdbAuthListener = null;

				report(type, UIConstants.RESULT_ERROR + " with code=" + errCode, null, null, "");
			}
		}
	}

	protected static void report(OpreateType type, String result, String user, String uid, String context) {
		//操作结果日志
		try {
			JSONObject j = new JSONObject();
			j.put("op_type", type.toString());
			j.put("result", result);
			String rsp_time = Long.toString(System.currentTimeMillis() - startTime);
			LogHelper.getInstance().logToDBForCommon(UIConstants.LOG_TYPE, LogHelper.LEVEL_INFO, sAppId + "", user,
					uid, context, rsp_time, j.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class InnerRunnable implements Runnable {

		enum Call {
			onSuccess, onCancel, onError
		};

		Call call = Call.onCancel;

		int errCode;
		AuthBaseEvent event;
		OpreateType type;
		OnUdbAuthListener listener;

		private InnerRunnable() {
		}

		public static InnerRunnable createSuccess(AuthBaseEvent event, OpreateType type, OnUdbAuthListener listener) {
			InnerRunnable r = new InnerRunnable();
			r.event = event;
			r.type = type;
			r.listener = listener;
			r.call = Call.onSuccess;
			return r;
		}

		public static InnerRunnable createCancel(OpreateType type, OnUdbAuthListener listener) {
			InnerRunnable r = new InnerRunnable();
			r.type = type;
			r.listener = listener;
			r.call = Call.onCancel;
			return r;
		}

		public static InnerRunnable createError(int errCode, OpreateType type, OnUdbAuthListener listener) {
			InnerRunnable r = new InnerRunnable();
			r.errCode = errCode;
			r.listener = listener;
			r.call = Call.onError;
			return r;
		}

		@Override
		public void run() {
			if (listener == null)
				return;

			switch (call) {
			case onSuccess:
				listener.onSuccess(event, type);
				break;
			case onCancel:
				listener.onCancel(type);
				break;
			case onError:
				listener.onError(errCode, type);
			}
		}
	}

}
