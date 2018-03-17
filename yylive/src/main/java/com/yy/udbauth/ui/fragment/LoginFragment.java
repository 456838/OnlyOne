package com.yy.udbauth.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.LoginEvent;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.activity.UdbAuthActivity;
import com.yy.udbauth.ui.adapter.AccountAdapter;
import com.yy.udbauth.ui.adapter.AccountAdapter.OnDeleteAccountListener;
import com.yy.udbauth.ui.info.PageSetting;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.LastAccountManager;
import com.yy.udbauth.ui.tools.OpreateType;
import com.yy.udbauth.ui.widget.UdbEditText;

import java.util.List;

/**
 * 概述：账号密码登录页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月19日 上午9:47:25
 */
public class LoginFragment extends UdbAuthBaseFragment implements OnClickListener {

	private static final int REQUEST_CODE_NEXT_VERIFY = 1110;

	private static final String KEY_USER = "username";

	//布局元素
	private View mMainView;
	private LinearLayout mUsernameLayout;
	private UdbEditText mEtUsername;
	private UdbEditText mEtPassword;
	private ListView mListView;
	private CheckBox mCbShowPassword;
	private Button mBtnRegister;
	private Button mBtnLogin;
	private TextView mTvSmsLogin;
	private TextView mTvFindMyPassport;
	private ImageButton mBtnCleanUsername;
	private ImageButton mBtnShowUserList;

	private PopupWindow mPopupWindow;
	private AccountAdapter mAdapter;
	private List<String> mAccounts;

	private String mUsername;
	private String mPassword;

