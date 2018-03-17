package com.yy.udbauth.ui.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.ui.info.AccountInfo;

/**
 * 概述：账户数据库助手 ，用于保存、查询、修改、删除已登录的用户信息。仅供参考<br/>
 * 
 * @version 1.0
 * @version 2.0 为了提高安全性，这里增加了数据加密，同时表结构也有些小改，APP更新后原来的数据会被删除
 * @author weitianpeng@yy.com
 * @time 2015年9月22日 上午11:37:52
 */
public class AccountSQLiteHelper extends SQLiteOpenHelper {

	/** 数据库名称*/
	static String DB_NAME = "sdk2acc.db";
	/** 数据库版本号*/
	static int DB_VERSION = 2;
	/** 数据库名称*/
	static String TABLE_NAME = "sdk2acc";
	/** 加密密钥*/
	static String SEED = "udbsdkn83ir6";

	/** Boolean values are stored as integers 0 (false) and 1 (true).*/
	private static int FALSE = 0;
	private static int TRUE = 1;

	/* 字段*/
	private static String F_UID = "uid";
	private static String F_YYID = "yyi";
	private static String F_PASSPORT = "pas";
	private static String F_CREDIT = "cre";
	private static String F_TOKEN = "tok";
	private static String F_WEBTOKEN = "wetok";
	private static String F_YYCOOKIES = "coo";
	private static String F_UPDATE_TIME = "date";
	/**  0为false,非0为true*/
	private static String F_IS_USING = "isu";

	public AccountSQLiteHelper(Context context) {
		this(context, DB_NAME, null, DB_VERSION);
	}

