package com.yy.udbauth.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.AuthBaseEvent;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.log.LogHelper;
import com.yy.udbauth.proto.IProtoMagager;
import com.yy.udbauth.ui.activity.UdbAuthActivity;
import com.yy.udbauth.ui.fragment.FindMyPasswordFragment;
import com.yy.udbauth.ui.fragment.LoginFragment;
import com.yy.udbauth.ui.fragment.ModifyPasswordFragment;
import com.yy.udbauth.ui.fragment.RegisterFragment;
import com.yy.udbauth.ui.fragment.SmsLoginFragment;
import com.yy.udbauth.ui.fragment.VerifyFragment;
import com.yy.udbauth.ui.info.LayoutRes;
import com.yy.udbauth.ui.info.PageSetting;
import com.yy.udbauth.ui.style.FindMyPasswordPageStyle;
import com.yy.udbauth.ui.style.LoginPageStyle;
import com.yy.udbauth.ui.style.ModifyPasswordPageStyle;
import com.yy.udbauth.ui.style.NextVerifyPageStyle;
import com.yy.udbauth.ui.style.PageStyle;
import com.yy.udbauth.ui.style.RegisterPageStyle;
import com.yy.udbauth.ui.style.SmsLoginPageStyle;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.OnCreditLoginListener;
import com.yy.udbauth.ui.tools.OnCreditRefreshListener;
import com.yy.udbauth.ui.tools.OnUdbAuthListener;
import com.yy.udbauth.ui.tools.OpreateType;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 概述：UI通用库主要接口
 * <p>
 * <li>AuthUI.getInstance().init(...);</li>
 * <li>AuthUI.getInstance().show..();</li>
 * <li>AuthUI.getInstance().loginWith..();</li>
 * </p>
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月18日 下午5:38:52
 */
public class AuthUI {

	/** 上下文*/
	protected Context mContext;
	/** 账户数据库代理*/
	private AuthDatabaseProxy mAuthDatabaseProxy;
	/** 收发包管理器*/
	private IProtoMagager mIProtoMagager;
	/** 凭证自动刷新监听器*/
	private OnCreditRefreshListener mOnCreditRefreshListener = null;
	/** 凭证登录回调监听器*/
	private OnCreditLoginListener mOnCreditLoginListener;
	/** 全局样式表*/
	private PageStyle mGlobalPageStyle = null;
	/** 布局配置*/
	private LayoutRes mLayoutRes = new LayoutRes();
	/** 页面入口配置*/
	private PageSetting mPageSetting = new PageSetting();
	/** 用于凭证登录的请求上下文标识*/
	private String mRequestContext = null;

	private String mAppId = null;

	/** 单例模式*/
	private static AuthUI sAuthUI;

	private static boolean isInit = false;

	private AuthUI() {

	}

	/** 获取实例*/
	public static AuthUI getInstance() {
		if (sAuthUI == null) {
			synchronized (AuthUI.class) {
				sAuthUI = new AuthUI();
			}
		}
		return sAuthUI;
	}

