package com.yy.udbauth.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.LoginEvent;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.AuthEvent.TimeoutEvent;
import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.AuthRequest.STRATEGY;
import com.yy.udbauth.ui.AuthCallbackProxy;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.tools.CountdownHelper;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.tools.OnUdbAuthListener;
import com.yy.udbauth.ui.tools.OpreateType;

import java.util.ArrayList;

/**
 * 
 * 概述：二次验证页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年11月6日 下午4:29:03
 */
public class VerifyFragment extends UdbAuthBaseFragment implements OnNextVerifyResultListener {

	private static final String KEY_CURR_STRATEGY = "key_curr_strategy";

	/** 表示传递一个二次验证策略的数组，ArrayList<NextVerify>*/
	public static final String EXTRA_STRATEGIES = "extra_strategies";
	/** 表示传递一个二次验证策略，NextVerify*/
	public static final String EXTRA_STRATEGY = "extra_strategy";
	/** 表示传递一个字符串，用户登录时输入的账号*/
	public static final String EXTRA_USERNAME = "extra_username";
	/** 表示传递一个字符串，用户登录时输入的密码（使用SHA1加密）*/
	public static final String EXTRA_PASSWD_SHA1 = "extra_password_sha1";
	/** 表示传递一个字符串，用户标识UID */
	public static final String EXTRA_UID = "extra_uid";
	/** 表示传递一个字符串，用户上次登录时，后端返回的credit */
	public static final String EXTRA_CREDIT = "extra_credit";
	/** 表示传递一个登录类型，默认0，表示是帐号；如果为1，表示凭证登录*/
	public static final String EXTRA_LOGIN_TYPE = "extra_login_type";
	/** 表示传递一个AccountInfo对象，登录结果*/
	public static final String EXTRA_LOGIN_EVENT = "extra_account_info";
	/** 表示登录结果*/
	public static final int RESULT_ACCOUNT_INFO = 345271;

	/** 表示帐号密码登录*/
	public static final int LOGIN_BY_PASSWORD = 0;
	/** 表示凭证登录*/
	public static final int LOGIN_BY_CREDIT = 1;

	private LinearLayout mSwitchVerifyLayout;
	private LinearLayout mOtherItemLayout;

	/** 用于记录上次显示的是上行短信还是下行短信*/
	private int mLastSmsStrategy = -1;
	/** 用于记录正在显示的策略*/
	private int mCurrStrategy = -1;
	private ArrayList<NextVerify> mStrategies;
	private String mUsername;
	private String mPasswdSha1;
	private String mUID;
	private String mCredit;
	private String mRequestContext;

	private HardwareVerifyFragment mHardwareVerifyFragment;
	private MobileVerifyFragment mMobileVerifyFragment;
	private PictureVerifyFragment mPictureVerifyFragment;
	private SmsVerifyFragment mSmsVerifyFragment;
	private WebVerifyFragment mWebVerifyFragment;
	private SmsUpVerifyFragment mSmsUpVerifyFragment;
	private OnTokenErrorListener mOnTokenErrorListener;
	private UdbAuthBaseFragment mCurrFragment;

	private FragmentManager mFragmentManager;
	private LayoutInflater mLayoutInflater;

	private View mMainView;

	//登录类型，默认0，表示是帐号；如果为1，表示凭证登录
	private int mLoginType = 0;

	//是否已经通知业务操作结果了
	boolean hasCallback = false;

