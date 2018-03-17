package com.yy.udbauth.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.yy.live.R;
import com.yy.udbauth.AuthEvent.NextVerify;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.fragment.VerifyFragment.OnTokenErrorListener;
import com.yy.udbauth.ui.tools.AndroidHelper;
import com.yy.udbauth.ui.tools.OnNextVerifyResultListener;

/**
 * 概述：Web交互验证
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月25日 下午6:22:49
 */
public class WebVerifyFragment extends UdbAuthBaseFragment implements OnTokenErrorListener {

	private static final String ENCODING = "UTF-8";

	View mMainView;
	WebView mWebView;
	TextView mTvTitle;

	//二次验证的内容
	NextVerify mNextVerify;

	public WebVerifyFragment(NextVerify v) {
		mNextVerify = v;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_web_verify;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mWebView = (WebView) mMainView.findViewById(R.id.ua_web_verify_webview);
		mTvTitle = (TextView) mMainView.findViewById(R.id.ua_fragment_verify_txt_title);

		mTvTitle.setText(mNextVerify.promptTitle + " " + mNextVerify.promptContent);

		//设置标题
		setTitleBarText(R.string.ua_title_second_verify);

		//初始化并显示交互验证
		initWebView();

		//适配样式表
		adjustPageStyle();

		return mMainView;
	}

	/** 适配样式表*/
	private void adjustPageStyle() {
		// 适配样式效果
		adjustDefaultTextStyle(mTvTitle);
	}

	/** 初始化WebView*/
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		String content = AndroidHelper.getStringFromResRaw(getContext(), R.raw.ua_wv);
		byte[] js = Base64.decode(mNextVerify.data, Base64.DEFAULT);
		String jsString = new String(js);
		String html = String.format(content, jsString);

		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.addJavascriptInterface(new WebBridge(), "WebBridge");
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadDataWithBaseURL(null, html, "text/html", ENCODING, null);
	}

	class WebBridge {
		/**
		 * 给JS回调的，表示交互验证结果
		 * @param token		JS返回的TOKEN
		 * @param success	结果，成功为true，失败为false
		 */
		@JavascriptInterface
		public void onDrawVerifyResult(final String token, boolean success) {

			if (success) {
				//验证成功，返回主线程工作
				mWebView.post(new Runnable() {
					@Override
					public void run() {
						if (getParentFragment() instanceof OnNextVerifyResultListener) {
							((OnNextVerifyResultListener) getParentFragment()).onVerifyResult(token,
									mNextVerify.strategy);
						}
					}
				});
			} else {
				//Web页面已经有提示了，这里就不再提示了
				//showShortToast(R.string.ua_web_verify_failed);
			}
		}

		@JavascriptInterface
		public String toString() {
			return "injectedObject";
		}
	}

	@Override
	public void onTokenError() {
	}
}
