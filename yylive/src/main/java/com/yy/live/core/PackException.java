package com.yy.live.core;


/**
 * pack exception
 * 
 * @author zhongyongsheng
 */
public class PackException extends RuntimeException
{

	private static final long serialVersionUID = -4218633413237051053L;

	public PackException()
	{
		super();
	}

	public PackException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public PackException(String detailMessage)
	{
		super(detailMessage);
	}

	public PackException(Throwable throwable)
	{
		super(throwable);
	}


}
