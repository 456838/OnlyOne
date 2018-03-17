package com.yy.udbauth.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yy.live.R;
import com.yy.udbauth.ui.AuthUI;

/**
 * 概述：WebView页面，用于显示一些网页内容，如“用户协议”
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年2月23日 上午10:30:06
 */
public class WebViewFragment extends UdbAuthBaseFragment {

	/** 表示传递一个URL字符串*/
	public static final String EXTRA_URL = "extra_url";
	/** 表示传递一个URL对应的标题*/
	public static final String EXTRA_TITLE = "extra_title";

	View mMainView;
	WebView mWebView;

	String mURL;
	String mTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//看看是不是用了自定义布局
		final int layout = AuthUI.getInstance().getLayoutRes().ua_fragment_webview;
		mMainView = inflater.inflate(layout, container, false);

		//关联布局元素
		mWebView = (WebView) mMainView.findViewById(R.id.ua_webview_webview);

		//外部数据
		mURL = getArguments().getString(EXTRA_URL);
		mTitle = getArguments().getString(EXTRA_TITLE);

		//设置标题
		setTitleBarText(mTitle);

		//初始化并显示交互验证
		initWebView();

		return mMainView;
	}

	/** 初始化WebView*/
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		mWebView.addJavascriptInterface(new WebBridge(), "WebBridge");
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.loadUrl(mURL);
		mWebView.setWebChromeClient(client);
		mWebView.setWebViewClient(webviewClient);
	}

	class WebBridge {
		@JavascriptInterface
		public String toString() {
			return "injectedObject";
		}
	}

	WebChromeClient client = new WebChromeClient() {

	};
	WebViewClient webviewClient = new WebViewClient() {

	};
}
