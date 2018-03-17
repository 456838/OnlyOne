package com.yy.udbauth.ui.tools;

import com.yy.udbauth.AuthEvent.LoginEvent;
import com.yy.udbauth.AuthEvent.UIAction;

/**
 * 概述：凭证登录回调监听 
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月22日 下午5:20:07
 */
public interface OnCreditLoginListener {

	/** 
	 * 凭证登录返回了结果，请根据{@link LoginEvent#uiAction}来判断是否成功，包含如下：<br/>
	 * <p>
	 * <li>{@link UIAction#SUCCESS}</li>
	 * <li>{@link UIAction#FAILED}</li>
	 * <li>{@link UIAction#NEXT_VERIFY}</li>
	 * <li>{@link UIAction#CREDIT_INVALID}</li>
	 * </p>
	 * @param et	用户登录信息
	 **/
	public void onResult(LoginEvent et);

	/**
	 * 凭证登录超时了
	 */
	public void onTimeout();

}
