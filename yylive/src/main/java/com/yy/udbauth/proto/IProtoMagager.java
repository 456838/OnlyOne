package com.yy.udbauth.proto;

import android.content.Context;

import com.yy.udbauth.AuthRequest;
import com.yy.udbauth.ui.IAuthEventWatcher;
import com.yy.udbauth.ui.InitCode;

/**
 * 概述：收发包管理器，这是虚基类，进频道和不进频道需要分别继承该类，并实现相关方法<br/>
 * <strong>注意：请在{@link #getDefault()}方法中，返回一个实例</strong>
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年5月5日 下午4:51:19
 */
public abstract class IProtoMagager {

	static IProtoMagager sIProtoMagager = null;

	/** 获取默认的收发包管理器*/
	public static IProtoMagager getDefault() {
		if (sIProtoMagager == null) {
			//sIProtoMagager = new NonChannelProtoManager();//这里返回的是不进频道的管理器 
			sIProtoMagager = new YYSdkProtoManager();//这里返回的是进频道的管理器 
		}
		return sIProtoMagager;
	}
	
	/** 获取收发包管理器的名称，供日志上报使用，如“yysdk”“loginsdk”*/
	public abstract String getName();

	/**
	 * 初始化
	 * @param context	ApplicationContext
	 * @param appid		业务APPID，请到UDB组注册
	 * @param appkey	业务APPKEY，请到UDB组注册
	 * @param terminalType	终端类型标识，请向信令组注册
	 * @param enableAnonymous	是否允许匿名登录，仅对进频道类SDK有效，其它情况请忽略
	 * @return	返回是否初始化成功，请参看{@link InitCode}定义的常量，其中{@link InitCode#SUCCESS}表示成功
	 */
	public abstract int init(Context context, String appid, String appkey, String terminalType, boolean enableAnonymous);

	/**
	 * 发送一个AuthReq
	 * @param auth	将要被发送出去的请求
	 * @return	返回是否发送成功，0表示成功，其它情况，请参考相关文档
	 */
	public abstract int sendAuthRequest(AuthRequest.AuthBaseReq auth);

	/**
	 * 发送一个AuthReq，如果发送失败，则弹出一个Toast，显示相关错误信息，如果成功则不显示Toast
	 * @param auth	将要被发送出去的请求
	 * @return	返回是否发送成功
	 */
	public abstract boolean sendAuthRequestWithToast(AuthRequest.AuthBaseReq auth);

	/**
	 * 增加一个回调事件监听器
	 * @param watcher	回调事件监听器
	 */
	public abstract void addAuthEventWatcher(IAuthEventWatcher watcher);

	/**
	 * 移除一个回调事件监听器
	 * @param watcher	回调事件监听器
	 */
	public abstract void removeAuthEventWatcher(IAuthEventWatcher watcher);

	
	/**
	 * 注销
	 */
	public abstract void logout();

}
