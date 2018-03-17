package com.yy.udbauth.ui;

/** UI库些常量*/
public class UIConstants {
	/** 表示UI库版本*/
	public static final String UI_VERSION = Version.name.trim();//Version类是Maven构建时自动生成的，可以左右会有空格
	/** 表示日志类型*/
	public static final String LOG_TYPE = "ui_log";
	/** 表示操作成功*/
	public static final String RESULT_SUCCESS = "success";
	/** 表示操作出错*/
	public static final String RESULT_ERROR = "error";
	/** 表示操作取消*/
	public static final String RESULT_CANCEL = "cancel";
}
