package com.yy.udbauth.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
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
import com.yy.udbauth.AuthEvent.CheckModPwdEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.OnUdbAuthListener;
import com.yy.udbauth.ui.tools.OpreateType;
import com.yy.udbauth.ui.widget.UdbButton;
import com.yy.udbauth.ui.widget.UdbDialog;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 概述：修改密码
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月19日 下午5:24:49
 */
public class ModifyPasswordFragment extends UdbAuthBaseFragment {

	/** 表示需要执行修改密码操作的帐号，支持“通行证、YY号、手机号、邮箱”四种类型*/
	public static final String EXTRA_USER = "extra_user";

	private static final String KEY_CHECK = "hasCallCheckModPwd";
	private static final String KEY_USER = "hasUser";

	//记录上次获取短信的时间
	public static long LAST_TIME_GET_SMS_CODE = 0L;
	//两次获取短信的时间间隔，单位为毫秒
	private static int TIME_INTERVAL_GET_SMS_CODE = 60000;

	View mMainView;
	ViewFlipper mViewFlipper;
	TextView mTvTips;
	UdbEditText mEtSmsCode;
	UdbEditText mEtPassword;
	CheckBox mCbShowPassword;
	UdbButton mBtnGetSmsCode;
	Button mBtnNext;
	Button mBtnSubmit;

	String mPassword;

	String mUser;//需要修改密码的帐号
	String mMobileMask;//手机掩码
	String mSmsCode;

	//请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
	String mCheckModPwdContext = null;
	String mSmsRequestContext = null;
	String mSmsVerifyRequestContext = null;
	String mPasswordRequestContext = null;

	//判断是否已经检查过用户了
	boolean hasCallCheckModPwd = false;

