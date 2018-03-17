package com.yy.udbauth.ui.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 概述：这是一个会倒计时的按钮
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年1月28日 下午4:16:24
 */
public class UdbButton extends Button implements Runnable {

	Handler mHandler = new Handler();
	
	String enableString;
	String disableStringWithFormat;
	
	boolean isCancelCountdown = false;
	long startTime;
	long endTime;

	public UdbButton(Context context) {
		super(context);
	}

	public UdbButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UdbButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onDetachedFromWindow() {
		cancelCountdown();
		super.onDetachedFromWindow();
	}

	/**
	 * 取消倒计时，恢复原来的状态
	 */
	public void cancelCountdown() {
		setEnabled(true);
		isCancelCountdown = true;
	}

	/**
	 * 启动按钮倒计时，每秒一次，达到指定时间后，停止倒计时
	 * 
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param enableString 按钮可用时显示的文字
	 * @param disableStringWithFormat 按钮不可用时（倒计时）显示的内容，如：“重新获取(%d)”
	 */
	public boolean setupCountdown(final long startTime, final long endTime, final String enableString,
			final String disableStringWithFormat) {

		if (endTime < startTime)
			return false;

		this.startTime = startTime;
		this.endTime = endTime;
		this.enableString = enableString;
		this.disableStringWithFormat = disableStringWithFormat;
		isCancelCountdown = false;
		mHandler.removeCallbacks(this);
		mHandler.post(this);

		return true;
	}

	@Override
	public void run() {

		if (isCancelCountdown) {
			//倒计时结束
			setEnabled(true);
			setText(enableString);
			return;
		}

		long nowTime = System.currentTimeMillis();

		if (nowTime < endTime) {
			//倒计时进行中
			String msg = disableStringWithFormat;

			if (msg == null)
				msg = "%d";
			else if (!msg.contains("%d")) {
				msg = msg + "%d";
			}

			msg = String.format(msg, (endTime - nowTime) / 1000);

			setEnabled(false);
			setText(msg);
			mHandler.postDelayed(this, 1000);
		} else {
			//倒计时结束
			setEnabled(true);
			setText(enableString);
		}
	}
}
