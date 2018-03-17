package com.yy.udbauth.ui.tools;

import android.content.DialogInterface.OnCancelListener;

import com.yy.udbauth.ui.fragment.UdbAuthBaseFragment;
import com.yy.udbauth.ui.style.PageStyle;

/** 
 * 概述：{@link UdbAuthBaseFragment}被关联的Activity中，必须实现此接口，否则运行报错
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月25日 下午12:00:46
 */
public interface IUdbAuthActivityCallback {

	public void setTitleBarText(String title);

	public void setTitleText(int resId);

	public void showProgressDialog(String msg, OnCancelListener listener);

	public PageStyle getPageStyle();
}