	//是否已经通知业务操作结果了
	boolean hasCallback = false;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && shouldHandle() && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.MODIFY_PWD) {
			AuthCallbackProxy.onCancel(OpreateType.MODIFY_PWD);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_modify_password;
		mMainView = inflater.inflate(layout, container, false);

		// 关联布局元素
		mViewFlipper = (ViewFlipper) mMainView.findViewById(R.id.ua_modify_password_viewflipper);
		mTvTips = (TextView) mMainView.findViewById(R.id.ua_modify_password_tv_tips);
		mEtSmsCode = (UdbEditText) mMainView.findViewById(R.id.ua_modify_password_et_smscode);
		mEtPassword = (UdbEditText) mMainView.findViewById(R.id.ua_modify_password_et_password);
		mCbShowPassword = (CheckBox) mMainView.findViewById(R.id.ua_modify_password_cb_show_password);
		mBtnGetSmsCode = (UdbButton) mMainView.findViewById(R.id.ua_modify_password_btn_get_sms_code);
		mBtnNext = (Button) mMainView.findViewById(R.id.ua_modify_password_btn_next);
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_modify_password_btn_submit);

		//设置标题
		setTitleBarText(R.string.ua_title_modify_password);

		//设置倒计时按钮
		mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
				getString(R.string.ua_modify_password_btn_get_sms_code),
				getString(R.string.ua_modify_password_btn_get_sms_code_disable));

		mEtSmsCode.bindCleanButton(R.id.ua_modify_password_btn_clear_sms_code);
		mEtPassword.bindCleanButton(R.id.ua_modify_password_btn_clear_password);

		mCbShowPassword.setOnCheckedChangeListener(onPasswordVisableChangeListener);
		mCbShowPassword.setChecked(false);

		mBtnGetSmsCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGetSmsCodeClick(v);
			}
		});

		mBtnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onNextClick(v);
			}
		});

		mBtnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onSubmitClick(v);
			}
		});

		//适配样式表
		adjustPageStyle();

		mUser = getArguments().getString(EXTRA_USER);
		if (savedInstanceState != null) {
			hasCallCheckModPwd = savedInstanceState.getBoolean(KEY_CHECK);
			mUser = savedInstanceState.getString(KEY_USER);
		}

		if (TextUtils.isEmpty(mUser)) {
			//通知业务，出错了
			AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_USER_INVALID, OpreateType.MODIFY_PWD);
			hasCallback = true;
			finish();
			return mMainView;
		}

		if (!hasCallCheckModPwd) {
			mMainView.setVisibility(View.INVISIBLE);
			doCheckModPwd(mUser);
		} else {
			mMainView.setVisibility(View.VISIBLE);
		}

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustButtonStyle(mBtnGetSmsCode);
		adjustButtonStyle(mBtnNext);
		adjustButtonStyle(mBtnSubmit);
	}

	@Override
	public boolean onBackPressed() {
		if (getActivity() != null) {

			String msg = String.format(getString(R.string.ua_abort_opreation),
					getString(R.string.ua_title_modify_password));

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
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(KEY_CHECK, hasCallCheckModPwd);
		outState.putString(KEY_USER, mUser);
		super.onSaveInstanceState(outState);
	}

	/** 检测帐号是否具备短信改密功能*/
	private void doCheckModPwd(String user) {
		mCheckModPwdContext = Long.toString(System.currentTimeMillis());

		AuthRequest.CheckModPwdReq auth = new AuthRequest.CheckModPwdReq(user, mCheckModPwdContext);

		if (sendAuthRequest(auth)) {
			showProgressDialog(R.string.ua_checking_user, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mCheckModPwdContext = null;
					AuthCallbackProxy.onCancel(OpreateType.MODIFY_PWD);
					finish();
				}
			});
		} else {
			hasCallback = true;
			finish();
		}
	}

	/** 下发短信验证码*/
	public void onGetSmsCodeClick(View v) {

		if (TextUtils.isEmpty(mUser)) {
			showShortToast(R.string.ua_empty_username2);
			return;
		}

		//使用下发短信验证码接口
		mSmsRequestContext = Long.toString(System.currentTimeMillis()); //请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.SendSmsReq auth = new AuthRequest.SendSmsReq(mUser, null, STRATEGY.NONE, null, mSmsRequestContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，记录下发短信的时间，并设置倒计时按钮
			LAST_TIME_GET_SMS_CODE = System.currentTimeMillis();
			mBtnGetSmsCode.setEnabled(false);
			mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
					getString(R.string.ua_modify_password_btn_get_sms_code),
					getString(R.string.ua_modify_password_btn_get_sms_code_disable));

			showProgressDialog(R.string.ua_requesting_sms_code, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSmsRequestContext = null;
				}
			});
		}
	}

	/** 下一步，检查短信验证码是否正确*/
	public void onNextClick(View v) {

		mSmsCode = mEtSmsCode.getText().toString().trim();

		if (TextUtils.isEmpty(mSmsCode)) {
			mEtSmsCode.requestFocus();
			showLongToast(R.string.ua_empty_sms_code);
			return;
		}

		//使用下发短信验证码接口
		mSmsVerifyRequestContext = Long.toString(System.currentTimeMillis()); //请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.VerifySmsCodeReq auth = new AuthRequest.VerifySmsCodeReq(mUser, mSmsCode, mSmsVerifyRequestContext);

		if (sendAuthRequest(auth)) {
			showProgressDialog(R.string.ua_requesting_sms_verify, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSmsVerifyRequestContext = null;
				}
			});
		}
	}

	/** 设置密码，提交请求*/
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

		//使用短信改密接口
		mPasswordRequestContext = Long.toString(System.currentTimeMillis()); //请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.QuickModPwdReq auth = new AuthRequest.QuickModPwdReq(mUser, passwdSha1, mPasswordRequestContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_requesting, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mPasswordRequestContext = null;
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

	@Override
	protected void onCheckModPwdEvent(CheckModPwdEvent et) {

		if (mCheckModPwdContext == null || !mCheckModPwdContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			//帐号状态是合法的，那么下发短信
			mMobileMask = et.mobileMask;
			onGetSmsCodeClick(mBtnGetSmsCode);
			mMainView.setVisibility(View.VISIBLE);
			String tips = String.format(getString(R.string.ua_modify_password_sms_code_tv_phone_ok_hint), mMobileMask);
			mTvTips.setText(tips);

		} else if (et.uiAction == AuthEvent.UIAction.OPEN_URL) {
			//这种情况需要打开一个Web页面
			Bundle bundle = new Bundle();
			bundle.putString(WebViewFragment.EXTRA_TITLE, getString(R.string.ua_title_modify_password));
			bundle.putString(WebViewFragment.EXTRA_URL, et.url);
			startFragmentForResult(WebViewFragment.class, bundle);

			AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NOT_SUPPORT, OpreateType.MODIFY_PWD);
			hasCallback = true;
			finish();
		} else {
			//失败，显示文案
			showToastOrHtmlDialog(et.description);
			hasCallback = true;
			finish();
		}

	}

	/** 接收到下发短信回调事件*/
	@Override
	protected void onSmsCodeEvent(AuthEvent.SendSmsEvent et) {

		if (mSmsRequestContext == null || !mSmsRequestContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showLongToast(R.string.ua_send_sms_success);

			mEtSmsCode.requestFocus();
			mBtnNext.setEnabled(true);
			String tips = String.format(getString(R.string.ua_modify_password_sms_code_tv_sms_ok_hint), mMobileMask);
			mTvTips.setText(tips);

		} else {
			LAST_TIME_GET_SMS_CODE = 0L;
			mBtnGetSmsCode.cancelCountdown();
			//showLongToast(R.string.ua_send_sms_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}
	}

	@Override
	protected void onVerifySmsCodeEvent(AuthEvent.VerifySmsCodeEvent et) {

		if (mSmsVerifyRequestContext == null || !mSmsVerifyRequestContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
			mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
			mViewFlipper.showNext();
		} else if (et.uiAction == AuthEvent.UIAction.VERIFY_FAILED) {
			showLongToast(et.description);
		} else {
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 接收到修改密码回调事件*/
	@Override
	protected void onSmsModPwdEvent(AuthEvent.SmsModPwdEvent et) {

		if (mPasswordRequestContext == null || !mPasswordRequestContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showLongToast(R.string.ua_set_passwrod_success);

			//重置倒计时
			CountdownHelper.resetSmsCode();

			//回调，修改密码也当作为成功回调
			et.user = mUser;
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.MODIFY_PWD);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else {
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mCheckModPwdContext != null && mCheckModPwdContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_send_sms_code);
			hasCallback = true;
			finish();
		} else if (mSmsRequestContext != null && mSmsRequestContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_send_sms_code);
		} else if (mPasswordRequestContext != null && mPasswordRequestContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_set_password);
		} else if (mSmsVerifyRequestContext != null && mSmsVerifyRequestContext.equals(et.context)) {
			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_verify_sms);
		}
	}
}
