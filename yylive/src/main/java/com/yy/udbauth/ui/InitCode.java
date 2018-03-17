package com.yy.udbauth.ui;

/**
 * 概述：初始化结果
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年7月28日 下午5:15:17
 */
public class InitCode { 
	/** 表示初始化成功*/
	public static int SUCCESS = 0;
	
	/** 表示AuthSDK初始化失败*/
	public static int AUTH_SDK_INIT_ERROR = 5;
	
	/** 表示AuthJNI初始化失败*/
	public static int AUTH_JNI_INIT_ERROR = 6;
	
	/** 表示Proto初始化失败*/
	public static int PROTO_INIT_ERROR = 7;
	
	/** 表示未知异常*/
	public static int UNKNOWN_EXCEPTION = 8;
	
	/** 表示未知错误*/
	public static int UNKNOWN_ERROR = 9;
}
