package com.yy.udbauth.ui;

import com.yy.udbauth.AuthEvent;

/**
 * 概述：处理AuthEvent的Handler
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年5月5日 下午4:49:08
 */
public interface IAuthEventWatcher {

	/**
	 * 这里处理AuthEvent<br/>
	 * 不要直接调用此方法，可能会有线程隐患。
	 * @param base	响应结果AuthBaseEvent
	 */
	public abstract void onAuthRes(AuthEvent.AuthBaseEvent base);

	/**
	 * 对于进频道类APP，登录过程分为两步：第一步是验证登录信息，第二步是登录AP，两个步骤都成功，才算是登录成功。<br/>
	 * 这里登录AP失败，需要做相关处理<br/>
	 * 不要直接调用此方法，可能会有线程隐患。
	 * @param	uSrvResCode	返回码
	 */
	public abstract void onLoginAPFalied(int uSrvResCode);
	
	/**
	 * 对于进频道类APP，可能会存在着被踢下线的情况，这里处理被踢的情况
	 * @param code 	被踢代码
	 * @param reason	被踢原因
	 */
	public abstract void onKickOff(int code, String reason);
}
