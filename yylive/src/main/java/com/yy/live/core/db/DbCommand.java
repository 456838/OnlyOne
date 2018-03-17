/**
 * 
 */
package com.yy.live.core.db;


import com.yy.live.core.CoreError;

/**
 * @author daixiang
 *
 */
public abstract class DbCommand {
	
	protected DbResult result;
	
	protected void realExecute() {
		result = new DbResult();
		try {
			execute();
		} catch (Exception e) {
			result.resultCode = DbResult.ResultCode.Failed;
			result.error = new CoreError(CoreError.Domain.Db, CoreError.Db_Error, e.getMessage(), e);
		}
	}
	
	/**
	 * 一般情况下，子类重载此函数即可，在里面写操作数据库的代码（此函数在数据库线程执行）
	 * 将结果码、结果对象放入result的resultObject对象，并返回result对象
	 * onSucceed或onFail回调会被相应调用（主线程）
	 * 
	 * @return
	 */
	public abstract void execute() throws Exception;
	
	/**
	 * 执行command后成功的回调，子类可以重载已实现自己的处理（主线程）
	 * @param obj
	 */
	public abstract void onSucceed(Object obj);
	
	/**
	 * 执行command后失败的回调，子类可以重载已实现自己的处理（主线程）
	 * @param error
	 */
	public abstract void onFail(CoreError error);

	public DbResult getResult() {
		return result;
	}

//	public void setResult(DbResult result) {
//		this.result = result;
//	}
	
}
