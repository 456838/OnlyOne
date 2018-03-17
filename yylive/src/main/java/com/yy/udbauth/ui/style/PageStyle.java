package com.yy.udbauth.ui.style;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import com.yy.live.R;

import java.io.Serializable;


/**
 * 概述：基本页面样式表
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月25日 上午9:27:41
 */
public class PageStyle implements Serializable {

	public static final int INVALID_RES_ID = -1;
	private static final long serialVersionUID = -8039290750189687491L;

	/** 页面背景图片，优先级排第一*/
	public Bitmap backgroundBitmap;
	/** 页面背景资源，优先级排第二*/
	public int backgroundResId;
	/** 页面背景颜色，优先级排第三*/
	public int backgroundColor;

	/** 标题栏颜色*/
	public int titlebarColor;
	/** 标题栏文字颜色*/
	public int titlebarTextColor;
	/** 是否显示标题返回按钮*/
	public boolean showBackButton = true;
	/** 是否显示标题栏*/
	public boolean showTitlebar = true;

	/** 文字默认颜色*/
	public int textColor;
	/** 文字突出显示的颜色*/
	public int textStrikingColor;

	/** 按钮背景图片，优先级排第一*/
	public Bitmap buttonBackground;
	/** 按钮背景资源，优先级排第二*/
	public int buttonBackgroundResId = INVALID_RES_ID;
	/** 按钮背景颜色，优先级排第三*/
	/** 按钮默认颜色*/
	public int buttonColor;
	/** 按钮不可用的颜色*/
	public int buttonDisabledColor;
	/** 按钮被按下的颜色*/
	public int buttonPressedColor;

	/** 按钮默认字体颜色*/
	public int buttonTextColor;
	/** 按钮不可用时字体颜色*/
	public int buttonTextDiabledColor;
	/** 按钮被按下时字体颜色*/
	public int buttonTextPressedColor;

	public PageStyle(Context context) {

		context.getApplicationContext();
		Resources res = context.getResources();

		backgroundResId = 0;
		backgroundColor = res.getColor(R.color.UA_Activity_Background);

		titlebarColor = res.getColor(R.color.UA_TitleBar_Background);
		titlebarTextColor = res.getColor(R.color.UA_TitleBar_Title_TextColor);

		textColor = res.getColor(R.color.UA_TextView_Normal);
		textStrikingColor = res.getColor(R.color.UA_TextView_StrikingColor);

		buttonColor = res.getColor(R.color.UA_Button_Flat_Theme_Nomal);
		buttonDisabledColor = res.getColor(R.color.UA_Button_Flat_Theme_Disabled);
		buttonPressedColor = res.getColor(R.color.UA_Button_Flat_Theme_Pressed);

		buttonTextColor = res.getColor(R.color.UA_Button_Flat_Text_Normal);
		buttonTextDiabledColor = res.getColor(R.color.UA_Button_Flat_Text_Disabled);
		buttonTextPressedColor = res.getColor(R.color.UA_Button_Flat_Text_Activited);
	}

	/** 获取适用于按钮的Drawable*/
	@SuppressWarnings("deprecation")
	public Drawable getButtonDrawable(Context context) {
		if (buttonBackground != null) {
			return new BitmapDrawable(buttonBackground);
		} else if (buttonBackgroundResId != INVALID_RES_ID && context != null) {
			return context.getResources().getDrawable(buttonBackgroundResId);
		} else {
			return createButtonDrawable(buttonColor, buttonPressedColor, buttonPressedColor, buttonDisabledColor);
		}
	}

	/** 获取适用于按钮文字的Drawable*/
	public ColorStateList getButtonTextDrawable() {
		return createColorStateList(buttonTextColor, buttonTextPressedColor, buttonTextPressedColor,
				buttonTextDiabledColor);
	}

	/** 创建按钮样式*/
	private StateListDrawable createButtonDrawable(int normal, int pressed, int focused, int disabled) {
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_pressed }, new ColorDrawable(
				pressed));
		drawable.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, new ColorDrawable(
				focused));
		drawable.addState(new int[] { android.R.attr.state_enabled }, new ColorDrawable(normal));
		drawable.addState(new int[0], new ColorDrawable(disabled));
		return drawable;
	}

	/** 创建文字颜色选择器*/
	private ColorStateList createColorStateList(int normal, int pressed, int focused, int diabled) {
		int[] colors = new int[] { pressed, focused, normal, diabled };
		int[][] states = new int[4][];
		states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
		states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
		states[2] = new int[] { android.R.attr.state_enabled };
		states[3] = new int[] {};
		return new ColorStateList(states, colors);
	}

}
