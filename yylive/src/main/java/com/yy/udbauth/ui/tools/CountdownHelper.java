package com.yy.udbauth.ui.tools;

import com.yy.udbauth.ui.fragment.ModifyPasswordFragment;
import com.yy.udbauth.ui.fragment.RegisterFragment;
import com.yy.udbauth.ui.fragment.SmsLoginFragment;
import com.yy.udbauth.ui.fragment.SmsVerifyFragment;

/**
 * 概述：倒计时助手
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年6月17日 上午11:19:05
 */
public class CountdownHelper {

	/** 
	 * 重置下发短信验证码的倒计时，一般在登录的时候使用
	 */
	public static void resetSmsCode(){
		SmsVerifyFragment.LAST_TIME_GET_SMS_CODE = 0L;
		ModifyPasswordFragment.LAST_TIME_GET_SMS_CODE = 0L;
		RegisterFragment.LAST_TIME_GET_SMS_CODE = 0L;
		SmsLoginFragment.LAST_TIME_GET_SMS_CODE = 0L;
		SmsVerifyFragment.LAST_TIME_GET_SMS_CODE = 0L;
	}
}
