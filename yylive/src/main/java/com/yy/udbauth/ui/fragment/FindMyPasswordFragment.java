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
import com.yy.udbauth.AuthEvent.SmsModPwdEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.OpreateType;
import com.yy.udbauth.ui.widget.UdbButton;
import com.yy.udbauth.ui.widget.UdbDialog;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 概述：找回密码
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月21日 上午10:34:27
 */
public class FindMyPasswordFragment extends UdbAuthBaseFragment {

	//记录上次获取短信的时间
	public static long LAST_TIME_GET_SMS_CODE = 0L;
	//两次获取短信的时间间隔，单位为毫秒
	private static int TIME_INTERVAL_GET_SMS_CODE = 60000;

	View mMainView;
	ViewFlipper mViewFlipper;
	UdbEditText mEtUser;
	UdbEditText mEtMobile;
	UdbEditText mEtSmsCode;
	UdbEditText mEtPassword;
	CheckBox mCbShowPassword;
	UdbButton mBtnGetSmsCode;
	Button mBtnGotoStep2;
	Button mBtnGotoStep3;
	Button mBtnGotoStep4;
	Button mBtnSubmit;

	String mUser; //用户输入的用户名
	String mSecMobile; //用户输入的密保手机号码
	String mSmsCode;//手机短信验证码
	String mPassword;//新密码

	String mCheckModPwdContext;//检验短信找密业务流水号
	String mSendSMSContext;//下发短信业务流水号
	String mVerifySmsCodeContext;//短信注册业务流水号
	String mSetPasswordContext = null;//提供注册密码业务流水号

	//是否已经通知业务操作结果了
	boolean hasCallback = false;

