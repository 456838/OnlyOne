package com.yy.udbauth.ui.fragment;

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
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.widget.UdbButton;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 
 * 概述：短信验证码页面（下行短信）
 * <p>
 * 	说明：上行短信和下行短信统称为手机短信验证。 最初开发的时候完全没有提到上行短信，搞到后面的设计有点恶心，很难理解。
 * </p>
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年11月6日 下午4:29:03
 * @update 2016-4-20 使之支持UID下发短信验证码
 */
public class SmsVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	//记录上次获取短信的时间
	public static long LAST_TIME_GET_SMS_CODE = 0L;
	//两次获取短信的时间间隔，单位为毫秒
	private static int TIME_INTERVAL_GET_SMS_CODE = 60000;

	View mMainView;
	UdbButton mBtnGetSmsCode;
	Button mBtnSubmit;
	TextView mTvGotoSMSUp;
	TextView mTvTitle;
	UdbEditText mEtToken;

	//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
	String mRequestContext = null;
	//二次验证的内容
	NextVerify mNextVerify;
	//用于切换的二次验证（一般为上行短信）
	NextVerify mOtherVerifyToSwitch;
	//用户输入的用户名
	String mUsername;
	//用户类型
	boolean mIsUid;

	boolean hasAutoSendSms = false;

	public SmsVerifyFragment(NextVerify v, String user, boolean isUid, NextVerify otherVerifyToSwitch) {
		mNextVerify = v;
		mUsername = user;
		mIsUid = isUid;
		mOtherVerifyToSwitch = otherVerifyToSwitch;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_sms_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mBtnGetSmsCode = (UdbButton) mMainView.findViewById(R.id.ua_fragment_verify_btn_get_verify);
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_fragment_verify_btn_ok);
		mEtToken = (UdbEditText) mMainView.findViewById(R.id.ua_fragment_verify_et_token);
		mTvGotoSMSUp = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_btn_goto_sms_up);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle + "" + mNextVerify.promptContent);
		mBtnGetSmsCode.setOnClickListener(onGetVerifyClickListener);
		mTvGotoSMSUp.setOnClickListener(onGotoSmsUpClickListener);
		mBtnSubmit.setOnClickListener(onSubmitClickListener);
		mEtToken.bindCleanButton(R.id.ua_fragment_verify_btn_clear_token);
		mEtToken.setHint(mNextVerify.selectTitle);

		//设置标题
		setTitleBarText(R.string.ua_title_second_verify);

		//设置倒计时按钮
		mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
				getString(R.string.ua_verify_btn_get_sms_code), getString(R.string.ua_verify_btn_get_sms_code_disable));

		//自动下发短信验证码
		if (!hasAutoSendSms && System.currentTimeMillis() > LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE) {
			onGetVerifyClickListener.onClick(mBtnGetSmsCode);
			hasAutoSendSms = true;
		}

		if (mOtherVerifyToSwitch != null) {
			mTvGotoSMSUp.setVisibility(View.VISIBLE);
		} else {
			mTvGotoSMSUp.setVisibility(View.GONE);
		}

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustButtonStyle(mBtnGetSmsCode);
		adjustButtonStyle(mBtnSubmit);
		adjustDefaultTextStyle(mTvTitle);
		adjustStrikingTextStyle(mTvGotoSMSUp);
	}

	/** 请求下发验证码短信*/
	OnClickListener onGetVerifyClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//调用下发短信验证码接口
			mRequestContext = Long.toString(System.currentTimeMillis());
			AuthRequest.SendSmsReq auth;
			if (mIsUid) {
				auth = new AuthRequest.SendSmsReq(mUsername, STRATEGY.NONE, null, mRequestContext);
			} else {

				auth = new AuthRequest.SendSmsReq(mUsername, null, STRATEGY.NONE, null, mRequestContext);
			}

			if (sendAuthRequest(auth)) {
				//发送请求成功，记录下发短信的时间，并设置倒计时按钮
				LAST_TIME_GET_SMS_CODE = System.currentTimeMillis();
				mBtnGetSmsCode.setEnabled(false);
				mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE
						+ TIME_INTERVAL_GET_SMS_CODE, getString(R.string.ua_verify_btn_get_sms_code),
						getString(R.string.ua_verify_btn_get_sms_code_disable));
			}
		}
	};

	OnClickListener onGotoSmsUpClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// 切换到其它验证方式，一般为上行短信
			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onSwitchVerify(mOtherVerifyToSwitch);
			}
		}
	};

	/** 提交token*/
	OnClickListener onSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String token = mEtToken.getText().toString().trim();

			if (TextUtils.isEmpty(token)) {
				showShortToast(R.string.ua_empty_sms_code);
				return;
			}

			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onVerifyResult(token, mNextVerify.strategy);
			}
		}
	};

	/** 接收到服务端返回的消息 */
	@Override
	protected void onSmsCodeEvent(AuthEvent.SendSmsEvent et) {

		if (mRequestContext == null || !mRequestContext.equals(et.context)) {
			return;
		}

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showShortToast(R.string.ua_send_sms_success);
		} else {
			LAST_TIME_GET_SMS_CODE = 0L;
			mBtnGetSmsCode.cancelCountdown();
			//showShortToast(R.string.ua_send_sms_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mRequestContext != null && mRequestContext.equals(et.context)) {
			showShortToast(R.string.ua_timeout_send_sms_code);
		}
	}

	@Override
	public void onTokenError() {
		showShortToast(R.string.ua_login_failed_with_err_smscode);
	}
}
