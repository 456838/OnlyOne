package com.yy.udbauth.ui.tools;

/**
 * 概述：表示用户操作类型，可能是账号密码登录、短信登录、短信注册、修改密码、找回密码
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年2月25日 下午3:30:18
 */
public enum OpreateType {

	/** 无*/
	NONE,
	/** 账号密码登录 */
	PWD_LOGIN,
	/** 短信登录*/
	SMS_LOGIN,
	/** 短信注册*/
	SMS_REGISTER,
	/** 找回密码*/
	FIND_MY_PWD,
	/** 修改密码 */
	MODIFY_PWD,
	/** 凭证登录（静默登录，无UI登录） */
	CREDIT_LOGIN,
	/** 二次验证 */
	NEXT_VERIFY;

	/** 转换为日志上报格式*/
	public String toString() {
		if (this == PWD_LOGIN)
			return "PWD_LOGIN";
		else if (this == SMS_LOGIN)
			return "SMS_LOGIN";
		else if (this == SMS_REGISTER)
			return "SMS_REG";
		else if (this == FIND_MY_PWD)
			return "FIND_MY_PWD";
		else if (this == MODIFY_PWD)
			return "MOD_PWD";
		else if (this == CREDIT_LOGIN)
			return "CREDIT_LOGIN";
		else if (this == NEXT_VERIFY)
			return "NEXT_VERIFY";
		else
			return "NONE";
	};

}