	/** 
	 * 初始化UI库
	 * 
	 * @param context	建议使用getApplicationContext()
	 * @param appId		业务APPID，请到UDB组注册
	 * @param appKey	业务APPKEY，请到UDB组注册
	 * @param terminalType	终端类型标识，即以前YYSDK的AppInfo.terminalType，请向信令组注册。若以前未使用过，可填写“0”
	 * @param enableAnonymousLogin	是否开启匿名登录，仅对进频道类SDK有效，其它情况请忽略
	 * @param listener	凭证自动刷新监听器
	 * @return 返回是否初始化成功，请参看{@link InitCode}
	 */
	public int init(Context context, String appId, String appKey, String terminalType, boolean enableAnonymousLogin,
			OnCreditRefreshListener listener) {

		try {
			if (isInit) {
				return InitCode.SUCCESS;
			}

			mContext = context.getApplicationContext();
			mOnCreditRefreshListener = listener;
			mAuthDatabaseProxy = new AuthDatabaseProxy(context);

			//获取收发包管理器实例，并初始化SDK
			mIProtoMagager = IProtoMagager.getDefault();
			int code = mIProtoMagager.init(context, appId, appKey, terminalType, enableAnonymousLogin);

			if (code != InitCode.SUCCESS) {
				return code;
			}

			//添加回调事件监听器
			mIProtoMagager.addAuthEventWatcher(mCreditEventHandler);

			mAppId = appId;
			isInit = true;

			//初始化日志上报
			JSONObject j = new JSONObject();
			j.put("uiver", UIConstants.UI_VERSION);
			j.put("type", mIProtoMagager.getName() + "");
			LogHelper.getInstance().logToDBForCommon(UIConstants.LOG_TYPE, LogHelper.LEVEL_INFO, appId, "", "", "",
					"0", j.toString());

			return InitCode.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			return InitCode.UNKNOWN_EXCEPTION;
		} catch (Error e) {
			e.printStackTrace();
			return InitCode.UNKNOWN_ERROR;
		}
	}

	/** 
	 * 对外接口，业务方调用；打开账号密码登录页面
	 * @param activity	一个Activity
	 * @param listener	操作结果监听器
	 */
	public void showLoginActivity(Activity activity, OnUdbAuthListener listener) {
		showLoginActivity(activity, null, listener);
	}

	/** 
	 * 对外接口，业务方调用；打开账号密码登录页面 
	 * @param activity	一个Activity
	 * @param style		页面样式表
	 * @param listener	操作结果监听器
	 */
	public void showLoginActivity(Activity activity, LoginPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.PWD_LOGIN, mAppId);

