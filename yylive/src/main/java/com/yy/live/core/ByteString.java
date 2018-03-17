package com.yy.live.core;

/**
 * String with bytes
 * 
 * @author zhongyongsheng
 * 
 */
public class ByteString
{
	private byte[] bytes;

	public ByteString()
	{

	}

	public ByteString(byte[] b)
	{
		this.bytes = b;
	}

	public void setBytes(byte[] b)
	{
		this.bytes = b;
	}

	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public void merge(byte[] newByte)
	{
		this.bytes = byteMerge(this.bytes, newByte);
	}

	/**
	 * 合并两个byte数组
	 * @param byte1
	 * @param byte2
	 * @return
	 */
	public static byte[] byteMerge(byte[] byte1, byte[] byte2)
	{
		byte[] byte3 = new byte[byte1.length + byte2.length];
		System.arraycopy(byte1, 0, byte3, 0, byte1.length);
		System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
		return byte3;
	}
}
