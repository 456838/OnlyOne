package com.yy.live.core;


/**
 *
 * @author zhongyongsheng
 * @author <a href="mailto:kuanglingxuan@yy.com">匡凌轩</a> 2014-6-10
 */
public class Uint8 extends Number implements Comparable<Uint8>
{

	private static final long serialVersionUID = -2010873547061112692L;
	
	private long v;

	public Uint8(int i)
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

	public Uint8(String l)
	{
		v = Long.valueOf(l);
	}

	public static Uint8 toUInt(int i)
	{
		return new Uint8(i);
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
		Uint8 other = (Uint8) obj;
		if (v != other.v)
			return false;
		return true;
	}

	@Override
	public int compareTo(Uint8 o) {
		return (int)(this.v - o.intValue());
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
	
	@Override
	public short shortValue() {
        return (short)v;
    }
}