	AuthEvent.LoginEvent mLoginEvent;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && shouldHandle() && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.FIND_MY_PWD) {
			AuthCallbackProxy.onCancel(OpreateType.FIND_MY_PWD);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_find_my_password;
		mMainView = inflater.inflate(layout, container, false);

		// 关联布局元素
		mViewFlipper = (ViewFlipper) mMainView.findViewById(R.id.ua_find_my_password_viewflipper);
		mEtUser = (UdbEditText) mMainView.findViewById(R.id.ua_find_my_password_et_user);
		mEtMobile = (UdbEditText) mMainView.findViewById(R.id.ua_find_my_password_et_mobile);
		mEtSmsCode = (UdbEditText) mMainView.findViewById(R.id.ua_find_my_password_et_smscode);
		mEtPassword = (UdbEditText) mMainView.findViewById(R.id.ua_find_my_password_et_password);
		mCbShowPassword = (CheckBox) mMainView.findViewById(R.id.ua_find_my_password_cb_show_password);
		mBtnGetSmsCode = (UdbButton) mMainView.findViewById(R.id.ua_find_my_password_btn_get_sms_code);
		mBtnGotoStep2 = (Button) mMainView.findViewById(R.id.ua_find_my_password_btn_goto_step2);
		mBtnGotoStep3 = (Button) mMainView.findViewById(R.id.ua_find_my_password_btn_goto_step3);
		mBtnGotoStep4 = (Button) mMainView.findViewById(R.id.ua_find_my_password_btn_goto_step4);
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_find_my_password_btn_submit);

		//设置标题
		setTitleBarText(R.string.ua_title_find_my_password);

		//设置倒计时按钮
		mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
				getString(R.string.ua_find_my_password_btn_get_sms_code),
				getString(R.string.ua_find_my_password_btn_get_sms_code_disable));

		mEtUser.bindCleanButton(R.id.ua_find_my_password_btn_clear_user);
		mEtMobile.bindCleanButton(R.id.ua_find_my_password_btn_clear_mobile);
		mEtSmsCode.bindCleanButton(R.id.ua_find_my_password_btn_clear_sms_code);
		mEtPassword.bindCleanButton(R.id.ua_find_my_password_btn_clear_password);

		mCbShowPassword.setOnCheckedChangeListener(onPasswordVisableChangeListener);
		mCbShowPassword.setChecked(false);

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

		mBtnGotoStep4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGotoStep4Click(v);
			}
		});

		mBtnGetSmsCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onGetSmsCodeClick(v);
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

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {

		// 适配样式效果
		adjustButtonStyle(mBtnGetSmsCode);
		adjustButtonStyle(mBtnGotoStep2);
		adjustButtonStyle(mBtnGotoStep3);
		adjustButtonStyle(mBtnGotoStep4);
		adjustButtonStyle(mBtnSubmit);
	}

	@Override
	public boolean onBackPressed() {
		if (getActivity() != null && mViewFlipper.getDisplayedChild() > 0) {

			String msg = String.format(getString(R.string.ua_abort_opreation),
					getString(R.string.ua_title_find_my_password));

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

	/** 去第二步，输入密保手机*/
	public void onGotoStep2Click(View v) {
		mUser = mEtUser.getText().toString().trim();

		if (TextUtils.isEmpty(mUser)) {
			mEtUser.requestFocus();
			showShortToast(R.string.ua_empty_username);
			return;
		}

		//调用检查账号是否具备短信找密条件
		mCheckModPwdContext = Long.toString(System.currentTimeMillis());
		AuthRequest.CheckModPwdReq auth = new AuthRequest.CheckModPwdReq(mUser, mCheckModPwdContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_checking_user, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mCheckModPwdContext = null;
				}
			});
		}
	}

	/** 去第三步，获取短信验证码*/
	public void onGotoStep3Click(View v) {
		mSecMobile = mEtMobile.getText().toString().trim();

		if (TextUtils.isEmpty(mSecMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_empty_mobile);
			return;
		}

		//支持海外手机号码，这里手机号码判断有特殊性
		if (!AndroidHelper.isNationalMobile(mSecMobile)) {
			mEtMobile.requestFocus();
			showShortToast(R.string.ua_invalid_mobile);
			return;
		}

		//请求下发短信
		onGetSmsCodeClick(mBtnGetSmsCode);
	}

	/** 去第四步，设置新密码*/
	public void onGotoStep4Click(View v) {

		mSmsCode = mEtSmsCode.getText().toString().trim();

		if (TextUtils.isEmpty(mSmsCode)) {
			mEtSmsCode.requestFocus();
			showShortToast(R.string.ua_empty_sms_code);
			return;
		}

		//验证手机和短信
		mVerifySmsCodeContext = Long.toString(System.currentTimeMillis());
		AuthRequest.VerifySmsCodeReq auth = new AuthRequest.VerifySmsCodeReq(mUser, mSmsCode, mVerifySmsCodeContext);

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

	/** 下发短信验证码*/
	public boolean onGetSmsCodeClick(View v) {

		//调用下发短信验证码接口
		mSendSMSContext = Long.toString(System.currentTimeMillis());
		AuthRequest.SendSmsReq auth = new AuthRequest.SendSmsReq(mUser, mSecMobile, STRATEGY.NONE, null,
				mSendSMSContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，记录下发短信的时间，并设置倒计时按钮
			LAST_TIME_GET_SMS_CODE = System.currentTimeMillis();
			mBtnGotoStep2.setEnabled(true);
			mBtnGetSmsCode.setEnabled(false);
			mBtnGetSmsCode.setupCountdown(LAST_TIME_GET_SMS_CODE, LAST_TIME_GET_SMS_CODE + TIME_INTERVAL_GET_SMS_CODE,
					getString(R.string.ua_find_my_password_btn_get_sms_code),
					getString(R.string.ua_find_my_password_btn_get_sms_code_disable));

			showProgressDialog(R.string.ua_requesting_sms_code, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSendSMSContext = null;
				}
			});
		}

		return true;
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

		//使用短信改密接口
		mSetPasswordContext = Long.toString(System.currentTimeMillis()); //请求上下文，用于与响应的上下文比较，看看是不是我们发出的请求的响应
		AuthRequest.QuickModPwdReq auth = new AuthRequest.QuickModPwdReq(mUser, passwdSha1, mSetPasswordContext);

		if (sendAuthRequest(auth)) {
			showProgressDialog(R.string.ua_requesting, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mSetPasswordContext = null;
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

		if (et.uiAction == AuthEvent.UIAction.SUCCESS && et.isLoginMobile == false) {
			//跳转去第二步，输入密保手机页面
			TextView tv = (TextView) mMainView.findViewById(R.id.ua_find_my_password_tv_secmobile);
			tv.setText(String.format(getString(R.string.ua_find_my_password_mobile_hint), et.mobileMask));

			mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
			mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
			mViewFlipper.showNext();

		} else if (et.uiAction == AuthEvent.UIAction.SUCCESS && et.isLoginMobile) {
			//跳转去第三步，手机短信验证页面
			mSecMobile = mUser; //此时，密保手机 = 登录手机 = user

			//请求下发短信
			onGetSmsCodeClick(mBtnGetSmsCode);

		} else if (et.uiAction == AuthEvent.UIAction.OPEN_URL) {
			//这种情况需要打开一个Web页面
			Bundle bundle = new Bundle();
			bundle.putString(WebViewFragment.EXTRA_TITLE, getString(R.string.ua_title_find_my_password));
			bundle.putString(WebViewFragment.EXTRA_URL, et.url);
			startFragmentForResult(WebViewFragment.class, bundle);

		} else {
			//失败，显示文案
			//showLongToast(R.string.ua_find_my_password_check_failed, et.description);
			showToastOrHtmlDialog(et.description);
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

			//如果当前不是在第三步，那么需要跳转到第三步（即下发短信）
			if (mViewFlipper.getChildAt(2) != mViewFlipper.getCurrentView()) {
				mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
				mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
				mViewFlipper.setDisplayedChild(2);
			}
		} else {
			LAST_TIME_GET_SMS_CODE = 0L;
			mBtnGetSmsCode.cancelCountdown();
			//showLongToast(R.string.ua_send_sms_failed, et.description);
			showToastOrHtmlDialog(et.description);
		}
	}

	@Override
	protected void onVerifySmsCodeEvent(AuthEvent.VerifySmsCodeEvent et) {

		if (mVerifySmsCodeContext == null || !mVerifySmsCodeContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			mViewFlipper.setInAnimation(getContext(), R.anim.ua_right_in);
			mViewFlipper.setOutAnimation(getContext(), R.anim.ua_left_out);
			mViewFlipper.setDisplayedChild(3);
		} else if (et.uiAction == AuthEvent.UIAction.VERIFY_FAILED) {
			showLongToast(et.description);
		} else {
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 处理改密结果*/
	@Override
	protected void onSmsModPwdEvent(final SmsModPwdEvent et) {

		if (mSetPasswordContext == null || !mSetPasswordContext.equals(et.context)) {
			return;
		}

		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {
			showLongToast(R.string.ua_set_passwrod_success);

			//重置倒计时
			CountdownHelper.resetSmsCode();
			//返回改密信息
			et.user = mUser; //增加用户名称
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.FIND_MY_PWD);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else {
			//改密失败
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 超时机制*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mCheckModPwdContext != null && mCheckModPwdContext.equals(et.context)) {

			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_check_modpwd);

		} else if (mSendSMSContext != null && mSendSMSContext.equals(et.context)) {

			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_verify_sms);

		} else if (mVerifySmsCodeContext != null && mVerifySmsCodeContext.equals(et.context)) {

			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_set_password);

		} else if (mSetPasswordContext != null && mSetPasswordContext.equals(et.context)) {

			showProgressDialog(null, null);
			showLongToast(R.string.ua_timeout_set_password);

		}
	}

}