	private AccountSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (newVersion == 2 && oldVersion == 1) {
			//2.0 为了提高安全性，这里增加了数据加密，同时表结构也有些小改，APP更新后原来的数据会被删除
			dropTable(db);
			createTable(db);
		}
	}

	/** 创建数据表*/
	private void createTable(SQLiteDatabase db) {

		try {
			StringBuilder sb = new StringBuilder();
			sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
			sb.append(F_UID).append(" TEXT, ");
			sb.append(F_YYID).append(" TEXT, ");
			sb.append(F_PASSPORT).append(" TEXT, ");
			sb.append(F_CREDIT).append(" TEXT, ");
			sb.append(F_TOKEN).append(" TEXT, ");
			sb.append(F_WEBTOKEN).append(" TEXT, ");
			sb.append(F_YYCOOKIES).append(" TEXT, ");
			sb.append(F_IS_USING).append(" INTEGER, ");
			sb.append(F_UPDATE_TIME).append(" TEXT ) ");

			db.execSQL(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 删除数据表*/
	private void dropTable(SQLiteDatabase db) {
		try {
			String sql = String.format("DROP TABLE %s ;", TABLE_NAME);
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/** 将登录结果保存到数据库
	 * 
	 * @param et	登录结果，其中uid不能为空
	 * @return	返回是否保存成功
	 */
	public boolean addOrReplace(AuthEvent.LoginEvent et) {
		if (TextUtils.isEmpty(et.uid) || TextUtils.isEmpty(et.yyid) || TextUtils.isEmpty(et.passport))
			return false;
		return addOrReplace(et.uid, et.yyid, et.passport, et.credit, null, null, null);
	}

	/** 将信息保存到数据库
	 * @param uid 该字段不能为空
	 * 
	 * @return	返回是否保存成功
	 */
	public boolean addOrReplace(String uid, String yyid, String passport, String credit, String token, String webtoken,
			String yycookies) {

		if (TextUtils.isEmpty(uid))
			return false;

		try {

			if (exist(uid))
				delete(uid);

			SQLiteDatabase db = getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(F_UID, encrypt(uid));
			values.put(F_YYID, encrypt(yyid));
			values.put(F_PASSPORT, encrypt(passport));
			values.put(F_CREDIT, encrypt(credit));
			values.put(F_YYCOOKIES, encrypt(token));
			values.put(F_TOKEN, encrypt(yycookies));
			values.put(F_WEBTOKEN, encrypt(webtoken));
			values.put(F_IS_USING, FALSE);
			values.put(F_UPDATE_TIME, System.currentTimeMillis());

			boolean success = db.insert(TABLE_NAME, null, values) >= 0;
			setActiviedAccount(uid);
			return success;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 更新账户信息到数据库
	 * @param uid	需要更新的账户
	 * @param credit	需要更新的credit
	 * @return
	 */
	public boolean update(String uid, String credit) {

		if (TextUtils.isEmpty(uid))
			return false;

		try {
			SQLiteDatabase db = getWritableDatabase();
			String creditNew = (credit == null) ? ("null") : ("'" + encrypt(credit) + "'");
			String sql = String.format("UPDATE %s set %s=%s WHERE %s='%s'", TABLE_NAME, F_CREDIT, creditNew, F_UID,
					encrypt(uid));
			db.execSQL(sql);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 设置正在使用的账号
	 * @return	返回是否设置成功
	 */
	public boolean setActiviedAccount(String uid) {

		if (TextUtils.isEmpty(uid))
			return false;

		try {
			SQLiteDatabase db = getWritableDatabase();
			String sql = String.format("UPDATE %s set %s=(%s='%s')", TABLE_NAME, F_IS_USING, F_UID, encrypt(uid));
			db.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取正在使用的账号信息
	 * @return	返回对应的账户信息，失败则返回null
	 */
	public AccountInfo getActivitedAccount() {

		Cursor cursor = null;
		try {
			AccountInfo info = new AccountInfo();
			SQLiteDatabase db = getReadableDatabase();
			String sql = String.format(Locale.getDefault(), "SELECT * FROM %s WHERE %s=%d", TABLE_NAME, F_IS_USING,
					TRUE);
			cursor = db.rawQuery(sql, new String[0]);

			if (!cursor.moveToFirst())
				return null;

			info.uid = decrypt(cursor.getString(cursor.getColumnIndex(F_UID)));
			info.yyid = decrypt(cursor.getString(cursor.getColumnIndex(F_YYID)));
			info.passport = decrypt(cursor.getString(cursor.getColumnIndex(F_PASSPORT)));
			info.credit = decrypt(cursor.getString(cursor.getColumnIndex(F_CREDIT)));
			info.tooken = decrypt(cursor.getString(cursor.getColumnIndex(F_TOKEN)));
			info.webtoken = decrypt(cursor.getString(cursor.getColumnIndex(F_WEBTOKEN)));
			info.yycookies = decrypt(cursor.getString(cursor.getColumnIndex(F_YYCOOKIES)));
			return info;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	/**
	 * 根据UID查询一条账户数据
	 * @param uid	需要查询的UID，不能为空
	 * @return	返回对应的账户信息，失败则返回null
	 */
	public AccountInfo query(String uid) {

		if (TextUtils.isEmpty(uid))
			return null;

		Cursor cursor = null;
		try {
			AccountInfo info = new AccountInfo();
			SQLiteDatabase db = getReadableDatabase();
			String sql = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, F_UID);
			cursor = db.rawQuery(sql, new String[] { encrypt(uid) });

			while (cursor.moveToFirst()) {
				info.uid = decrypt(cursor.getString(cursor.getColumnIndex(F_UID)));
				info.yyid = decrypt(cursor.getString(cursor.getColumnIndex(F_YYID)));
				info.passport = decrypt(cursor.getString(cursor.getColumnIndex(F_PASSPORT)));
				info.credit = decrypt(cursor.getString(cursor.getColumnIndex(F_CREDIT)));
				info.tooken = decrypt(cursor.getString(cursor.getColumnIndex(F_TOKEN)));
				info.webtoken = decrypt(cursor.getString(cursor.getColumnIndex(F_WEBTOKEN)));
				info.yycookies = decrypt(cursor.getString(cursor.getColumnIndex(F_YYCOOKIES)));
				break;
			}

			return info;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	/**
	 * 根据Passport查询一条账户数据
	 * @param passport	需要查询的UID，不能为空
	 * @return	返回对应的账户信息，失败则返回null
	 */
	public AccountInfo queryByPassport(String passport) {

		if (TextUtils.isEmpty(passport))
			return null;

		Cursor cursor = null;
		try {
			AccountInfo info = new AccountInfo();
			SQLiteDatabase db = getReadableDatabase();
			String sql = String.format("SELECT * FROM %s WHERE %s=?", TABLE_NAME, F_PASSPORT);
			cursor = db.rawQuery(sql, new String[] { encrypt(passport) });

			while (cursor.moveToFirst()) {
				info.uid = decrypt(cursor.getString(cursor.getColumnIndex(F_UID)));
				info.yyid = decrypt(cursor.getString(cursor.getColumnIndex(F_YYID)));
				info.passport = decrypt(cursor.getString(cursor.getColumnIndex(F_PASSPORT)));
				info.credit = decrypt(cursor.getString(cursor.getColumnIndex(F_CREDIT)));
				info.tooken = decrypt(cursor.getString(cursor.getColumnIndex(F_TOKEN)));
				info.webtoken = decrypt(cursor.getString(cursor.getColumnIndex(F_WEBTOKEN)));
				info.yycookies = decrypt(cursor.getString(cursor.getColumnIndex(F_YYCOOKIES)));
				break;
			}

			return info;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	/**
	 * 查询所有账户数据
	 * @return	返回所有账户信息，失败则返回空数组
	 */
	public List<AccountInfo> queryAll() {

		List<AccountInfo> infos = new ArrayList<AccountInfo>();

		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String sql = String.format("SELECT * FROM %s", TABLE_NAME);
			cursor = db.rawQuery(sql, new String[0]);

			while (cursor.moveToNext()) {
				AccountInfo info = new AccountInfo();
				info.uid = decrypt(cursor.getString(cursor.getColumnIndex(F_UID)));
				info.yyid = decrypt(cursor.getString(cursor.getColumnIndex(F_YYID)));
				info.passport = decrypt(cursor.getString(cursor.getColumnIndex(F_PASSPORT)));
				info.credit = decrypt(cursor.getString(cursor.getColumnIndex(F_CREDIT)));
				info.tooken = decrypt(cursor.getString(cursor.getColumnIndex(F_TOKEN)));
				info.webtoken = decrypt(cursor.getString(cursor.getColumnIndex(F_WEBTOKEN)));
				info.yycookies = decrypt(cursor.getString(cursor.getColumnIndex(F_YYCOOKIES)));
				infos.add(info);
			}
			return infos;
		} catch (Exception e) {
			e.printStackTrace();
			return infos;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	/**
	 * 根据uid判断是否存在该账户信息
	 * @param uid	需要判断是否存在的账户的uid
	 * @return	返回是否存在
	 */
	public boolean exist(String uid) {

		if (TextUtils.isEmpty(uid))
			return false;

		Cursor cursor = null;
		try {
			SQLiteDatabase db = getReadableDatabase();
			String sql = String.format("SELECT uid FROM %s WHERE %s='%s'", TABLE_NAME, F_UID, encrypt(uid));
			cursor = db.rawQuery(sql, new String[0]);
			return cursor.moveToFirst();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
	}

	/**
	 * 根据uid删除账户数据
	 * @param uid	需要删除的账户的UID，不能为空
	 * @return	返回是否删除成功
	 */
	public boolean delete(String uid) {

		if (TextUtils.isEmpty(uid))
			return false;

		try {
			getWritableDatabase().delete(TABLE_NAME, String.format("%s=?", F_UID), new String[] { encrypt(uid) });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据uid删除账户的凭证
	 * @param uid	需要删除的账户的UID，不能为空
	 * @return	返回是否删除成功
	 */
	public boolean deleteCredit(String uid) {

		if (TextUtils.isEmpty(uid))
			return false;

		try {
			getWritableDatabase().delete(TABLE_NAME, String.format("%s=?", F_UID), new String[] { encrypt(uid) });
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除所有账户数据
	 * @return	返回是否删除成功
	 */
	public boolean deleteAll() {
		try {
			getWritableDatabase().delete(TABLE_NAME, null, new String[] {});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 加密 
	 * @return 返回密码后的密文，如果加密失败，直接返回原来的参数
	 */
	private String encrypt(String plainText) {
		return EncryptUtils.encrypt(SEED, plainText);
	}

	/** 解密 */
	private String decrypt(String cipherText) {
		return EncryptUtils.decrypt(SEED, cipherText);
	}
}
