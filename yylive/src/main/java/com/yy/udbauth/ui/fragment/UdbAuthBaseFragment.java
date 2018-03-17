package com.yy.udbauth.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.AuthBaseEvent;
import com.yy.udbauth.AuthEvent.CheckModPwdEvent;
import com.yy.udbauth.AuthEvent.CheckRegisterEvent;
import com.yy.udbauth.AuthEvent.CreditRenewEvent;
import com.yy.udbauth.AuthEvent.RegisterEvent;
import com.yy.udbauth.AuthEvent.SmsModPwdEvent;
import com.yy.udbauth.AuthRequest.AuthBaseReq;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.IAuthEventWatcher;
import com.yy.udbauth.ui.style.PageStyle;
import com.yy.udbauth.ui.tools.FragmentHelper;
import com.yy.udbauth.ui.tools.IUdbAuthActivityCallback;
import com.yy.udbauth.ui.widget.HtmlAlertDialog;

/**
 * 概述：自定义Fragment基类，主要包含回调事件处理、发送请求处理等基本操作<br/>
 * <p>
 * <strong>注意，此类必须被关联到实现了{@link IUdbAuthActivityCallback}接口的Activity中，否则回报错</strong>
 * </>
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月19日 上午10:49:31
 */
public class UdbAuthBaseFragment extends Fragment {
	private static final int REQ_CODE_FOR_AUTH = 9793;
	private static final int RESULT_CODE_FOR_AUTH = 3793;
	private static final String KEY_COME_BACK = "key_come_back";

	IUdbAuthActivityCallback mOnUdbAuthActivityCallback;
	private Context mContext;
	private boolean mHasComebackFromOther = true;

	private IAuthEventWatcher mAuthEventWatcher = new IAuthEventWatcher() {

		@Override
		public void onAuthRes(AuthBaseEvent base) {

			if (base instanceof AuthEvent.LoginEvent) {

				//转换为登录回调事件
				AuthEvent.LoginEvent et = (AuthEvent.LoginEvent) base;
				onLoginEvent(et);

			} else if (base instanceof AuthEvent.SendSmsEvent) {

				//转换为下发短信回调事件
				AuthEvent.SendSmsEvent et = (AuthEvent.SendSmsEvent) base;
				onSmsCodeEvent(et);

			} else if (base instanceof AuthEvent.VerifySmsCodeEvent) {

				//转换为检验短信验证码事件
				AuthEvent.VerifySmsCodeEvent et = (AuthEvent.VerifySmsCodeEvent) base;
				onVerifySmsCodeEvent(et);

			} else if (base instanceof AuthEvent.RefreshPicEvent) {

				//转换为刷新图片验证码回调事件
				AuthEvent.RefreshPicEvent et = (AuthEvent.RefreshPicEvent) base;
				onRefreshPicEvent(et);

			} else if (base instanceof CreditRenewEvent) {

				//转换为凭证自动刷新回调事件
				CreditRenewEvent et = (CreditRenewEvent) base;
				onCreditRenewEvent(et);

			} else if (base instanceof CheckModPwdEvent) {
				//转换为检验账号是否具有短信找密条件
				CheckModPwdEvent et = (CheckModPwdEvent) base;
				onCheckModPwdEvent(et);

			} else if (base instanceof SmsModPwdEvent) {
				//转换为改密结果
				SmsModPwdEvent et = (SmsModPwdEvent) base;
				onSmsModPwdEvent(et);

			} else if (base instanceof CheckRegisterEvent) {
				//转换为注册检查结果
				CheckRegisterEvent et = (CheckRegisterEvent) base;
				onCheckRegisterEvent(et);

			} else if (base instanceof RegisterEvent) {
				//转换为注册结果
				RegisterEvent et = (RegisterEvent) base;
				onRegisterEvent(et);

			} else if (base instanceof AuthEvent.TimeoutEvent) {

				//转换为超时回调事件
				AuthEvent.TimeoutEvent et = (AuthEvent.TimeoutEvent) base;
				onTimeoutEvent(et);
			}
		}

		@Override
		public void onLoginAPFalied(int uSrvResCode) {
			/*
			 * 对于进频道类APP，登录过程分为两步：第一步是验证登录信息，第二步是登录AP，两个步骤都成功，才算是登录成功。
			 * 这里登录AP失败，需要做相关处理
			*/
			onLoginAPFailed(uSrvResCode);
			Log.e("udbauth", "udbauth:LoginResNGEvent error code " + uSrvResCode);
		}

		@Override
		public void onKickOff(int code, String reason) {
			onKickOffEvent(code, reason);
		}

	};

