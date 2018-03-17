package com.yy.udbauth.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.LoginEvent;
import com.yy.udbauth.AuthEvent.RegisterEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthEvent.UIAction;
import com.yy.udbauth.AuthEvent.VerifySmsCodeEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.info.PageSetting;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.CountryHelper.CountryInfo;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.OpreateType;
import com.yy.udbauth.ui.widget.UdbButton;
import com.yy.udbauth.ui.widget.UdbDialog;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 概述：手机短信注册页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月21日 上午10:34:27
 */
public class RegisterFragment extends UdbAuthBaseFragment {

	private final String URL_LICENSE = "http://zc.yy.com/license.html";

	private final static int REQUEST_CODE = 3234;

	//记录上次获取短信的时间
	public static long LAST_TIME_GET_SMS_CODE = 0L;
	//两次获取短信的时间间隔，单位为毫秒
	private static int TIME_INTERVAL_GET_SMS_CODE = 60000;

	View mMainView;
	View mCountryView;
	ViewFlipper mViewFlipper;
	UdbEditText mEtMobile;
	UdbEditText mEtSmsCode;
	UdbEditText mEtPassword;
	CheckBox mCbShowPassword;
	UdbButton mBtnGetSmsCode;
	Button mBtnGotoStep2;
	Button mBtnGotoStep3;
	Button mBtnSubmit;
	TextView mBtnLicense;
	TextView mTvCountryName;
	TextView mTvCountryNumber;
	CheckBox mCheckBox;

	String mMobile; //注册手机号码
	String mSmsCode;//注册手机短信验证码
	String mPassword;//注册密码

