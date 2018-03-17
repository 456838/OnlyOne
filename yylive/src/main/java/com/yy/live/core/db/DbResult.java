/**
 * 
 */
package com.yy.live.core.db;


import com.yy.live.core.CoreError;

/**
 * @author daixiang
 *
 */
public class DbResult {

	public enum ResultCode {
		Successful,
		Failed
	}
	
	public ResultCode resultCode;
	public Object resultObject;
	public CoreError error;
	
	public DbResult() {
		resultCode = ResultCode.Successful;
	}
	
	public DbResult(ResultCode resultCode, Object resultObject, CoreError error) {
		
		this.resultCode = resultCode;
		this.resultObject = resultObject;
		this.error = error;
	}
//	public ResultCode getResultCode() {
//		return resultCode;
//	}
//	public void setResultCode(ResultCode resultCode) {
//		this.resultCode = resultCode;
//	}
//	public Object getResultObject() {
//		return resultObject;
//	}
//	public void setResultObject(Object resultObject) {
//		this.resultObject = resultObject;
//	}
//	public DbError getError() {
//		return error;
//	}
//	public void setError(DbError error) {
//		this.error = error;
//	}
//	
	
}
