package com.yy.udbauth.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 
 * 概述：软件令牌页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年11月6日 下午4:29:03
 */
public class MobileVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	private static final String KEY_NEXTVERIFY = "key_nextverify";

	private static final String URL_INSTALL = "https://aq.yy.com/app/yysec/index.html";

	View mMainView;
	Button mBtnSubmit;
	TextView mTvTitle;
	UdbEditText mEtToken;
	TextView mTvNotInstallYet;

	//二次验证的内容
	NextVerify mNextVerify;

	/**
	 * 当Fragment被摧毁并重建的时候，Android比较调用无参构建函数，如果没有无参构建函数，会崩溃。<br/>
	 * 所以这里保留无参数构建函数，并提供该方法来设置初始参数
	 */
	public void setConstructorParams(NextVerify v) {
		mNextVerify = v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(KEY_NEXTVERIFY, mNextVerify);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mNextVerify = (NextVerify) savedInstanceState.getSerializable(KEY_NEXTVERIFY);
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_mobile_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_fragment_verify_btn_ok);
		mTvNotInstallYet = (TextView) mMainView.findViewById(R.id.ua_verify_txt_not_install_yet);
		mEtToken = (UdbEditText) mMainView.findViewById(R.id.ua_fragment_verify_et_token);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle + "" + mNextVerify.promptContent);
		mBtnSubmit.setOnClickListener(onSubmitClickListener);
		mTvNotInstallYet.setOnClickListener(onGotoInstallListener);
		mEtToken.bindCleanButton(R.id.ua_fragment_verify_btn_clear_token);
		mEtToken.setHint(mNextVerify.selectTitle);

		//设置标题
		setTitleBarText(R.string.ua_title_second_verify);

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustButtonStyle(mBtnSubmit);
		adjustStrikingTextStyle(mTvNotInstallYet);
		adjustDefaultTextStyle(mTvTitle);
	}

	OnClickListener onGotoInstallListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Uri content_url = Uri.parse(URL_INSTALL);
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(content_url);
			startActivity(intent);
		}
	};

	/** 提交token*/
	OnClickListener onSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String token = mEtToken.getText().toString().trim();

			if (TextUtils.isEmpty(token)) {
				showShortToast(R.string.ua_empty_mobile_token);
				return;
			}

			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onVerifyResult(token, mNextVerify.strategy);
			}
		}
	};

	@Override
	public void onTokenError() {
		showShortToast(R.string.ua_login_failed_with_err_mcode);
	}
}
