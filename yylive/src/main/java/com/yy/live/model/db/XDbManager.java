package com.yy.live.model.db;

import com.yy.live.model.YYLiveParams;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/14 19:29
 * Time: 19:29
 * Description:
 */
public class XDbManager {
    private DbManager mDbManager;

    public XDbManager() {
        mDbManager = x.getDb(getDefaultDbConfig());
    }

    public void init() {
        if (mDbManager == null) {
            mDbManager = x.getDb(getDefaultDbConfig());
        }
    }

    public DbManager.DaoConfig getDefaultDbConfig() {
        return new DbManager.DaoConfig()
                .setDbName(YYLiveParams.DEFAULT_DB)
                .setAllowTransaction(true)
                // 不设置dbDir时, 默认存储在app的私有目录.
//                .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(2)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                });
    }

}
