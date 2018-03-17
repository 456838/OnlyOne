package com.yy.live.core;


/**
 *
 * @author zhongyongsheng
 */
public interface ByteStringable
{
	/**
	 * encode this object to ByteString
	 * @param bs target ByteString
	 */
	public void toString(ByteString bs);
	
	/**
	 * decode ByteString to this object
	 * @param bs target ByteString
	 */
	public void unString(ByteString bs);
}
