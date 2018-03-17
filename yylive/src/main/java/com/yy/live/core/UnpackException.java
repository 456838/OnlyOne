package com.yy.live.core;


/**
 * unpack exception
 * 
 * @author zhongyongsheng
 */
public class UnpackException extends RuntimeException
{

	private static final long serialVersionUID = -4218633413237051053L;

	public UnpackException()
	{
		super();
	}

	public UnpackException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public UnpackException(String detailMessage)
	{
		super(detailMessage);
	}

	public UnpackException(Throwable throwable)
	{
		super(throwable);
	}


}
