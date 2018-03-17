package com.yy.udbauth.ui.info;


import com.yy.live.R;

import java.io.Serializable;

/** 
 * 布局资源设置。如果想要完全自定义页面的布局，可以自己编写好布局这后，设置到这里（注意，该有的View ID要有）<br/>
 */
public class LayoutRes implements Serializable {
	private static final long serialVersionUID = -4503260035133245783L;

	/** 基础Activity页面，包含标题栏和用于存在Fragment的容器*/
	public int ua_activity_udbauth = R.layout.ua_activity_udbauth;
	
	
	/** 账号密码登录*/
	public int ua_fragment_login = R.layout.ua_fragment_login;
	/** 短信登录*/
	public int ua_fragment_sms_login = R.layout.ua_fragment_sms_login;

	/** 注册：容器*/
	public int ua_fragment_register = R.layout.ua_fragment_register;

	/** 二次验证：手机令牌*/
	public int ua_fragment_mobile_verify = R.layout.ua_fragment_mobile_verify;
	/** 二次验证：硬件令牌*/
	public int ua_fragment_hardware_verify = R.layout.ua_fragment_hardware_verify;
	/** 二次验证：图片验证码*/
	public int ua_fragment_picture_verify = R.layout.ua_fragment_picture_verify;
	/** 二次验证：下行短信验证*/
	public int ua_fragment_sms_verify = R.layout.ua_fragment_sms_verify;
	/** 二次验证：上行短信验证*/
	public int ua_fragment_upsms_verify = R.layout.ua_fragment_upsms_verify;
	/** 二次验证：容器布局*/
	public int ua_fragment_verify = R.layout.ua_fragment_verify;
	/** 二次验证：WEB交互式验证*/
	public int ua_fragment_web_verify = R.layout.ua_fragment_web_verify;

	/** 修改密码*/
	public int ua_fragment_modify_password = R.layout.ua_fragment_modify_password;
	/** 找回密码*/
	public int ua_fragment_find_my_password = R.layout.ua_fragment_find_my_password;

	/** 国家列表*/
	public int ua_fragment_country_select = R.layout.ua_fragment_country_select;

	/** WEB页面*/
	public int ua_fragment_webview = R.layout.ua_fragment_webview;
}
