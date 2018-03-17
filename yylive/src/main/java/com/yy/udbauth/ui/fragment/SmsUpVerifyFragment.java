package com.yy.udbauth.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.widget.UdbDialog;

import org.json.JSONObject;

/**
 * 概述：上行短信验证页面
 * <p>
 * 	说明：上行短信和下行短信统称为手机短信验证。 最初开发的时候完全没有提到上行短信，搞到后面的设计有点恶心，很难理解。
 * </p>
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年3月9日 下午3:57:10
 */
public class SmsUpVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	private static final String KEY_NEXTVERIFY = "key_nextverify";
	private static final String KEY_USER = "key_user";
	private static final String KEY_PASSWORD = "key_password";
	private static final String KEY_OTHERVERIFY = "key_otherverify";
	private static final String KEY_HAS_GOTO_SMS_APP = "key_has_goto_sms_app";
	View mMainView;
	Button mBtnGotoSMSApp;
	TextView mTvMyAnotherPhoneHasSent;
	TextView mTvTitle;

	//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
	String mRequestContext = null;
	//二次验证的内容
	NextVerify mNextVerify;
	//用户输入的用户名
	String mUsername;
	//用户输入的密码（使用Sha1加密）
	String mPasswdSha1;
	//短信发送目标号码
	String mSmsSendTo;
	//短信发送内容
	String mSmsContent;

	//用于切换的二次验证（一般为下行短信）
	NextVerify mOtherVerifyToSwitch;

	//是否已经调用过短信APP了
	boolean hasGotoSmsApp = false;

	/**
	 * 当Fragment被摧毁并重建的时候，Android比较调用无参构建函数，如果没有无参构建函数，会崩溃。<br/>
	 * 所以这里保留无参数构建函数，并提供该方法来设置初始参数
	 */
	public void setConstructorParams(NextVerify v, String username, String passwdSha1, NextVerify otherVerifyToSwitch) {
		mNextVerify = v;
		mUsername = username;
		mPasswdSha1 = passwdSha1;
		mOtherVerifyToSwitch = otherVerifyToSwitch;

		try {
			JSONObject jo = new JSONObject(mNextVerify.promptContent);
			mSmsSendTo = jo.optString("gateway");
			mSmsContent = jo.optString("code");
		} catch (Exception e) {

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(KEY_NEXTVERIFY, mNextVerify);
		outState.putString(KEY_USER, mUsername);
		outState.putString(KEY_PASSWORD, mPasswdSha1);
		outState.putSerializable(KEY_OTHERVERIFY, mOtherVerifyToSwitch);
		outState.putBoolean(KEY_HAS_GOTO_SMS_APP, hasGotoSmsApp);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState == null) {
			super.onCreate(savedInstanceState);
			return;
		}

		mNextVerify = (NextVerify) savedInstanceState.getSerializable(KEY_NEXTVERIFY);
		mUsername = savedInstanceState.getString(KEY_USER);
		mPasswdSha1 = savedInstanceState.getString(KEY_PASSWORD);
		mOtherVerifyToSwitch = (NextVerify) savedInstanceState.getSerializable(KEY_OTHERVERIFY);
		hasGotoSmsApp = savedInstanceState.getBoolean(KEY_HAS_GOTO_SMS_APP, hasGotoSmsApp);

		try {
			JSONObject jo = new JSONObject(mNextVerify.promptContent);
			mSmsSendTo = jo.optString("gateway");
			mSmsContent = jo.optString("code");
		} catch (Exception e) {

		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_upsms_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mBtnGotoSMSApp = (Button) mMainView.findViewById(R.id.ua_fragment_verify_btn_goto_sms_app);
		mTvMyAnotherPhoneHasSent = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_btn_has_sent);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle);
		mBtnGotoSMSApp.setOnClickListener(onGotoSMSAppClickListener);
		mTvMyAnotherPhoneHasSent.setOnClickListener(onHasSentClickListener);

		//设置标题
		setTitleBarText(R.string.ua_title_second_verify);

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustButtonStyle(mBtnGotoSMSApp);
		adjustDefaultTextStyle(mTvTitle);
		adjustStrikingTextStyle(mTvMyAnotherPhoneHasSent);
	}

	@Override
	public boolean onBackPressed() {
		// 按返回的时候，如果下行短信验证不为空，那么切换到下行短信
		if (getParentFragment() instanceof OnNextVerifyResultListener && mOtherVerifyToSwitch != null) {
			((OnNextVerifyResultListener) getParentFragment()).onSwitchVerify(mOtherVerifyToSwitch);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onResume() {
		if (hasGotoSmsApp) {
			checkUpSms();
			hasGotoSmsApp = false;
		}
		super.onResume();
	}

	public void checkUpSms() {
		//发送验证
		mRequestContext = Long.toString(System.currentTimeMillis());
		AuthRequest.CheckSmsUpReq auth = new AuthRequest.CheckSmsUpReq(mUsername, mPasswdSha1, mRequestContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_checking, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					AuthRequest.CancelReq req = new AuthRequest.CancelReq(mRequestContext);
					sendAuthRequestSilent(req);
					mRequestContext = null;
				}
			});
		}
	}

	/** 提交token*/
	OnClickListener onGotoSMSAppClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			try {
				//跳转到短信APP
				Uri smsToUri = Uri.parse("smsto:" + mSmsSendTo);
				Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
				intent.putExtra("sms_body", mSmsContent);
				startActivity(intent);

				hasGotoSmsApp = true;
			} catch (Exception e) {
				showShortToast(R.string.ua_no_sms_app_detected);
			}
		}
	};

	OnClickListener onHasSentClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			checkUpSms();
		}
	};

	@Override
	protected void onLoginEvent(AuthEvent.LoginEvent et) {
		if (mRequestContext == null || !mRequestContext.equals(et.context)) {
			return;
		}

		//隐藏进度对话框
		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {

			//重置倒计时
			CountdownHelper.resetSmsCode();

			//登录成功 
			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onLoginSuccess(et, mNextVerify.strategy);
			}

		} else if (et.uiAction == AuthEvent.UIAction.SERVER_HAS_NOT_RECEIVED_SMS) {
			//继续检查
			if (getActivity() == null) {
				showLongToast(R.string.ua_login_failed_with_no_sms_up);
			} else {
				UdbDialog.Builder adb = new UdbDialog.Builder(getActivity());
				adb.setTitle(R.string.ua_a_tip);
				adb.setMessage(R.string.ua_login_failed_with_no_sms_up);
				adb.setNegativeButton(R.string.ua_dialog_cancel, null);
				adb.setPositiveButton(R.string.ua_dialog_recheck, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						checkUpSms();
					}
				});
				adb.create().show();
			}

		} else {
			//登录失败
			//showLongToast(R.string.ua_login_failed, et.description);
			showLongToast(et.description);
			finish();

		}
	};

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mRequestContext != null && mRequestContext.equals(et.context)) {
			showShortToast(R.string.ua_timeout_check_upsms);
		}
	}

	@Override
	public void onTokenError() {

	}
}
