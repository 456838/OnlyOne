/**
 * db线程，所有数据库操作都被发送到这个线程执行，包括创建，打开等
 * 因此所有数据库操作结果都是异步在主线程回调
 */
package com.yy.live.core.db;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.salton123.util.asynctask.SafeDispatchHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author daixiang
 */
public abstract class DbThread extends Thread implements DbContext {
    public static final String TAG ="DbThread";
    private static final int DbCommandMsg = 1;
    private static final int DbCommandResultMsg = 2;
    private static final int CreateDbHelperMsg = 3;
    private static final int CloseDbHelperMsg = 4;

    protected DbHelper dbHelper;
    //	private Context context;
    protected String dbName;
    private CommandHandler commandHandler;    // 用于主线程往数据库线程发消息
    private Handler resultHandler;            // 用于数据库线程往主线程发消息
    private boolean isReady = false;
    private List<DbCommand> cachedCommands;   // 在线程未准备好以前，缓存收到的命令，线程就绪后会执行

    public DbThread(String name, String dbName) {
        super();
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        setName("db" + "-" + name + "-" + dbName);
//		context = ctx;
        this.dbName = dbName;

        cachedCommands = Collections.synchronizedList(new ArrayList<DbCommand>());

        resultHandler = new SafeDispatchHandler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DbCommandResultMsg:
                        DbCommand cmd = (DbCommand) msg.obj;
                        if (cmd != null) {

                            DbResult dbResult = cmd.getResult();
                            switch (dbResult.resultCode) {
                                case Successful:
                                    cmd.onSucceed(dbResult.resultObject);
                                    break;
                                case Failed:
                                    cmd.onFail(dbResult.error);
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void sendCommand(DbCommand cmd) {
        if (cmd != null) {
            if (!isReady) {
                cachedCommands.add(cmd);
            } else {
                Message msg = commandHandler.obtainMessage(DbCommandMsg, cmd);
                if (msg != null) {
                    commandHandler.sendMessage(msg);
                }
            }
        }
    }

    // 必须在本线程执行
    private void processCachedCommands() {
        if (dbHelper != null) {
            List<DbCommand> commands = new ArrayList<DbCommand>(cachedCommands);
            if (commands.size() > 0) {
                Log.i(TAG,"handle cached commands: " + commands.size());
                for (DbCommand cmd : commands) {
                    commandHandler.processCommand(cmd);
                }
            }
            commands.clear();
            commands = null;
        }
        cachedCommands.clear();
    }

    public void run() {

        Looper.prepare();
        // 调用这个会创建和打开数据库
        if (dbName != null) {
            createDbHelper(dbName);
        }

        commandHandler = new CommandHandler();
        isReady = true;
        Log.i(TAG,"DbThread ready");

        // 处理之前缓存的命令
        processCachedCommands();

        Looper.loop();
    }

//	protected void createDbHelper() {
//		if (dbHelper == null) {
//			dbHelper = new DbHelper(context, dbName);
//		}
//	}

    public abstract void createDbHelper(String dbName);

    protected void threadCreateDbHelper(String dbName) {
        Message msg = commandHandler.obtainMessage(CreateDbHelperMsg, dbName);
        if (msg != null) {
            commandHandler.sendMessage(msg);
        }
    }

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public void closeDbHelper() {

        if (Looper.myLooper().getThread() != this) {
            Message msg = commandHandler.obtainMessage(CloseDbHelperMsg, dbName);
            if (msg != null) {
                commandHandler.sendMessage(msg);
            }
            return;
        }

        if (dbHelper != null) {
            Log.i(TAG,"close dbHelper: " + dbHelper.getDbName());
            dbHelper.close();
            dbHelper = null;
        }
    }

    public void open() {
        start();
    }

    public boolean isOpen(){
        return dbHelper != null && dbHelper.isOpen();
    }

    @SuppressLint("HandlerLeak")
    private class CommandHandler extends SafeDispatchHandler {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case DbCommandMsg:
                    DbCommand cmd = (DbCommand) msg.obj;
                    processCommand(cmd);
                    break;
                case CreateDbHelperMsg:
                    dbName = (String) msg.obj;
                    createDbHelper(dbName);
                    break;
                case CloseDbHelperMsg:
                    closeDbHelper();
                    break;
                default:
                    break;
            }
        }

        public void processCommand(DbCommand cmd) {
            if (cmd != null) {
//				DbResult dbResult = cmd.execute();
                cmd.realExecute();
//				if (dbResult != null) {
//					cmd.setResult(dbResult);

                if (resultHandler != null) {
                    Message newMsg = resultHandler.obtainMessage(DbCommandResultMsg, cmd);
                    resultHandler.sendMessage(newMsg);
                }
//				}
            }
        }
    }
}
