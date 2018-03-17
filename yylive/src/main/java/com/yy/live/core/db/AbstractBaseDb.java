/**
 * 每个db类需要继承此类
 */
package com.yy.live.core.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.salton123.util.log.MLog;
import com.yy.live.core.AbstractBaseCore;

import java.sql.SQLException;

/**
 * @author daixiang
 */
public abstract class AbstractBaseDb extends AbstractBaseCore {

    //private Dao<?, ?> dao;
    //private DbHelper dbHelper;
    protected DbContext dbContext;

    public final boolean isDbContextAttached() {
        return dbContext != null;
    }

    protected AbstractBaseDb() {

        super();
    }

//	protected void setDbHelper(DbHelper helper) {
//		dbHelper = helper;
//	}

    protected void setDbContext(DbContext ctx) {
        dbContext = ctx;
    }

    /**
     * 为保证线程安全，此函数应该只在command的execute函数里调用（在DbThread执行）
     *
     * @param cls
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <D extends Dao<T, ?>, T> D getDao(Class<T> cls) {

        Dao<?, ?> dao = null;
        if (dbContext != null) {
            DbHelper helper = dbContext.getDbHelper();
            if (cls != null && helper != null) {
                try {
                    dao = (D) helper.getDao(cls);
                    //logTag.info("created dao for " + cls.getSimpleName());
                } catch (SQLException e) {
                    MLog.error(this, "cannot getDao for class " + cls.getName(), e);
                }
            }
        }
        return (D) dao;
    }

    protected <D extends Dao<T, ?>, T> D getDaoWithConfig(DatabaseTableConfig<T> config) {
        Dao<?, ?> dao = null;
        if (dbContext != null) {
            DbHelper helper = dbContext.getDbHelper();
            if (config != null && helper != null) {
                try {
                    dao = (Dao<?, ?>) DaoManager.createDao(helper.getConnectionSource(), config);
                    //logTag.info("created dao for " + cls.getSimpleName());
                } catch (SQLException e) {
                    MLog.error(this, "cannot getDao for class with config" + config);
                }
            }
        }
        return (D) dao;
    }

    protected SQLiteDatabase getSqliteDb() {
        return dbContext.getDbHelper().getWritableDatabase();
    }

    protected void sendCommand(DbCommand cmd) {
        //DbManager.sendCommand(cmd);
        if (dbContext != null) {
            dbContext.sendCommand(cmd);
        }
    }

    protected <D extends Dao<T, ?>, T> D getDaoNoCacheWithConfig(DatabaseTableConfig<T> config) {
        D dao = getDaoWithConfig(config);
        if (dbContext != null && dbContext.getDbHelper() != null)
            DaoManager.unregisterDao(dbContext.getDbHelper().getConnectionSource(), dao);
        return dao;
    }
}