	@Override
	public void onDestroy() {
		//如果还没有回调过，并且业务调用入口为当前的类，通知业务用已经取消
		if (!hasCallback && AuthCallbackProxy.getPurposeOpreateType() == OpreateType.NEXT_VERIFY) {
			AuthCallbackProxy.onCancel(OpreateType.NEXT_VERIFY);
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mSwitchVerifyLayout = (LinearLayout) mMainView.findViewById(R.id.ua_fragment_verify_switch_verify_layout);
		mOtherItemLayout = (LinearLayout) mMainView.findViewById(R.id.ua_fragment_verify_other_item_layout);

		mLayoutInflater = inflater;
		mFragmentManager = getChildFragmentManager();

		//获取并校验传入参数
		if (!getAndCheckParams()) {
			return super.onCreateView(inflater, container, savedInstanceState);
		}

		if (savedInstanceState != null && savedInstanceState.containsKey(KEY_CURR_STRATEGY)) {
			mCurrStrategy = savedInstanceState.getInt(KEY_CURR_STRATEGY);
			for (NextVerify nv : mStrategies) {
				if (nv.strategy == mCurrStrategy) {

					showSubFragment(nv);
					break;
				}
			}
		} else {
			//显示第一个验证策略
			showSubFragment(mStrategies.get(0));
		}

		return mMainView;
	}

	@SuppressWarnings("unchecked")
	private boolean getAndCheckParams() {
		try {
			//获取二次验证的内容
			mStrategies = (ArrayList<NextVerify>) getArguments().getSerializable(EXTRA_STRATEGIES);
			mUsername = getArguments().getString(EXTRA_USERNAME);
			mPasswdSha1 = getArguments().getString(EXTRA_PASSWD_SHA1);
			mUID = getArguments().getString(EXTRA_UID);
			mCredit = getArguments().getString(EXTRA_CREDIT);
			mLoginType = getArguments().getInt(EXTRA_LOGIN_TYPE, LOGIN_BY_PASSWORD);

			//检验传入参数
			if (AuthCallbackProxy.getPurposeOpreateType() == OpreateType.NEXT_VERIFY) {
				if (mUsername == null && mLoginType == LOGIN_BY_PASSWORD) {
					//一定要有用户名
					AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NEXT_VERIFY_PARAMS_INVALID_USERNAME,
							OpreateType.NEXT_VERIFY);
					hasCallback = true;
					finish();
					return false;
				} else if (mPasswdSha1 == null && mLoginType == LOGIN_BY_PASSWORD) {
					//使用密码登录，但是密码为空
					AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NEXT_VERIFY_PARAMS_INVALID_PASSWORD,
							OpreateType.NEXT_VERIFY);
					hasCallback = true;
					finish();
					return false;
				} else if (mUID == null && mLoginType == LOGIN_BY_CREDIT) {
					//使用凭证登录，但是UID为空
					AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NEXT_VERIFY_PARAMS_INVALID_UID,
							OpreateType.NEXT_VERIFY);
					hasCallback = true;
					finish();
					return false;
				} else if (mCredit == null && mLoginType == LOGIN_BY_CREDIT) {
					//使用密码登录，但是凭证为空
					AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NEXT_VERIFY_PARAMS_INVALID_CREDIT,
							OpreateType.NEXT_VERIFY);
					hasCallback = true;
					finish();
					return false;
				}
			}

			//检验传入二次验证策略
			if (mStrategies == null || mStrategies.size() <= 0) {
				throw new Exception();
			}

		} catch (Exception e) {
			if (AuthCallbackProxy.getPurposeOpreateType() == OpreateType.NEXT_VERIFY) {
				AuthCallbackProxy.onError(OnUdbAuthListener.ERROR_NEXT_VERIFY_PARAMS_INVALID_VERIFY,
						OpreateType.NEXT_VERIFY);
				hasCallback = true;
			} else {
				showLongToast(R.string.ua_invalid_second_verify_content);
			}
			finish();
			return false;
		}
		return true;
	}

	@Override
	public boolean onBackPressed() {
		if (mCurrFragment == null) {
			return super.onBackPressed();
		} else {
			return mCurrFragment.onBackPressed();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(KEY_CURR_STRATEGY, mCurrStrategy);
		super.onSaveInstanceState(outState);
	}

	/** 根据二次验证策略的内容，显示对应的页面*/
	private void showSubFragment(NextVerify verify) {
		int strategy = verify.strategy;
		UdbAuthBaseFragment fragment = null;

		switch (strategy) {
		case STRATEGY.PICCODE: {
			//图片验证码
			if (mPictureVerifyFragment == null) {
				mPictureVerifyFragment = new PictureVerifyFragment(verify, mUsername);
			}
			fragment = mPictureVerifyFragment;
			mOnTokenErrorListener = mPictureVerifyFragment;
			break;
		}
		case STRATEGY.SLIDE: {
			//WEB交互验证
			if (mWebVerifyFragment == null) {
				mWebVerifyFragment = new WebVerifyFragment(verify);
			}
			fragment = mWebVerifyFragment;
			mOnTokenErrorListener = mWebVerifyFragment;
			break;
		}
		case STRATEGY.MOBTOKEN: {
			//手机令牌验证
			if (mMobileVerifyFragment == null) {
				mMobileVerifyFragment = new MobileVerifyFragment();
				mMobileVerifyFragment.setConstructorParams(verify);
			}
			fragment = mMobileVerifyFragment;
			mOnTokenErrorListener = mMobileVerifyFragment;
			break;
		}
		case STRATEGY.HWTOKEN: {
			//硬件令牌验证
			if (mHardwareVerifyFragment == null) {
				mHardwareVerifyFragment = new HardwareVerifyFragment(verify);
			}
			fragment = mHardwareVerifyFragment;
			mOnTokenErrorListener = mHardwareVerifyFragment;

			break;
		}
		case STRATEGY.SMSCODE: {
			//手机短信验证（下行短信）
			if (mSmsVerifyFragment == null) {

				//判断是否包含了上行短信验证，如果有，那么取出
				NextVerify v = null;
				for (NextVerify n : mStrategies) {
					if (n.strategy == STRATEGY.SMS_UP) {
						v = n;
						break;
					}
				}

				if (mLoginType == LOGIN_BY_CREDIT) {
					mSmsVerifyFragment = new SmsVerifyFragment(verify, mUID, true, v);
				} else {
					mSmsVerifyFragment = new SmsVerifyFragment(verify, mUsername, false, v);
				}
			}

			mLastSmsStrategy = STRATEGY.SMSCODE;
			fragment = mSmsVerifyFragment;
			mOnTokenErrorListener = mSmsVerifyFragment;
			break;
		}
		case STRATEGY.SMS_UP: {
			//上行短信验证
			if (mSmsUpVerifyFragment == null) {

				//判断是否包含了下行短信验证，如果有，那么取出
				NextVerify v = null;
				for (NextVerify n : mStrategies) {
					if (n.strategy == STRATEGY.SMSCODE)
						v = n;
				}

				mSmsUpVerifyFragment = new SmsUpVerifyFragment();
				mSmsUpVerifyFragment.setConstructorParams(verify, mUsername, mPasswdSha1, v);
			}

			mLastSmsStrategy = STRATEGY.SMS_UP;
			fragment = mSmsUpVerifyFragment;
			mOnTokenErrorListener = mSmsUpVerifyFragment;
			break;
		}
		default: {
			//登录失败
			showLongToast(R.string.ua_invalid_second_verify_type);
			break;
		}
		}

		if (fragment != null) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.replace(R.id.ua_verify_container, fragment);
			ft.commit();

			mCurrStrategy = strategy;
			mCurrFragment = fragment;
			showOtherVerifyLayout(strategy);
		}

		try {
			InputMethodManager manager = (InputMethodManager) getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromInputMethod(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 显示其他验证方式*/
	private void showOtherVerifyLayout(int strategy) {

		mOtherItemLayout.removeAllViews();

		boolean visable = false;
		for (NextVerify nv : mStrategies) {

			//说明：不显示当前的
			if (nv.strategy == strategy) {
				continue;
			}

			//说明：不管当前显示的是上行短信还是下行短信，都不在其它布局中显示任何的短信验证
			if ((strategy == STRATEGY.SMS_UP || strategy == STRATEGY.SMSCODE)
					&& (nv.strategy == STRATEGY.SMS_UP || nv.strategy == STRATEGY.SMSCODE)) {
				continue;
			}

			/*说明：如果当前不是上行短信又不是下行短信，那么仅显示下行短信不显示上行短信
			 * 当用户点击的是下行短信的时候，再判断最近打开的是什么就切换回什么*/
			if ((strategy != STRATEGY.SMS_UP || strategy != STRATEGY.SMSCODE) && (nv.strategy == STRATEGY.SMS_UP)) {
				continue;
			}

			visable = true;

			//增加一组
			View view = mLayoutInflater.inflate(R.layout.ua_item_verify, mOtherItemLayout, false);
			TextView tv = (TextView) view.findViewById(R.id.ua_item_verify_txt);
			ImageView img = (ImageView) view.findViewById(R.id.ua_item_verify_img);

			tv.setText(nv.selectTitle);
			img.setImageResource(getImageResource(nv.strategy));
			view.setClickable(true);
			view.setOnClickListener(new OnSwitchVerifyClickListener(nv));

			mOtherItemLayout.addView(view);
		}

		mSwitchVerifyLayout.setVisibility(visable ? View.VISIBLE : View.GONE);
	}

	/** 获取二次验证图标*/
	private int getImageResource(int strategy) {
		switch (strategy) {
		case STRATEGY.SMSCODE:
			return R.drawable.ua_ic_sms;
		case STRATEGY.MOBTOKEN:
			return R.drawable.ua_ic_mobile;
		case STRATEGY.HWTOKEN:
			return R.drawable.ua_ic_hardware;
		default:
			return R.drawable.ua_ic_other_verify;
		}
	}

	@Override
	public void onVerifyResult(String token, int strategy) {

		if (mLoginType == LOGIN_BY_PASSWORD) {

			//二次验证填写结果，进行登录
			//调用账号密码登录接口
			mRequestContext = Long.toString(System.currentTimeMillis());
			AuthRequest.LoginReq auth = new AuthRequest.LoginReq(mUsername, mPasswdSha1, strategy, token,
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

		} else if (mLoginType == LOGIN_BY_CREDIT) {

			//调用账号密码登录接口
			mRequestContext = Long.toString(System.currentTimeMillis());
			AuthRequest.CreditLoginReq auth = new AuthRequest.CreditLoginReq(mUID, mCredit, strategy, token,
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
	}

	@Override
	public void onSwitchVerify(NextVerify nv) {
		showSubFragment(nv);
	}

	@Override
	public void onLoginSuccess(LoginEvent et, int strategy) {
		if (et != null && et.uiAction == AuthEvent.UIAction.SUCCESS && strategy == STRATEGY.SMS_UP) {
			//上行短信验证成功，登录成功 
			showShortToast(R.string.ua_login_success);

			Intent data = new Intent();
			data.putExtra(EXTRA_LOGIN_EVENT, et);
			getActivity().setResult(RESULT_ACCOUNT_INFO, data);
			finish();
		}
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

			if (AuthCallbackProxy.getPurposeOpreateType() == OpreateType.NEXT_VERIFY) {
				//二次验证被直接调用的，回调它
				et.user = mLoginType == LOGIN_BY_CREDIT ? et.passport : mUsername;
				AuthCallbackProxy.onAuthSuccess(et, OpreateType.NEXT_VERIFY);
				finish();
			} else {
				//帐号密码登录过来的，返回结果给它
				Intent data = new Intent();
				data.putExtra(EXTRA_LOGIN_EVENT, et);
				getActivity().setResult(RESULT_ACCOUNT_INFO, data);
				finish();
			}
		} else if (et.uiAction == AuthEvent.UIAction.NEXT_VERIFY) {

			//需要二次验证
			if (et.nextVerifies == null || et.nextVerifies.size() <= 0) {
				//登录失败
				showLongToast(R.string.ua_login_failed_with_empty_verify);
				finish();
			}

			mStrategies = et.nextVerifies;
			showSubFragment(mStrategies.get(0));

		} else if (et.uiAction == AuthEvent.UIAction.VERIFY_FAILED) {

			// 二次验证错误，停留在当前页面，继续进行验证
			mOnTokenErrorListener.onTokenError();

			if (mCurrFragment instanceof PictureVerifyFragment) {
				for (int i = 0; i < et.nextVerifies.size(); i++) {
					if (et.nextVerifies.get(i).strategy == STRATEGY.PICCODE) {
						((PictureVerifyFragment) mCurrFragment).showPicture(et.nextVerifies.get(i).data);
					}
				}
			}

		} else {

			//登录失败
			//			showLongToast(R.string.ua_login_failed, et.description);
			showLongToast(et.description);
			finish();
		}
	}

	/** 表示发送的请求超时了*/
	@Override
	protected void onTimeoutEvent(TimeoutEvent et) {
		if (mRequestContext != null && mRequestContext.equals(et.context)) {
			showProgressDialog(null, null);
			showShortToast(R.string.ua_timeout_login);
		}
	}

	class OnSwitchVerifyClickListener implements OnClickListener {
		NextVerify nv;

		public OnSwitchVerifyClickListener(NextVerify nv) {
			this.nv = nv;
		}

		@Override
		public void onClick(View v) {
			//说明：如果用户点击了下行短信，并且最后显示的是上行短信，那么就显示上行短信吧
			if (nv.strategy == STRATEGY.SMSCODE && mLastSmsStrategy == STRATEGY.SMS_UP) {
				//先抽把上行短信抽出来
				for (NextVerify n : mStrategies) {
					if (n.strategy == STRATEGY.SMS_UP) {
						showSubFragment(n);
						break;
					}
				}
			} else {
				showSubFragment(nv);
			}
		}
	};

	/** 二次验证的TOKEN错误，用于通知二次验证页面*/
	public interface OnTokenErrorListener {
		public void onTokenError();
	}
}
