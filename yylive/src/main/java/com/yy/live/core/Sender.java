package com.yy.live.core;

import java.nio.ByteBuffer;

/**
 * sender to send to data to network
 * @author zhongyongsheng
 */
public class Sender extends Header
{

	private static final int HEADER_SIZE = 10;

	private Pack pack = new Pack();
	private int bodySize;

	public Sender(long uri, Marshallable m)
	{
		this(new Uint32(uri), m);
	}

	public Sender(int uri, Marshallable m)
	{
		this(new Uint32(uri), m);
	}

	public Sender(Uint32 uri, Marshallable m)
	{
		setUri(uri);
		pack.getBuffer().position(headerSize());
		m.marshall(pack);
		bodySize = pack.size() - headerSize();
	}

	public void endPack()
	{
		pack.replaceUint32(4, uri);
		pack.replaceUint16(8, resCode);
		pack.replaceUint32(0, new Uint32(headerSize() + bodySize()));
		pack.getBuffer().position(headerSize() + bodySize());
		pack.getBuffer().flip();

	}

	public Pack getPack()
	{
		return pack;
	}

	public int headerSize()
	{
		return HEADER_SIZE;
	}

	public int bodySize()
	{
		return bodySize;
	}

	public ByteBuffer getBuffer()
	{
		return getPack().getBuffer();
	}

    public byte[] getBytes() {
        return getPack().toBytes();
    }
}
