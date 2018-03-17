package com.yy.udbauth.ui.tools;

import com.yy.udbauth.AuthEvent.AuthBaseEvent;

/**
 * 概述：回调监听 
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月22日 下午5:20:07
 */
public interface OnUdbAuthListener {

	/** 表示未知错误*/
	public static final int ERROR_UNKNOWN = 999;

	/** 表示【账号+密码+二次验证】时，传入的用户名不合法*/
	public static final int ERROR_NEXT_VERIFY_PARAMS_INVALID_USERNAME = 201;
	/** 表示【账号+密码+二次验证】时，传入的密码不合法*/
	public static final int ERROR_NEXT_VERIFY_PARAMS_INVALID_PASSWORD = 202;
	/** 表示【UID+凭证+二次验证】时，传入的用户标识UID不合法*/
	public static final int ERROR_NEXT_VERIFY_PARAMS_INVALID_UID = 203;
	/** 表示【UID+凭证+二次验证】时，传入的登录凭证不合法*/
	public static final int ERROR_NEXT_VERIFY_PARAMS_INVALID_CREDIT = 204;
	/** 表示【二次验证】时，传入的二次验证内容不合法*/
	public static final int ERROR_NEXT_VERIFY_PARAMS_INVALID_VERIFY = 205;

	/** 表示【修改密码】时，传入的账号不合法*/
	public static final int ERROR_USER_INVALID = 305;
	/** 表示【修改密码】时，该账号不支持短信改密*/
	public static final int ERROR_NOT_SUPPORT = 306;

	/** 
	 * 用户操作成功<br/>
	 * 
	 * @param event	用户操作返回的数据
	 * @param type  用户操作类型
	 **/
	public void onSuccess(AuthBaseEvent event, OpreateType type);

	/** 
	 * 表示操作出错，参考ERROR_开关常量 
	 * @param type  用户操作类型
	 */
	public void onError(int errCode, OpreateType type);

	/**
	 * 用户取消操作
	 * 
	 * @param type  用户操作类型
	 */
	public void onCancel(OpreateType type);
}