	/** 表示登录结果回调，子类根据需要重写该方法*/
	protected void onLoginEvent(AuthEvent.LoginEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示下发短信回调，子类根据需要重写该方法 */
	protected void onSmsCodeEvent(AuthEvent.SendSmsEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示检验短信验证码回调，子类根据需要重写该方法 */
	protected void onVerifySmsCodeEvent(AuthEvent.VerifySmsCodeEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示刷新图片验证码回调，子类根据需要重写该方法*/
	protected void onRefreshPicEvent(AuthEvent.RefreshPicEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示凭证自动刷新回调，子类根据需要重写该方法*/
	protected void onCreditRenewEvent(CreditRenewEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示检验账号是否具有短信找密条件，子类根据需要重写该方法*/
	protected void onCheckModPwdEvent(CheckModPwdEvent et) {
		// TODO Auto-generated method stub

	}

	/** 表示检验账号是否具有短信找密条件，子类根据需要重写该方法*/
	protected void onSmsModPwdEvent(SmsModPwdEvent et) {
		// TODO Auto-generated method stub

	}

	/** 表示检验账号是否已经注册过，子类根据需要重写该方法*/
	protected void onCheckRegisterEvent(CheckRegisterEvent et) {
		// TODO Auto-generated method stub

	}

	/** 表示注册结果，子类根据需要重写该方法*/
	protected void onRegisterEvent(RegisterEvent et) {
		// TODO Auto-generated method stub

	}

	/** 表示超时回调，子类根据需要重写该方法*/
	protected void onTimeoutEvent(AuthEvent.TimeoutEvent et) {
		// TODO Auto-generated method stub
	}

	/** 表示登录AP失败回调，子类根据需要重写该方法<br/>
	 * <p>
	 * 对于进频道类APP，登录过程分为两步：第一步是验证登录信息，第二步是登录AP，两个步骤都成功，才算是登录成功。这里登录AP失败，需要做相关处理
	 * </p>
	 */
	protected void onLoginAPFailed(int uSrvResCode) {
		// TODO Auto-generated method stub
	}

	/**
	 * 表示被系统踢下线，子类根据需要重写该方法<br/>
	 * @param code	原因代码
	 * @param reason	原因
	 */
	protected void onKickOffEvent(int code, String reason) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAttach(Activity activity) {
		mContext = activity.getApplicationContext();

		//只能关联到实现了IUdbAuthActivityCallback接口的Activity
		if (!(getActivity() instanceof IUdbAuthActivityCallback)) {
			throw new RuntimeException("only can attach to Activity that implement IUdbAuthActivityCallback");
		} else {
			mOnUdbAuthActivityCallback = (IUdbAuthActivityCallback) getActivity();
		}

		//添加回调事件监听器
		AuthUI.getInstance().addAuthEventWatcher(mAuthEventWatcher);
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		//移除回调事件监听器
		AuthUI.getInstance().removeAuthEventWatcher(mAuthEventWatcher);
		super.onDetach();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mHasComebackFromOther = savedInstanceState.getBoolean(KEY_COME_BACK, mHasComebackFromOther);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		if (getActivity() != null) {
			getActivity().setResult(RESULT_CODE_FOR_AUTH);
		}
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CODE_FOR_AUTH) {
			mHasComebackFromOther = true;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 
	 * 将指定的Fragment嵌入到UdbAuthActivity中，并启动<br/>
	 * 该函数用于记录是否从本页面打开了一个新的页面
	 */
	public void startFragmentForResult(Class<? extends Fragment> fragmentClass) {
		mHasComebackFromOther = false;
		FragmentHelper.startFragmentForResult(getActivity(), fragmentClass, REQ_CODE_FOR_AUTH, null);
	}

	/** 
	 * 将指定的Fragment嵌入到UdbAuthActivity中，并启动<br/>
	 * 该函数用于记录是否从本页面打开了一个新的页面
	 */
	public void startFragmentForResult(Class<? extends Fragment> fragmentClass, Bundle bundle) {
		mHasComebackFromOther = false;
		FragmentHelper.startFragmentForResult(getActivity(), fragmentClass, REQ_CODE_FOR_AUTH, bundle);
	}

	/** 
	 * 将指定的Fragment嵌入到UdbAuthActivity中，并启动<br/>
	 * 该函数用于记录是否从本页面打开了一个新的页面
	 */
	public void startFragmentForResult(Class<? extends Fragment> fragmentClass, int resultCode, Bundle bundle) {
		mHasComebackFromOther = false;
		FragmentHelper.startFragmentForResult(getActivity(), fragmentClass, resultCode, bundle);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(KEY_COME_BACK, mHasComebackFromOther);
		super.onSaveInstanceState(outState);
	}

	/**
	 * 用于判断是否应该处理回调
	 * @return	true表示是，false表示否
	 */
	public boolean shouldHandle() {
		return mHasComebackFromOther;
	}

	public Context getContext() {
		return mContext;
	}

	public void finish() {
		if (getActivity() != null) {
			getActivity().finish();
		}
	}

	/** 
	 * 其实就是Activity.onBackPressed()或者标题栏返回按钮被点击，默认为finish()；<br/>
	 * 如有需要，请重写
	 * @return 返回是否已经消费了该事件，如果返回false，将由Activity处理
	 */
	public boolean onBackPressed() {
		return false;
	}

	/** 向服务端发送一个Auth请求，如果失败不会显示一个Toast*/
	protected boolean sendAuthRequestSilent(AuthBaseReq auth) {
		return AuthUI.getInstance().sendAuthRequestWithToast(auth);
	}

	/** 向服务端发送一个Auth请求，如果失败会显示一个Toast*/
	protected boolean sendAuthRequest(AuthBaseReq auth) {
		return AuthUI.getInstance().sendAuthRequestWithToast(auth);
	}

	/** 显示一个Toast，短时间*/
	protected void showShortToast(String msg) {
		if (getContext() != null)
			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
	}

	/** 显示一个Toast，短时间*/
	protected void showShortToast(int res) {
		if (getContext() != null)
			Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
	}

	/** 显示一个Toast，长时间，还带有字符串格式化*/
	protected void showShortToast(int res, Object... args) {
		if (getContext() != null) {
			String msg = String.format(getContext().getString(res), args);
			Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
		}
	}

	/** 显示一个Toast，长时间*/
	protected void showLongToast(String msg) {
		if (getActivity() != null)
			Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
	}

	/** 显示一个Toast，长时间*/
	protected void showLongToast(int res) {
		if (getActivity() != null)
			Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
	}

	/** 显示一个Toast，长时间，还带有字符串格式化*/
	protected void showLongToast(int res, Object... args) {
		if (getContext() != null) {
			String msg = String.format(getContext().getString(res), args);
			Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 显示一个进度对话框
	 * 
	 * @param msg
	 *            不为null时，表示显示内容；为null时表示隐藏
	 * @param listener
	 *            当用户按返回键取消时所触发的监听器
	 */
	public void showProgressDialog(String msg, OnCancelListener listener) {
		mOnUdbAuthActivityCallback.showProgressDialog(msg, listener);
	}

	/**
	 * 显示一个进度对话框
	 * 
	 * @param resId
	 *            字符串资源ID
	 * @param listener
	 *            当用户按返回键取消时所触发的监听器
	 */
	public void showProgressDialog(int resId, OnCancelListener listener) {
		mOnUdbAuthActivityCallback.showProgressDialog(getActivity().getString(resId), listener);
	}

	/**
	 * 当cs中包含了html标签时，显示一个对话框；否则的话显示一个Toast
	 * @param cs	需要被显示的内容
	 */
	public void showToastOrHtmlDialog(String cs) {
		if (HtmlAlertDialog.isHtmlAlertDialog(cs)) {
			//失败，对话框提示
			try {
				new HtmlAlertDialog(getActivity()).setContent(cs).show();
			} catch (Exception e) {
				;
			}
		} else {
			showLongToast(cs);
		}
	}

	/** 设置标题栏文字*/
	public void setTitleBarText(String title) {
		mOnUdbAuthActivityCallback.setTitleBarText(title);
	}

	/** 设置标题栏文字*/
	public void setTitleBarText(int resId) {
		mOnUdbAuthActivityCallback.setTitleBarText(getString(resId));
	}

	/** 获取页面样式表*/
	public PageStyle getPageStyle() {
		return mOnUdbAuthActivityCallback.getPageStyle();
	}

	/** 适配按钮样式效果*/
	@SuppressWarnings("deprecation")
	public void adjustButtonStyle(Button btn) {
		if (btn != null && getPageStyle() != null) {
			btn.setBackgroundDrawable(getPageStyle().getButtonDrawable(getActivity()));
			btn.setTextColor(getPageStyle().getButtonTextDrawable());
		}
	}

	/** 适配默认文本样式效果*/
	public void adjustDefaultTextStyle(TextView txt) {
		if (txt != null && getPageStyle() != null) {
			txt.setTextColor(getPageStyle().textColor);
		}
	}

	/** 适配突出文本样式效果*/
	public void adjustStrikingTextStyle(TextView txt) {
		if (txt != null && getPageStyle() != null) {
			txt.setTextColor(getPageStyle().textStrikingColor);
		}
	}

	/** 设置View的可视状态*/
	protected void setupViewVisable(View rootView, int viewResId, boolean show) {
		if (rootView != null) {
			View v = rootView.findViewById(viewResId);
			if (v != null)
				v.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		}
	}
}
