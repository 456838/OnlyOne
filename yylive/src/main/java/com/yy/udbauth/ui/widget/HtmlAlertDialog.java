package com.yy.udbauth.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;

import com.yy.live.R;


/**
 * 概述：自定义Html对话框，用户显示带html标签的文字内容
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年2月29日 下午6:18:23
 */
public class HtmlAlertDialog {

	String mContent;
	Dialog mDialog;
	Activity mActivity;

	public HtmlAlertDialog(Activity activity) {
		mActivity = activity;
	}

	public HtmlAlertDialog setContent(String content) {
		mContent = content;
		return this;
	}

	public String getContent() {
		return mContent;
	}

	public void show() {
		UdbDialog.Builder dialog = new UdbDialog.Builder(mActivity);
		dialog.setTitle(R.string.ua_dialog_title);
		dialog.setMessage(Html.fromHtml(mContent));
		dialog.setNegativeButton(R.string.ua_dialog_ok, null);
		dialog.create().show();
	}

	public static boolean isHtmlAlertDialog(String html) {
		try {
			return html.matches(".*<([^>]*)>.*");
		} catch (Exception e) {
			return false;
		}
	}
}
