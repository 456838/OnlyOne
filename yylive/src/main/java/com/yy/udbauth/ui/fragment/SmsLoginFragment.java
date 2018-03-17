package com.yy.udbauth.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.activity.UdbAuthActivity;
import com.yy.udbauth.ui.info.PageSetting;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.OpreateType;
import com.yy.udbauth.ui.widget.UdbButton;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 概述：手机短信快速登录
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月19日 下午10:02:54
 */
public class SmsLoginFragment extends UdbAuthBaseFragment {

	private static final String SP_FILENAME = "udbusaveiauthsm";
	private static final String KEY_MOBILE = "893jkd";
	private static final String PERFIX = "818";

	//记录上次获取短信的时间
	public static long LAST_TIME_GET_SMS_CODE = 0L;
	//两次获取短信的时间间隔，单位为毫秒
	private static int TIME_INTERVAL_GET_SMS_CODE = 60000;

	View mMainView;
	UdbEditText mEtMobile;
	UdbEditText mEtSmsCode;
	TextView mTvPasswordLogin;
	UdbButton mBtnGetSmsCode;
	Button mBtnLogin;

	String mMobile; //登录手机号码
	String mSmsCode;//登录手机短信验证码

	String mSendSMSContext;//下发短信业务流水号
	String mSmsLoginContext;//短信登录业务流水号
	//是否已经通知业务操作结果了
	boolean hasCallback = false;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && shouldHandle() && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.SMS_LOGIN) {
			AuthCallbackProxy.onCancel(OpreateType.SMS_LOGIN);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_sms_login;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mEtMobile = (UdbEditText) mMainView.findViewById(R.id.ua_sms_login_et_mobile);
		mEtSmsCode = (UdbEditText) mMainView.findViewById(R.id.ua_sms_login_et_smscode);
		mTvPasswordLogin = (TextView) mMainView.findViewById(R.id.ua_sms_login_btn_password_login);
		mBtnGetSmsCode = (UdbButton) mMainView.findViewById(R.id.ua_sms_login_btn_get_sms_code);
		mBtnLogin = (Button) mMainView.findViewById(R.id.ua_sms_login_btn_login);

		//设置标题
		setTitleBarText(R.string.ua_title_sms_login);

		//设置倒计时按钮
		mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
				getString(R.string.ua_sms_login_btn_get_sms_code),
				getString(R.string.ua_sms_login_btn_get_sms_code_disable));

		mEtMobile.bindCleanButton(R.id.ua_sms_login_btn_clear_mobile);
		mEtSmsCode.bindCleanButton(R.id.ua_sms_login_btn_clear_sms_code);

		mTvPasswordLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 判断是不是从手机短信登录过来的，如果是，结束当前页面即可，这样可以避免循环打开
				UdbAuthActivity callingActivity = FragmentHelper.getContainActivity(LoginFragment.class);
				if (callingActivity == null) {
					startFragmentForResult(LoginFragment.class);
				} else {
					finish();
				}
			}
		});

		mBtnGetSmsCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGetSmsCodeClick(v);
			}
		});

		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginClick(v);
			}
		});

		readLastLoginMobile();
		mEtMobile.setText(mMobile);

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {

		// 适配样式效果
		adjustButtonStyle(mBtnLogin);
		adjustButtonStyle(mBtnGetSmsCode);
		adjustDefaultTextStyle(mTvPasswordLogin);

		final PageSetting es = AuthUI.getInstance().getPageSetting();
		setupViewVisable(mMainView, R.id.ua_sms_login_btn_password_login, es.smsLoginPage_showPasswordLogin);
	}

	/** 保存为最近登录的手机号码*/
	private void saveLastLoginMobile() {
		SharedPreferences sp = getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		sp.edit().putString(KEY_MOBILE, PERFIX + mMobile).commit();
	}

	/** 读取最近登录的手机号码*/
	private void readLastLoginMobile() {
		SharedPreferences sp = getContext().getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		mMobile = sp.getString(KEY_MOBILE, "");
		if (mMobile.startsWith(PERFIX)) {
			mMobile = mMobile.substring(PERFIX.length());
		}
	}

	/** 下发短信验证码*/
	public void onGetSmsCodeClick(View v) {

		mMobile = mEtMobile.getText().toString().trim();

		if (TextUtils.isEmpty(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_empty_mobile);
			return;
		}

		//支持海外手机号码，这里手机号码判断有特殊性
		if (!AndroidHelper.isNationalMobile(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_invalid_mobile);
			return;
		}

		//调用下发短信验证码接口
		mSendSMSContext = Long.toString(System.currentTimeMillis());//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.SendSmsReq auth = new AuthRequest.SendSmsReq(mMobile,
				AuthRequest.SendSmsReq.SMS_TYPE_REGISTER_OR_LOGIN, STRATEGY.NONE, null, mSendSMSContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，记录下发短信的时间，并设置倒计时按钮
			LAST_TIME_GET_SMS_CODE = System.currentTimeMillis();
			mBtnGetSmsCode.setEnabled(false);
			mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
					getString(R.string.ua_sms_login_btn_get_sms_code),
					getString(R.string.ua_sms_login_btn_get_sms_code_disable));

			showProgressDialog(R.string.ua_requesting_sms_code, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSendSMSContext = null;
				}
			});
		}
	}

	/** 提交*/
	public void onLoginClick(View v) {

		mMobile = mEtMobile.getText().toString().trim();
		mSmsCode = mEtSmsCode.getText().toString().trim();

		if (TextUtils.isEmpty(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_empty_mobile);
			return;
		}

		//支持海外手机号码，这里手机号码判断有特殊性
		if (!AndroidHelper.isNationalMobile(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_invalid_mobile);
			return;
		}

		if (TextUtils.isEmpty(mSmsCode)) {
			mEtSmsCode.requestFocus();
			showShortToast(R.string.ua_empty_sms_code);
			return;
		}

		//调用短信登录接口
		mSmsLoginContext = Long.toString(System.currentTimeMillis());
		AuthRequest.SmsRegloginReq auth = new AuthRequest.SmsRegloginReq(mMobile, mSmsCode, mSmsLoginContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_requesting, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSmsLoginContext = null;
				}
			});
		}
	}

	/** 接收到下发短信回调事件*/
	@Override
	protected void onSmsCodeEvent(AuthEvent.SendSmsEvent et) {

		if (mSendSMSContext == null || !mSendSMSContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showLongToast(R.string.ua_send_sms_success);
			mEtSmsCode.requestFocus();
			mBtnLogin.setEnabled(true);
		} else {
			LAST_TIME_GET_SMS_CODE = 0L;
			mBtnGetSmsCode.cancelCountdown();
			//showLongToast(R.string.ua_send_sms_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 处理登录结果*/
	@Override
	protected void onLoginEvent(AuthEvent.LoginEvent et) {

		if (mSmsLoginContext == null || !mSmsLoginContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {

			//重置倒计时
			CountdownHelper.resetSmsCode();

			saveLastLoginMobile();

			//登录成功 
			showShortToast(R.string.ua_sms_logining);

			//回调，不管是什么情况下，登录成功就回调
			et.user = mMobile;
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.SMS_LOGIN);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else {
			//登录失败
			//showShortToast(R.string.ua_login_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}

	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mSendSMSContext != null && mSendSMSContext.equals(et.context)) {
			showProgressDialog(null, null);
			showShortToast(R.string.ua_timeout_send_sms_code);
		} else if (mSmsLoginContext != null && mSmsLoginContext.equals(et.context)) {
			showProgressDialog(null, null);
			showShortToast(R.string.ua_timeout_login);
		}
	}

}
