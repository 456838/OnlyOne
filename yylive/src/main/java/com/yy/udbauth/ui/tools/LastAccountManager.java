package com.yy.udbauth.ui.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 概述：用于保存最近登录的用户名称
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年7月1日 下午2:25:46
 */
public class LastAccountManager {
	private static final String SP_FILENAME = "98SDF8";
	private static final String KEY_ACCOUNTS = "f823";

	/** 最大记录数字*/
	private static final int MAX_NUMBER = 3;

	/**
	 * 从最近登录用户列表中更新一个用户，如果不存在，那么追加
	 * @param context
	 * @param account
	 */
	public synchronized static void updateLastAccounts(Context context, String account) {
		if (account == null)
			return;

		deleteLastAccount(context, account); //删除重复的

		SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		Set<String> newAccounts = sp.getStringSet(KEY_ACCOUNTS, new HashSet<String>());

		if (newAccounts.size() >= MAX_NUMBER) {
			makeRoomForNew(context);
			newAccounts = sp.getStringSet(KEY_ACCOUNTS, new HashSet<String>());
		}

		//增加新的，格式“时间_账号” 
		newAccounts.add(System.currentTimeMillis() + "_" + account);
		sp.edit().putStringSet(KEY_ACCOUNTS, newAccounts).commit();
	}

	/**
	 * 从最近登录用户列表中删除一个用户
	 * @param context
	 * @param account
	 */
	public synchronized static void deleteLastAccount(Context context, String account) {

		SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		Set<String> temp = sp.getStringSet(KEY_ACCOUNTS, new HashSet<String>());
		List<String> accounts = new ArrayList<String>(temp);

		//格式“时间_账号”
		for (int i = 0; i < accounts.size(); i++) {

			String acc = accounts.get(i);

			if (acc != null && acc.indexOf("_") >= 0) {
				acc = acc.substring(acc.indexOf("_") + 1);

				if (acc.equals(account)) {
					accounts.remove(i);
				}
			}
		}

		sp.edit().putStringSet(KEY_ACCOUNTS, new HashSet<String>(accounts)).commit();
	}

	/**
	 * 获取最近登录的用户列表
	 * @param context	
	 * @return	返回非null列表
	 */
	public synchronized static List<String> getLastAccounts(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		Set<String> temp = sp.getStringSet(KEY_ACCOUNTS, new HashSet<String>());
		List<String> accounts = new ArrayList<String>(temp);

		//根据时间排序
		Collections.sort(accounts, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return -(lhs.compareTo(rhs));
			}
		});

		for (int i = 0; i < accounts.size(); i++) {
			String acc = accounts.get(i);

			//格式“时间_账号”
			if (acc != null && acc.indexOf("_") >= 0) {
				accounts.set(i, acc.substring(acc.indexOf("_") + 1));
			}
		}
		return accounts;
	}

	private static void makeRoomForNew(Context context) {
		SharedPreferences sp = context.getSharedPreferences(SP_FILENAME, Context.MODE_PRIVATE);
		Set<String> temp = sp.getStringSet(KEY_ACCOUNTS, new HashSet<String>());
		List<String> accounts = new ArrayList<String>(temp);

		//根据时间排序
		Collections.sort(accounts, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return -(lhs.compareTo(rhs));
			}
		});

		for (int i = 2; i < accounts.size(); i++) {
			accounts.remove(i);
		}

		sp.edit().putStringSet(KEY_ACCOUNTS, new HashSet<String>(accounts)).commit();
	}
}