	String mCheckRegisterContext;//检查账号是否可用于注册业务流水号
	String mSendSMSContext;//下发短信业务流水号
	String mVerifySmsCodeContext;//短信注册业务流水号
	String mRegisterContext = null;//提供注册密码业务流水号
	//是否已经通知业务操作结果了
	boolean hasCallback = false;
	boolean mIsAutoLogin = false;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && shouldHandle() && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.SMS_REGISTER) {
			AuthCallbackProxy.onCancel(OpreateType.SMS_REGISTER);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_register;
		mMainView = inflater.inflate(layout, container, false);

		// 关联布局元素
		mViewFlipper = (ViewFlipper) mMainView.findViewById(R.id.ua_register_viewflipper);
		mCountryView = mMainView.findViewById(R.id.ua_register_country_layout);
		mEtMobile = (UdbEditText) mMainView.findViewById(R.id.ua_register_et_mobile);
		mEtSmsCode = (UdbEditText) mMainView.findViewById(R.id.ua_register_et_smscode);
		mEtPassword = (UdbEditText) mMainView.findViewById(R.id.ua_register_et_password);
		mCbShowPassword = (CheckBox) mMainView.findViewById(R.id.ua_register_cb_show_password);
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_register_btn_submit);
		mBtnGetSmsCode = (UdbButton) mMainView.findViewById(R.id.ua_register_btn_get_sms_code);
		mBtnGotoStep2 = (Button) mMainView.findViewById(R.id.ua_register_btn_goto_step2);
		mBtnGotoStep3 = (Button) mMainView.findViewById(R.id.ua_register_btn_goto_step3);
		mBtnLicense = (TextView) mMainView.findViewById(R.id.ua_register_btn_see_license);
		mTvCountryName = (TextView) mMainView.findViewById(R.id.ua_register_tv_country_name);
		mTvCountryNumber = (TextView) mMainView.findViewById(R.id.ua_register_tv_country_number);
		mCheckBox = (CheckBox) mMainView.findViewById(R.id.ua_register_cb_agree);

		//设置标题
		setTitleBarText(R.string.ua_title_register1);

		//设置倒计时按钮
		mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
				getString(R.string.ua_reg_btn_get_sms_code), getString(R.string.ua_reg_btn_get_sms_code_disable));

		mEtMobile.bindCleanButton(R.id.ua_register_btn_clear_mobile);
		mEtSmsCode.bindCleanButton(R.id.ua_register_btn_clear_sms_code);
		mEtPassword.bindCleanButton(R.id.ua_register_btn_clear_password);

		mCbShowPassword.setOnCheckedChangeListener(onPasswordVisableChangeListener);
		mCbShowPassword.setChecked(false);

		mBtnGetSmsCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendSmsCode();
			}
		});

		mBtnGotoStep2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGotoStep2Click(v);
			}
		});

		mBtnGotoStep3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGotoStep3Click(v);
			}
		});

		mBtnLicense.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//使用外部浏览器打开
				//Uri content_url = Uri.parse(URL_LICENSE);
				//Intent intent = new Intent();
				//intent.setAction(Intent.ACTION_VIEW);
				//intent.setData(content_url);
				//startActivity(intent);

				//使用内部浏览器打开
				Bundle bundle = new Bundle();
				bundle.putString(WebViewFragment.EXTRA_URL, URL_LICENSE);
				bundle.putString(WebViewFragment.EXTRA_TITLE, mBtnLicense.getText().toString());
				startFragmentForResult(WebViewFragment.class, bundle);
			}
		});

		mBtnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubmitClick(v);
			}
		});

		mCountryView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragmentForResult(CountrySelectFragment.class, REQUEST_CODE, null);
			}
		});

		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mBtnGotoStep2.setEnabled(isChecked);
			}
		});
		mCheckBox.setChecked(true);

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {

		// 适配样式效果
		adjustButtonStyle(mBtnGetSmsCode);
		adjustButtonStyle(mBtnGotoStep2);
		adjustButtonStyle(mBtnGotoStep3);
		adjustButtonStyle(mBtnSubmit);
		adjustStrikingTextStyle(mBtnLicense);

		final PageSetting es = AuthUI.getInstance().getPageSetting();
		mIsAutoLogin = es.smsRegisterPage_autoLogin;
	}

	@Override
	public boolean onBackPressed() {
		if (getActivity() != null && mViewFlipper.getDisplayedChild() > 0) {

			String msg = null;
			msg = String.format(getString(R.string.ua_abort_opreation), getString(R.string.ua_title_register1));

			UdbDialog.Builder dialog = new UdbDialog.Builder(getActivity());
			dialog.setTitle(R.string.ua_a_tip);
			dialog.setMessage(msg);
			dialog.setCancelable(true);
			dialog.setNegativeButton(R.string.ua_dialog_cancel, null);
			dialog.setPositiveButton(R.string.ua_dialog_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			dialog.create().show();

			return true;
		}

		return super.onBackPressed();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			CountryInfo info = (CountryInfo) data.getSerializableExtra(CountrySelectFragment.EXTRA_COUNTRY_INFO);
			mTvCountryName.setText(info.name);
			mTvCountryNumber.setText(info.number);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 检查账号是否可用于注册*/
	public boolean checkUserRegister() {

		mMobile = mEtMobile.getText().toString().trim();

		if (TextUtils.isEmpty(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_empty_mobile);
			return false;
		}

		//2016-4-14为了支持海外手机号码，这里需要增加国家区号
		String countryNumber = mTvCountryNumber.getText().toString().trim();

		if (!countryNumber.equals("+86")) {
			mMobile = countryNumber.replace("+", "00") + mMobile;
		}

		//支持海外手机号码，这里手机号码判断有特殊性
		if (!AndroidHelper.isNationalMobile(mMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_invalid_mobile);
			return false;
		}

		//必须同意协议
		if (!mCheckBox.isChecked()) {
			showShortToast(R.string.ua_uncheck_user_agreement);
			return false;
		}

		//先检查账号是否可用于注册
		mCheckRegisterContext = Long.toString(System.currentTimeMillis());
		AuthRequest.CheckRegisterReq auth = new AuthRequest.CheckRegisterReq(mMobile, mCheckRegisterContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_checking_user, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mCheckRegisterContext = null;
				}
			});
		}

		return true;
	}

	/** 请求下发短信验证码*/
	private void sendSmsCode() {

		//调用下发短信验证码接口
		mSendSMSContext = Long.toString(System.currentTimeMillis());//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.SendSmsReq auth = new AuthRequest.SendSmsReq(mMobile,
				AuthRequest.SendSmsReq.SMS_TYPE_REGISTER_OR_LOGIN, STRATEGY.NONE, null, mSendSMSContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，记录下发短信的时间，并设置倒计时按钮
			LAST_TIME_GET_SMS_CODE = System.currentTimeMillis();
			mBtnGotoStep2.setEnabled(true);
			mBtnGetSmsCode.setEnabled(false);
			mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
					getString(R.string.ua_reg_btn_get_sms_code), getString(R.string.ua_reg_btn_get_sms_code_disable));

			showProgressDialog(R.string.ua_requesting_sms_code, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSendSMSContext = null;
				}
			});
		}
	}

	/** 去第二步，输入短信验证码*/
	public void onGotoStep2Click(View v) {
		checkUserRegister();
	}

	/** 去第三步，设置新密码*/
	public void onGotoStep3Click(View v) {
		mSmsCode = mEtSmsCode.getText().toString().trim();

		if (TextUtils.isEmpty(mSmsCode)) {
			mEtSmsCode.requestFocus();
			showShortToast(R.string.ua_empty_sms_code);
			return;
		}

		//验证短信接口
		mVerifySmsCodeContext = Long.toString(System.currentTimeMillis());
		AuthRequest.VerifySmsCodeReq auth = new AuthRequest.VerifySmsCodeReq(mMobile, mSmsCode, mVerifySmsCodeContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_requesting, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mVerifySmsCodeContext = null;
				}
			});
		}
	}

	/** 提交请求*/
	public void onSubmitClick(View v) {

		mPassword = mEtPassword.getText().toString();

		if (TextUtils.isEmpty(mPassword)) {
			mEtPassword.requestFocus();
			showLongToast(R.string.ua_empty_password);
			return;
		}

		if (TextUtils.isDigitsOnly(mPassword) && mPassword.length() < 9) {
			mEtPassword.requestFocus();
			showLongToast(R.string.ua_invalid_password_with_9_number);
			return;
		}

		if (mPassword.contains(" ")) {
			mEtPassword.requestFocus();
			showLongToast(R.string.ua_invalid_password_within_blank);
			return;
		}

		if (mPassword.length() < 8 || mPassword.length() > 20) {
			mEtPassword.requestFocus();
			showLongToast(R.string.ua_invalid_password_out_8_20);
			return;
		}

		String passwdSha1 = AuthSDK.getPasswdSha1(mPassword);
		mRegisterContext = Long.toString(System.currentTimeMillis()); //请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应

		AuthRequest.AuthBaseReq auth = null;

		if (mIsAutoLogin) {
			//注册并登录
			auth = new AuthRequest.SmsRegloginReq(mMobile, mSmsCode, passwdSha1, mRegisterContext);
		} else {
			//纯注册，之后必须使用账号密码进行登录
			auth = new AuthRequest.RegisterReq(mMobile, mSmsCode, passwdSha1, mRegisterContext);
		}

		if (sendAuthRequest(auth)) {
			showProgressDialog(R.string.ua_requesting, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mRegisterContext = null;
				}
			});
		}
	}

	OnCheckedChangeListener onPasswordVisableChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				//显示密码
				mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				mEtPassword.setSelection(mEtPassword.getText().length());
			} else {
				//隐藏密码
				mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				mEtPassword.setSelection(mEtPassword.getText().length());
			}
		}
	};

	/** 检查账号是否可用于注册*/
	@Override
	protected void onCheckRegisterEvent(AuthEvent.CheckRegisterEvent et) {

		if (mCheckRegisterContext == null || !mCheckRegisterContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == UIAction.SUCCESS) {
			sendSmsCode();
		} else {
			showToastOrHtmlDialog(et.description);
		}

	};

	/** 接收到下发短信回调事件*/
	@Override
	protected void onSmsCodeEvent(AuthEvent.SendSmsEvent et) {

		if (mSendSMSContext == null || !mSendSMSContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == UIAction.SUCCESS) {
			showLongToast(R.string.ua_send_sms_success);
			mEtSmsCode.requestFocus();

			//如果当前是在step1，那么进入到step2
			if (mViewFlipper.getChildAt(0) == mViewFlipper.getCurrentView()) {
				mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
				mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
				mViewFlipper.showNext();
			}
		} else {
			LAST_TIME_GET_SMS_CODE = 0L;
			mBtnGetSmsCode.cancelCountdown();
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 处理验证短信成功的事件*/
	@Override
	protected void onVerifySmsCodeEvent(VerifySmsCodeEvent et) {

		if (mVerifySmsCodeContext == null || !mVerifySmsCodeContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == UIAction.SUCCESS) {
			mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
			mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
			mViewFlipper.showNext();
		} else if (et.uiAction == UIAction.VERIFY_FAILED) {
			showLongToast(et.description);
		} else {
			showToastOrHtmlDialog(et.description);
		}
	}

	@Override
	protected void onRegisterEvent(RegisterEvent et) {

		if (mRegisterContext == null || !mRegisterContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == UIAction.SUCCESS) {
			showLongToast(R.string.ua_register_success);

			//重置倒计时
			CountdownHelper.resetSmsCode();

			//回调，不管是什么情况下，成功就回调
			et.user = mMobile;
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.SMS_REGISTER);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else {
			showToastOrHtmlDialog(et.description);
		}

	}

	@Override
	protected void onLoginEvent(LoginEvent et) {
		if (mRegisterContext == null || !mRegisterContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == UIAction.SUCCESS) {
			showLongToast(R.string.ua_register_success);

			//重置倒计时
			CountdownHelper.resetSmsCode();

			//回调，不管是什么情况下，成功就回调
			et.user = mMobile;
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.SMS_REGISTER);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else {
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mCheckRegisterContext != null && mCheckRegisterContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_check_reg);
		} else if (mSendSMSContext != null && mSendSMSContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_send_sms_code);
		} else if (mVerifySmsCodeContext != null && mVerifySmsCodeContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_register);
		} else if (mRegisterContext != null && mRegisterContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_set_password);
		}
	}

}
