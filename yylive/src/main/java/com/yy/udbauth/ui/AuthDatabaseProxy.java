package com.yy.udbauth.ui;

import java.util.List;

import android.content.Context;

import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.ui.info.AccountInfo;
import com.yy.udbauth.ui.tools.AccountSQLiteHelper;

/**
 * 概述：数据库操作代理
 * 
 * @version 1.0
 * @author weitianpeng@yy.com
 * @time 2016年4月11日 下午5:40:35
 */
public class AuthDatabaseProxy {
	AccountSQLiteHelper mSqliteHelper;

	public AuthDatabaseProxy(Context context) {
		mSqliteHelper = new AccountSQLiteHelper(context);
	}

	/** 从数据库中获取当前正在使用的账户信息*/
	public AccountInfo getActivitedAccount() {
		return mSqliteHelper.getActivitedAccount();
	}

	/** 根据uid，设置数据库中某个对应的账户为激活状态（即正在使用）*/
	public boolean setActiviedAccount(String uid) {
		return mSqliteHelper.setActiviedAccount(uid);
	}

	/** 增加或替换数据库中的一个账户信息*/
	public boolean addOrReplace(AuthEvent.LoginEvent et) {
		return mSqliteHelper.addOrReplace(et);
	}

	/** 增加或替换数据库的一个账户信息*/
	public boolean addOrReplace(String uid, String yyid, String passport, String credit, String token, String webtoken,
			String yycookies) {
		return mSqliteHelper.addOrReplace(uid, yyid, passport, credit, token, webtoken, yycookies);
	}

	/**
	 * 根据UID查询一条账户数据
	 * @param uid	需要查询的UID，不能为空
	 * @return	返回对应的账户信息，失败则返回null
	 */
	public AccountInfo queryAccountByUid(String uid) {
		return mSqliteHelper.query(uid);
	}

	/**
	 * 根据Passport查询一条账户信息
	 * @param passport	需要查询的UID，不能为空
	 * @return	返回对应的账户信息，失败则返回null
	 */
	public AccountInfo queryAccountByPassport(String passport) {
		return mSqliteHelper.queryByPassport(passport);
	}

	/** 
	 * 从数据库读取所有账户信息
	 * @return 返回所有账户信息，失败则返回空数组
	 */
	public List<AccountInfo> queryAllAccounts() {
		return mSqliteHelper.queryAll();
	}

	/** 
	 * 从数据库中删除一个账户信息
	 * @param uid	需要删除的账户的UID，不能为空
	 * @return	返回是否删除成功
	 */
	public boolean deleteAccount(String uid) {
		return mSqliteHelper.delete(uid);
	}

	/** 
	 * 从数据库中清空所有账户信息
	 * @return	返回是否成功
	 */
	public boolean deleteAllAccounts() {
		return mSqliteHelper.deleteAll();
	}

	/**
	 * 更新账户信息到数据库
	 * @param uid	需要更新的账户
	 * @param credit	需要更新的credit
	 */
	public boolean update(String uid, String credit) {
		return mSqliteHelper.update(uid, credit);
	}

}