	//业务流水号，用于标识请求唯一性
	String mRequestContext = null;
	//是否已经通知业务操作结果了
	boolean hasCallback = false;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && shouldHandle() && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.PWD_LOGIN) {
			AuthCallbackProxy.onCancel(OpreateType.PWD_LOGIN);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_login;
		mMainView = inflater.inflate(layout, container, false);

		// 关联布局元素
		mUsernameLayout = (LinearLayout) mMainView.findViewById(R.id.ua_login_ll_username);
		mEtUsername = (UdbEditText) mMainView.findViewById(R.id.ua_login_et_username);
		mEtPassword = (UdbEditText) mMainView.findViewById(R.id.ua_login_et_password);
		mCbShowPassword = (CheckBox) mMainView.findViewById(R.id.ua_login_cb_show_password);
		mBtnShowUserList = (ImageButton) mMainView.findViewById(R.id.ua_login_btn_show_accounts_list);
		mBtnCleanUsername = (ImageButton) mMainView.findViewById(R.id.ua_login_btn_clear_username);
		mTvSmsLogin = (TextView) mMainView.findViewById(R.id.ua_login_btn_sms_login);
		mTvFindMyPassport = (TextView) mMainView.findViewById(R.id.ua_login_btn_find_my_password);
		mBtnRegister = (Button) mMainView.findViewById(R.id.ua_login_btn_register);
		mBtnLogin = (Button) mMainView.findViewById(R.id.ua_login_btn_login);
		mListView = (ListView) inflater.inflate(R.layout.ua_popun_window_account, container, false);

		//绑定监听 
		mBtnShowUserList.setOnClickListener(this);
		mBtnRegister.setOnClickListener(this);
		mTvSmsLogin.setOnClickListener(this);
		mTvFindMyPassport.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mCbShowPassword.setOnCheckedChangeListener(onPasswordVisableChangeListener);
		mCbShowPassword.setChecked(false);

		mPopupWindow = new PopupWindow(mListView);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		mEtUsername.bindCleanButton(R.id.ua_login_btn_clear_username);
		mEtPassword.bindCleanButton(R.id.ua_login_btn_clear_password);

		mBtnCleanUsername.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mEtUsername.setText("");
				mEtPassword.setText("");
			}
		});

		//设置标题栏
		setTitleBarText(R.string.ua_title_login);

		//显示默认用户
		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_USER)) {
			mUsername = savedInstanceState.getString(mUsername);
		}
		showDefalutAccount();

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {

		// 适配样式效果
		adjustButtonStyle(mBtnLogin);
		adjustDefaultTextStyle(mTvFindMyPassport);
		adjustDefaultTextStyle(mTvSmsLogin);
		adjustStrikingTextStyle(mBtnRegister);

		final PageSetting es = AuthUI.getInstance().getPageSetting();
		setupViewVisable(mMainView, R.id.ua_login_btn_find_my_password, es.loginPage_showFindMyPassword);
		setupViewVisable(mMainView, R.id.ua_login_btn_register, es.loginPage_showRegister);
		setupViewVisable(mMainView, R.id.ua_login_btn_sms_login, es.loginPage_showSmsLogin);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_USER, mUsername);
		super.onSaveInstanceState(outState);
	}

	/** 显示默认用户*/
	private void showDefalutAccount() {

		mAccounts = LastAccountManager.getLastAccounts(getContext());

		if (mAccounts.size() <= 0) {
			mBtnShowUserList.setVisibility(View.GONE);
			mEtUsername.setText("");
			mEtPassword.setText("");
			return;
		}

		mBtnShowUserList.setVisibility(View.VISIBLE);
		mEtUsername.setText(mAccounts.get(0));
		mEtPassword.setText("");
		mBtnShowUserList.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.ua_login_btn_show_accounts_list) {

			// 页面【显示已登录用户信息】按钮点击事件
			onShowUserListClick(v);

		} else if (id == R.id.ua_login_btn_register) {

			// 页面【立即注册】按钮点击事件/
			startFragmentForResult(RegisterFragment.class);

		} else if (id == R.id.ua_login_btn_sms_login) {

			// 页面【手机短信登录账号】按钮点击事件/
			// 判断是不是从手机短信登录过来的，如果是，结束当前页面即可，这样可以避免循环打开
			UdbAuthActivity callingActivity = FragmentHelper.getContainActivity(SmsLoginFragment.class);
			if (callingActivity == null) {
				startFragmentForResult(SmsLoginFragment.class);
			} else {
				finish();
			}

		} else if (id == R.id.ua_login_btn_find_my_password) {
			// 页面【找回密码】按钮点击事件/
			startFragmentForResult(FindMyPasswordFragment.class);

		} else if (id == R.id.ua_login_btn_login) {

			// 页面【登录】按钮点击事件
			onLoginClick(v);
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

	/** 页面【显示已登录用户信息】按钮点击事件*/
	public void onShowUserListClick(View v) {

		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return;
		}

		mAccounts = LastAccountManager.getLastAccounts(getContext());

		mAdapter = new AccountAdapter(getContext(), mAccounts, onDeleteAccountListener);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(onAccountClickListener);
		mPopupWindow.setWidth(mUsernameLayout.getWidth());
		mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);

		if (mAdapter.getCount() <= 0) {
			mPopupWindow.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
					.getDisplayMetrics()));
		}

		mPopupWindow.showAsDropDown(mUsernameLayout);
		mBtnShowUserList.setImageResource(R.drawable.ua_ic_shrink);
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mBtnShowUserList.setImageResource(R.drawable.ua_ic_stretch);
			}
		});
	}

	/** 页面【登录】按钮点击事件*/
	public void onLoginClick(View v) {

		mUsername = mEtUsername.getText().toString().trim();
		mPassword = mEtPassword.getText().toString();

		if (TextUtils.isEmpty(mUsername)) {
			mEtUsername.requestFocus();
			showShortToast(R.string.ua_empty_username);
			return;
		}

		//支持海外手机号码，这里仅针对国内手机号码做判断
		if (mUsername.startsWith("00") && !AndroidHelper.isNationalMobile(mUsername)) {
			mEtUsername.requestFocus();
			showShortToast(R.string.ua_invalid_mobile);
			return;
		}

		if (TextUtils.isEmpty(mPassword)) {
			mEtPassword.requestFocus();
			showShortToast(R.string.ua_empty_password);
			return;
		}

		String passwdSha1 = AuthSDK.getPasswdSha1(mPassword);

		//调用账号密码登录接口
		mRequestContext = Long.toString(System.currentTimeMillis());
		AuthRequest.LoginReq auth = new AuthRequest.LoginReq(mUsername, passwdSha1, STRATEGY.NONE, null,
				mRequestContext);

		if (sendAuthRequest(auth)) {
			//发送请求成功，显示进度对话框
			showProgressDialog(R.string.ua_logining, new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					mRequestContext = null;
				}
			});
		}
	}

	OnDeleteAccountListener onDeleteAccountListener = new OnDeleteAccountListener() {

		@Override
		public void onDelete(String account) {
			LastAccountManager.deleteLastAccount(getContext(), account);
			mPopupWindow.dismiss();
			showDefalutAccount();
		}
	};

	OnItemClickListener onAccountClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			mEtUsername.setText(mAccounts.get(position));
			mEtPassword.setText("");
			mPopupWindow.dismiss();
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		//用户进行二次验证成功
		if (requestCode == REQUEST_CODE_NEXT_VERIFY && resultCode == VerifyFragment.RESULT_ACCOUNT_INFO) {

			LoginEvent event = (LoginEvent) data.getSerializableExtra(VerifyFragment.EXTRA_LOGIN_EVENT);

			if (event != null) {
				//回调，不管是什么情况下，登录成功就回调
				LastAccountManager.updateLastAccounts(getContext(), mUsername);
				event.user = mUsername;
				AuthCallbackProxy.onAuthSuccess(event, OpreateType.PWD_LOGIN);
				hasCallback = true;
				FragmentHelper.finishAllActivity();
			}
		} else if (requestCode == REQUEST_CODE_NEXT_VERIFY) {
			/* 
			 * 二次验证被取消，这里需要调用logout接口，
			 * 不然SDK会出现无登录状态的情况（连匿名登录状态都没有），这将导致无法进入频道
			 */
			AuthUI.getInstance().logout();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 表示接收到后端返回的登录结果*/
	@Override
	protected void onLoginEvent(LoginEvent et) {

		if (mRequestContext == null || !mRequestContext.equals(et.context)) {
			return;
		}

		//隐藏进度对话框
		showProgressDialog(null, null);

		if (et.uiAction == AuthEvent.UIAction.SUCCESS) {

			//重置倒计时
			CountdownHelper.resetSmsCode();

			//登录成功 
			showShortToast(R.string.ua_login_success);

			//回调，不管是什么情况下，登录成功就回调
			LastAccountManager.updateLastAccounts(getContext(), mUsername);
			et.user = mUsername;
			AuthCallbackProxy.onAuthSuccess(et, OpreateType.PWD_LOGIN);
			hasCallback = true;
			FragmentHelper.finishAllActivity();

		} else if (et.uiAction == AuthEvent.UIAction.NEXT_VERIFY) {

			//需要二次验证
			showNextVerifyActivity(et);

		} else if (et.uiAction == AuthEvent.UIAction.CREDIT_INVALID) {

			//凭证已经失效，需要重新进行登录
			showShortToast(R.string.ua_credit_is_unavailable);

			mEtPassword.setText("");
			mEtPassword.requestFocus();
		} else {

			//登录失败
			showToastOrHtmlDialog(et.description);
		}
	}

	/** 显示二次验证*/
	private void showNextVerifyActivity(LoginEvent et) {

		if (et.nextVerifies == null || et.nextVerifies.size() <= 0) {
			//登录失败
			showLongToast(R.string.ua_login_failed_with_empty_verify);
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putSerializable(VerifyFragment.EXTRA_STRATEGIES, et.nextVerifies);
		bundle.putString(VerifyFragment.EXTRA_USERNAME, mUsername);
		bundle.putString(VerifyFragment.EXTRA_PASSWD_SHA1, AuthSDK.getPasswdSha1(mPassword));
		startFragmentForResult(VerifyFragment.class, REQUEST_CODE_NEXT_VERIFY, bundle);
	}

	/** 表示发送的请求超时了*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mRequestContext != null && mRequestContext.equals(et.context)) {
			showProgressDialog(null, null);
			showShortToast(R.string.ua_timeout_login);
		}
	}

}
