package com.yy.live.core;

/**
 * 有符号64位整形
 * @author zhongyongsheng
 */
public class Int64 extends Number implements Comparable<Int64>
{

	private static final long serialVersionUID = 2512773791709683899L;
	private long v;

	public Int64(int i)
	{
//		if (i < 0)
//		{
//			String s = Integer.toBinaryString(i);
//			v = Long.valueOf(s, 2);
//		}
//		else
//		{
//			v = i;
//		}
		v = 0xFFFFFFFFL & i;
	}

	public Int64(long l)
	{
		v = l;
	}

	public Int64(String l)
	{
		v = Long.valueOf(l);
	}

	public static Int64 toUInt(int i)
	{
		return new Int64(i);
	}
	
	public static Int64 toUInt(long i)
	{
		return new Int64(i);
	}

	@Override
	public String toString()
	{
		return Long.toString(v);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (v ^ (v >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Int64 other = (Int64) obj;
		if (v != other.v)
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Int64 o) {
		return (int)(this.v - o.longValue());
	}

	@Override
	public int intValue() {
		return  (int)v;
	}

	@Override
	public long longValue() {
		return v;
	}

	@Override
	public float floatValue() {
		return (float)v;
	}

	@Override
	public double doubleValue() {
		return (double)v;
	}
}
