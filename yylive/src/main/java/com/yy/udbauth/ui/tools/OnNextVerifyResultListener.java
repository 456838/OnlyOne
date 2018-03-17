package com.yy.udbauth.ui.tools;

import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.AuthEvent.NextVerify;

/** 二次验证选填结果监听器*/
public interface OnNextVerifyResultListener {

	/** 二次验证选填结果
	 * @param token	用户填写的结果
	 * @param strategy	二次验证的类型
	 */
	public void onVerifyResult(String token, int strategy);
	
	/**
	 * 要求切换验证方式
	 * @param nv	切换为哪一个验证方式
	 */
	public void onSwitchVerify(NextVerify nv);
	
	/**
	 * 上行短信发送成功，表示已经登录
	 * @param et		登录结果
	 * @param strategy	所用策略
	 */
	public void onLoginSuccess(AuthEvent.LoginEvent et, int strategy);
}