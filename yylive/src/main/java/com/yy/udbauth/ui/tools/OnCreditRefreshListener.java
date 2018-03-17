package com.yy.udbauth.ui.tools;

import com.yy.udbauth.AuthEvent;

/** 凭证自动刷新监听器*/
public interface OnCreditRefreshListener {

	/** 
	 * 凭证自动刷新的时候回调
	 */
	public void onCreditRefresh(AuthEvent.CreditRenewEvent res);
}