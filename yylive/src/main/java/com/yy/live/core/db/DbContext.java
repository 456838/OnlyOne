/**
 * 
 */
package com.yy.live.core.db;

/**
 * @author daixiang
 *
 */
public interface DbContext {

	public void createDbHelper(String dbName);
	
	public DbHelper getDbHelper();
	
	public void closeDbHelper();
	
	/**
	 * 将命令发送到context里去执行
	 * @param cmd
	 */
	public void sendCommand(DbCommand cmd);
	
	/**
	 * 使用context之前必须先调用open
	 */
	public void open();
}
