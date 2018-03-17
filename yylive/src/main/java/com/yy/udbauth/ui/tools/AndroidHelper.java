package com.yy.udbauth.ui.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * 概述：助手
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2015年9月14日 上午11:49:23
 */
public class AndroidHelper {
	/**
	 * 手机号验证，包括对国际手机号码的验证
	 * 
	 * @param  mobile	手机号码	，如果是国际手机号码的话，前面是：“00”开关
	 * @return 验证通过返回true，否则返回false
	 */
	public static boolean isNationalMobile(String mobile) {
		if (mobile == null || !TextUtils.isDigitsOnly(mobile))
			return false;

		if (isChinaMobile(mobile)) {
			return true;
		} else if (mobile.startsWith("0086")) {
			return false;
		} else if (mobile.startsWith("00") && mobile.length() > 2) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是国内手机号码
	 * 
	 * @param  mobile	手机号码，可以是“0086”开头
	 * @return 验证通过返回true，否则返回false
	 */
	public static boolean isChinaMobile(String mobile) {
		if (mobile == null || !TextUtils.isDigitsOnly(mobile))
			return false;

		if (mobile.startsWith("1") && mobile.length() == 11)
			return true;
		else if (mobile.startsWith("0086") && !mobile.startsWith("00861"))
			return false;
		else if (mobile.startsWith("00861") && mobile.length() == 15)
			return true;
		else
			return false;
	}

	/**
	 * 判断当前网络是否可用
	 * @param context	上下文
	 * @return	true表示可用，false表示不可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		try {
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo[] infos = manager.getAllNetworkInfo();
			for (NetworkInfo i : infos) {
				if (i.isConnected()) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/** 从assets目录中读取文件，并转化为字符串 */
	public static String getStringFromAssets(Context context, String filename) {
		BufferedReader br = null;
		InputStream is = null;
		try {
			AssetManager manager = context.getAssets();
			is = manager.open(filename);
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/** 从assets目录中读取文件，并转化为字节数组 */
	public static byte[] getByteArrayFromAssets(Context context, String filename) {
		InputStream is = null;
		try {
			AssetManager manager = context.getAssets();
			is = manager.open(filename);
			byte[] results = new byte[is.available()];
			is.read(results);
			return results;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/** 从res/raw资源目录中读取字符串 */
	public static String getStringFromResRaw(Context context, int rawResId) {
		BufferedReader br = null;
		InputStream is = null;
		try {
			is = context.getResources().openRawResource(rawResId);
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
