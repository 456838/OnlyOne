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
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;
import com.yy.udbauth.ui.widget.UdbEditText;

/**
 * 
 * 概述：硬件令牌页面
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年11月6日 下午4:29:03
 */
public class HardwareVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	View mMainView;
	UdbEditText mEtToken;
	TextView mTvTitle;
	Button mBtnSubmit;

	//二次验证的内容
	NextVerify mNextVerify;

	public HardwareVerifyFragment(NextVerify v) {
		mNextVerify = v;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_hardware_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mBtnSubmit = (Button) mMainView.findViewById(R.id.ua_fragment_verify_btn_ok);
		mEtToken = (UdbEditText) mMainView.findViewById(R.id.ua_fragment_verify_et_token);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle + "" + mNextVerify.promptContent);
		mBtnSubmit.setOnClickListener(onSubmitClickListener);
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
		adjustDefaultTextStyle(mTvTitle);
	}

	/** 提交token*/
	OnClickListener onSubmitClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String token = mEtToken.getText().toString().trim();

			if (TextUtils.isEmpty(token)) {
				showShortToast(R.string.ua_empty_hardware_token);
				return;
			}

			if (getParentFragment() instanceof OnNextVerifyResultListener) {
				((OnNextVerifyResultListener) getParentFragment()).onVerifyResult(token, mNextVerify.strategy);
			}
		}
	};

	@Override
	public void onTokenError() {
		showShortToast(R.string.ua_login_failed_with_err_hcode);
	}
}
