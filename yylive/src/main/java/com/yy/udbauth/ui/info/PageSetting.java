package com.yy.udbauth.ui.info;

/**
 * 概述：页面的入口
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年8月4日 上午10:16:39
 */
public class PageSetting {
	
	/** 是否在登录页面显示“找回密码”*/
	public boolean loginPage_showFindMyPassword = true;

	/** 是否在登录页面显示“手机短信登录”*/
	public boolean loginPage_showSmsLogin = true;

	/** 是否在登录页面显示“手机注册” */
	public boolean loginPage_showRegister = true;

	
	
	/** 是否显示短信登录页面中的“账号密码登录”*/
	public boolean smsLoginPage_showPasswordLogin = true;

	/** 是否在注册页面中使用自动登录功能<br/>
	 *  <li>true表示是，在OnUdbAuthListener.onSuccess()中返回LoginEvent</li>
	 *  <li>false表示否，在OnUdbAuthListener.onSuccess()返回RegisterEvent</li>
	 */
	public boolean smsRegisterPage_autoLogin = false;
}