		if (style != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
			FragmentHelper.startFragment(activity, LoginFragment.class, bundle);
		} else {

			FragmentHelper.startFragment(activity, LoginFragment.class);
		}
	}

	/** 
	 * 对外接口，业务方调用；打开手机短信登录页面
	 * @param activity	一个Activity
	 * @param listener	操作结果监听器
	 */
	public void showSmsLoginActivity(Activity activity, OnUdbAuthListener listener) {
		showSmsLoginActivity(activity, null, listener);
	}

	/** 
	 * 对外接口，业务方调用；打开手机短信登录页面
	 * @param activity	一个Activity
	 * @param style		页面样式表
	 * @param listener	操作结果监听器
	 */
	public void showSmsLoginActivity(Activity activity, SmsLoginPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.SMS_LOGIN, mAppId);

		if (style != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
			FragmentHelper.startFragment(activity, SmsLoginFragment.class, bundle);
		} else {

			FragmentHelper.startFragment(activity, SmsLoginFragment.class);
		}
	}

	/** 
	 * 对外接口，业务方调用；打开找回密码页面
	 * @param activity	一个Activity
	 * @param listener	操作结果监听器
	 */
	public void showFindMyPasswordActivity(Activity activity, OnUdbAuthListener listener) {
		showFindMyPasswordActivity(activity, null, listener);
	}

	/** 
	 * 对外接口，业务方调用；打开找回密码页面 
	 * @param activity	一个Activity
	 * @param style		页面样式表
	 * @param listener	操作结果监听器
	 */
	public void showFindMyPasswordActivity(Activity activity, FindMyPasswordPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.FIND_MY_PWD, mAppId);

		if (style != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
			FragmentHelper.startFragment(activity, FindMyPasswordFragment.class, bundle);
		} else {

			FragmentHelper.startFragment(activity, FindMyPasswordFragment.class);
		}
	}

	/** 
	 * 对外接口，业务方调用；打开修改密码页面 
	 * @param activity	一个Activity
	 * @param user	需要执行修改密码的帐号。<strong>注意该帐号必需处于登录状态才可以修改密码。如果不是登录状态，请调用找回密码接口。</strong>
	 * @param listener	操作结果监听器
	 */
	public void showModifyMyPasswordActivity(Activity activity, String user, OnUdbAuthListener listener) {
		showModifyMyPasswordActivity(activity, null, user, listener);
	}

	/** 
	 * 对外接口，业务方调用；打开修改密码页面 
	 * @param activity	一个Activity
	 * @param style		页面样式表
	 * @param user	需要执行修改密码的帐号。<strong>注意该帐号必需处于登录状态才可以修改密码。如果不是登录状态，请调用找回密码接口。</strong>
	 * @param listener	操作结果监听器
	 */
	public void showModifyMyPasswordActivity(Activity activity, ModifyPasswordPageStyle style, String user,
			OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.MODIFY_PWD, mAppId);

		if (style != null || user != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
			bundle.putString(ModifyPasswordFragment.EXTRA_USER, user);
			FragmentHelper.startFragment(activity, ModifyPasswordFragment.class, bundle);
		} else {

			FragmentHelper.startFragment(activity, ModifyPasswordFragment.class);
		}
	}

	/** 
	 * 对外接口，业务方调用；打开手机短信注册页面
	 * @param activity	一个Activity
	 * @param listener	操作结果监听器
	 */
	public void showSmsRegisterActivity(Activity activity, OnUdbAuthListener listener) {
		showSmsRegisterActivity(activity, null, listener);
	}

	/** 
	 * 对外接口，业务方调用；打开手机短信注册页面
	 * @param activity	一个Activity
	 * @param style		页面样式表
	 * @param listener	操作结果监听器
	 */
	public void showSmsRegisterActivity(Activity activity, RegisterPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.SMS_REGISTER, mAppId);

		if (style != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
			FragmentHelper.startFragment(activity, RegisterFragment.class, bundle);
		} else {

			FragmentHelper.startFragment(activity, RegisterFragment.class);
		}
	}

	/**
	 * 对外接口，业务方调用；打开二次验证页面，并返回登录信息
	 * @param activity	一个Activity
	 * @param uid		用户标识UID
	 * @param credit	登录凭证
	 * @param nextVerifies	一个或多个二次验证策略
	 * @param listener	回调监听器
	 */
	public void showNextVerifyActivityForCreditLogin(Activity activity, String uid, String credit,
			ArrayList<NextVerify> nextVerifies, OnUdbAuthListener listener) {
		this.showNextVerifyActivityForCreditLogin(activity, uid, credit, nextVerifies, null, listener);
	}

	/**
	 * 对外接口，业务方调用；打开二次验证页面，并返回登录信息
	 * @param activity	一个Activity
	 * @param uid		用户标识UID
	 * @param credit	登录凭证
	 * @param nextVerifies	一个或多个二次验证策略
	 * @param style	页面样式表
	 * @param listener	回调监听器
	 */
	public void showNextVerifyActivityForCreditLogin(Activity activity, String uid, String credit,
			ArrayList<NextVerify> nextVerifies, NextVerifyPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.NEXT_VERIFY, mAppId);

		Bundle bundle = new Bundle();
		bundle.putInt(VerifyFragment.EXTRA_LOGIN_TYPE, VerifyFragment.LOGIN_BY_CREDIT);
		bundle.putString(VerifyFragment.EXTRA_UID, uid);
		bundle.putString(VerifyFragment.EXTRA_CREDIT, credit);
		bundle.putSerializable(VerifyFragment.EXTRA_STRATEGIES, nextVerifies);

		if (style != null) {
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
		}

		FragmentHelper.startFragment(activity, VerifyFragment.class, bundle);
	}

	/**
	 * 对外接口，业务方调用；打开二次验证页面
	 * @param activity	一个Activity
	 * @param user		账号，支持通行证、手机号、YY号、邮箱
	 * @param passwdSha1	使用SHA1加密后的字符串
	 * @param nextVerifies	一个或多个二次验证策略
	 * @param listener	回调监听器
	 */
	public void showNextVerifyActivityForPwdLogin(Activity activity, String user, String passwdSha1,
			ArrayList<NextVerify> nextVerifies, OnUdbAuthListener listener) {
		this.showNextVerifyActivityForPwdLogin(activity, user, passwdSha1, nextVerifies, null, listener);
	}

	/**
	 * 对外接口，业务方调用；打开二次验证页面
	 * @param activity	一个Activity
	 * @param user		账号，支持通行证、手机号、YY号、邮箱
	 * @param passwdSha1	使用SHA1加密后的字符串，可用函数{@link AuthSDK#getPasswdSha1(String)}
	 * @param nextVerifies	一个或多个二次验证策略
	 * @param style	页面样式表
	 * @param listener	回调监听器
	 */
	public void showNextVerifyActivityForPwdLogin(Activity activity, String user, String passwdSha1,
			ArrayList<NextVerify> nextVerifies, NextVerifyPageStyle style, OnUdbAuthListener listener) {
		AuthCallbackProxy.setOnUdbAuthListener(listener, OpreateType.NEXT_VERIFY, mAppId);

		Bundle bundle = new Bundle();
		bundle.putInt(VerifyFragment.EXTRA_LOGIN_TYPE, VerifyFragment.LOGIN_BY_PASSWORD);
		bundle.putString(VerifyFragment.EXTRA_USERNAME, user);
		bundle.putString(VerifyFragment.EXTRA_PASSWD_SHA1, passwdSha1);
		bundle.putSerializable(VerifyFragment.EXTRA_STRATEGIES, nextVerifies);

		if (style != null) {
			bundle.putSerializable(UdbAuthActivity.EXTRA_PAGE_STYLE, style);
		}

		FragmentHelper.startFragment(activity, VerifyFragment.class, bundle);
	}

	/** 
	 * 设置全局样式表，如果不需要，请设置为null
	 */
	public void setGlobalPageStyle(PageStyle ps) {
		mGlobalPageStyle = ps;
	}

	/** 
	 * 获取全局样式表
	 */
	public PageStyle getGlobalPageStyle() {
		return mGlobalPageStyle;
	}

	/** 
	 * 配置自定义布局，可以设置一个或多个自定义布局
	 * @param ls	如果为null，则表示恢复默认状态
	 */
	public void setLayoutRes(LayoutRes ls) {
		if (ls == null) {
			resetLayoutRes();
		} else {
			mLayoutRes = ls;
		}
	}

	/** 
	 * 获取当前布局配置
	 */
	public LayoutRes getLayoutRes() {
		return mLayoutRes;
	}

	/** 
	 * 恢复默认的布局
	 */
	public void resetLayoutRes() {
		mLayoutRes = new LayoutRes();
	}

	/**
	 * 获取页面的配置
	 */
	public PageSetting getPageSetting() {
		return mPageSetting;
	}

	/** 
	 * 自定义页面配置，比如说：显示/隐藏短信登录的“账号密码登录”入口
	 * @param es	如果为空，则恢复默认配置
	 */
	public void setPageSetting(PageSetting es) {
		if (es == null) {
			es = new PageSetting();
		} else {
			mPageSetting = es;
		}
	}

	/**
	 * 恢复默认的页面配置
	 */
	public void resetPageSetting() {
		mPageSetting = new PageSetting();
	}

	/**
	 * 干掉UI库所有的Activity，可能会收到onCancel的回调
	 */
	public void finishAllActivity() {
		UdbAuthActivity.finishAll();
	}

	/** 
	 * 返回数据库代理，用于操作账户信息
	 */
	public AuthDatabaseProxy getDatabase() {
		return mAuthDatabaseProxy;
	}

	/** 
	 * 增加一个回调事件接口，可以监听所有回调事件 
	 * @param listener	回调接口
	 */
	public void addAuthEventWatcher(IAuthEventWatcher listener) {
		if (mIProtoMagager != null) {
			mIProtoMagager.addAuthEventWatcher(listener);
		}
	}

	/** 
	 * 移除一个回调事件接口
	 */
	public void removeAuthEventWatcher(IAuthEventWatcher listener) {
		if (mIProtoMagager != null) {
			mIProtoMagager.removeAuthEventWatcher(listener);
		}
	}

	/**
	 * 发送一个Auth请求
	 * @param auth	将要被发送出去的请求
	 * @return	返回是否发送成功，0表示成功，其它情况，请参考相关文档
	 */
	public int sendAuthRequest(AuthRequest.AuthBaseReq auth) {
		if (mIProtoMagager != null) {
			return mIProtoMagager.sendAuthRequest(auth);
		} else {
			return -43;
		}
	}

	/**
	 * 发送一个Auth请求，如果发送失败，则弹出一个Toast，显示相关错误信息，如果成功则不显示Toast
	 * @param auth	将要被发送出去的请求
	 * @return	返回是否发送成功
	 */
	public boolean sendAuthRequestWithToast(AuthRequest.AuthBaseReq auth) {
		if (mIProtoMagager != null) {
			return mIProtoMagager.sendAuthRequestWithToast(auth);
		} else {
			return false;
		}
	}

	/** 
	 * 使用uid和凭证进行快速登录（无界面，也叫静默登录）
	 * @param uid	账户uid
	 * @param credit	账户登录凭证
	 * @param listener	回调监听器
	 * @return 返回是否发送请求成功，0表示成功，其它情况，请参考相关文档
	 */
	public int loginWithCredit(String uid, String credit, OnCreditLoginListener listener) {
		mOnCreditLoginListener = listener;
		mRequestContext = Long.toString(System.currentTimeMillis()); //上下文（流水号）
		AuthRequest.CreditLoginReq auth = new AuthRequest.CreditLoginReq(uid, credit, STRATEGY.NONE, null,
				mRequestContext);

		//将Auth数据转为byte[]再发送出去
		return sendAuthRequest(auth);
	}

	/** 
	 * 注销，让SDK清空登录状态。对于进频道类APP，调用此方法可切换到匿名用户，保证可以正常进出频道。
	 */
	public void logout() {
		if (mIProtoMagager != null) {
			mIProtoMagager.logout();
		}
	}

	/** 获取业务所设备的APPID*/
	protected String getAppId() {
		return mAppId;
	}

	private IAuthEventWatcher mCreditEventHandler = new IAuthEventWatcher() {

		@Override
		public void onAuthRes(AuthBaseEvent base) {
			if (base instanceof AuthEvent.CreditRenewEvent) {
				//监听凭证自动刷新，这里需要更新保存在本地的凭证
				AuthEvent.CreditRenewEvent et = (AuthEvent.CreditRenewEvent) base;

				if (mOnCreditRefreshListener != null) {
					mOnCreditRefreshListener.onCreditRefresh(et);
				}
			} else if (mRequestContext != null && mOnCreditLoginListener != null
					&& base instanceof AuthEvent.TimeoutEvent) {
				//处理凭证登录超时事件
				AuthEvent.TimeoutEvent et = (AuthEvent.TimeoutEvent) base;

				if (mRequestContext.equals(et.context)) {
					mRequestContext = null;
					mOnCreditLoginListener.onTimeout();
				}
			} else if (mRequestContext != null && mOnCreditLoginListener != null
					&& base instanceof AuthEvent.LoginEvent) {
				//处理凭证登录回调事件
				AuthEvent.LoginEvent et = (AuthEvent.LoginEvent) base;

				if (mRequestContext.equals(et.context)) {
					mRequestContext = null;
					mOnCreditLoginListener.onResult(et);
				}
			}
		}

		@Override
		public void onLoginAPFalied(int uSrvResCode) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onKickOff(int code, String reason) {
			// TODO Auto-generated method stub

		}
	};
}
