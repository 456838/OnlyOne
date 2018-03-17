/**
 * 此类使用ormlite完成数据库创建等功能
 */

package com.yy.live.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.salton123.util.log.MLog;

import java.sql.SQLException;

abstract public class DbHelper extends OrmLiteSqliteOpenHelper {

	protected final String logTag;
	private String dbName;
	
	public DbHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
		this.dbName = dbName;
		logTag = "DbHelper(" + dbName + ")";
		MLog.info(logTag, "DbHelper constructor");
	}
	
	public String getDbName() {
		return dbName;
	}

    private String dbOpenAccountAction;
    private String dbOpenAccountDbName;
    public void setDBOpenAccount(String action, String dbName){
        this.dbOpenAccountAction = action;
        this.dbOpenAccountDbName = dbName;
    }

    public String getDbOpenAccountAction() {
        return dbOpenAccountAction;
    }

    public String getDbOpenAccountDbName() {
        return dbOpenAccountDbName;
    }

    @Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		
		MLog.info(logTag, "DbHelper onCreate, name = " + dbName);
		try {
			onDbCreate(database, connectionSource);
		} catch (SQLException e) {
			MLog.error(logTag, "DbHelper onCreate error", e);
		}
	}
	
	protected abstract void onDbCreate(SQLiteDatabase database, ConnectionSource connectionSource) throws SQLException;

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		MLog.info(logTag, "DbHelper onUpdate old " + oldVersion + " new " + newVersion);
		
		try {
			onDbUpgrade(database, connectionSource, oldVersion, newVersion);
		} catch (SQLException e) {
			MLog.error(logTag, "DbHelper onUpgrade error", e);
		}
	}
	
	protected abstract void onDbUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion,
			int newVersion)throws SQLException;

}