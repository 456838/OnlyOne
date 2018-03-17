package com.yy.live.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * unmarshal collection or map util
 * 
 * @author zhongyongsheng
 */
@SuppressWarnings("unchecked")
public class UnmarshalContainer
{

	// unmarshal list, set below

	/**
	 * unmarshal list<uint8>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColUint8(Unpack up, Collection<Uint8> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popUint8());
		}
	}
	/**
	 * unmarshal list<uint16>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColUint16(Unpack up, Collection<Uint16> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popUint16());
		}
	}

	/**
	 * unmarshal list<uint32>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColUint32(Unpack up, Collection<Uint32> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popUint32());
		}
	}

	/**
	 * unmarshal list<uint64>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColUint64(Unpack up, Collection<Uint64> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popUint64());
		}
	}

	/**
	 * unmarshal list<string>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColString(Unpack up, Collection<String> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popString());
		}
	}

	/**
	 * unmarshal list<string>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColBytes(Unpack up, Collection<byte[]> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			col.add(up.popBytes());
		}
	}

	/**
	 * unmarshal list<XXXClass>
	 * @param up
	 * @param col
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void unmarshalColMarshallable(Unpack up, Collection col, Class<? extends Marshallable> type)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			try
			{
				Marshallable m = type.newInstance();
				m.unmarshall(up);
				col.add(m);
			} catch (IllegalAccessException e)
			{
				throw new UnpackException(e);
			} catch (InstantiationException e)
			{
				throw new UnpackException(e);
			}
		}
	}

	/**
	 * unmarshal list<map<string,string>>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColMapStringString(Unpack up, Collection<Map<String, String>> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			unmarshalMapStringString(up, map);
			col.add(map);
		}
	}

	/**
	 * unmarshal list<map<string,string>>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColMapStringBytes(Unpack up, Collection<Map<String, byte[]>> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Map<String, byte[]> map = new HashMap<String, byte[]>();
			unmarshalMapStringBytes(up, map);
			col.add(map);
		}
	}
	/**
	 * unmarshal list<map<uint32,string>>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColMapUint32String(Unpack up, Collection<Map<Uint32, String>> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Map<Uint32, String> map = new HashMap<Uint32, String>();
			unmarshalMapUint32String(up, map);
			col.add(map);
		}
	}

	/**
	 * unmarshal list<map<uint32,uint32>>
	 * @param up
	 * @param col
	 */
    public static void unmarshalColMapUint32Uint32(Unpack up, Collection<Map<Uint32, Uint32>> col)
    {
        Uint32 size = up.popUint32();
        for (int i = 0; i < size.intValue(); i++)
        {
            Map<Uint32, Uint32> map = new HashMap<Uint32, Uint32>();
            unmarshalMapUint32Uint32(up, map);
            col.add(map);
        }
    }

	/**
	 * unmarshal list<map<uint32, map<string, string>>>
	 * @param up
	 * @param col
	 */
	public static void unmarshalColMapUint32MapStringString(Unpack up, Collection<Map<Uint32, Map<String, String>>> col)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Map<Uint32, Map<String, String>> map = new HashMap<Uint32, Map<String, String>>();
			unmarshalMapUint32MapStringString(up, map);
			col.add(map);
		}
	}

	// unmarshal map below

	/**
	 * unmarshal map<uint8,uint32>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint8Uint32(Unpack up, Map<Uint8, Uint32> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint8(), up.popUint32());
		}
	}

	/**
	 * unmarshal map<uint16,uint32>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint16Uint32(Unpack up, Map<Uint16, Uint32> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint16(), up.popUint32());
		}
	}

	/**
	 * unmarshal map<uint16,byte[]>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint16Bytes(Unpack up, Map<Uint16, byte[]> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint16(), up.popBytes());
		}
	}

	/**
	 * unmarshal map<uint16,string>
	 * @param up
	 * @param map
	 */
  	public static void unmarshalMapUint16String(Unpack up, Map<Uint16, String> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint16(), up.popString());
		}
	}

	/**
	 * unmarshal map<uint32,uint32>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint32Uint32(Unpack up, Map<Uint32, Uint32> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint32(), up.popUint32());
		}
	}

	/**
	 * unmarshal map<uint32,string>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint32String(Unpack up, Map<Uint32, String> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint32(), up.popString());
		}
	}

	/**
	 * unmarshal map<uint32,Boolean>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint32Boolean(Unpack up, Map<Uint32, Boolean> map) {
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint32(), up.popBoolean());
		}
	}

	/**
	 * unmarshal map<uint32,string>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint32Bytes(Unpack up, Map<Uint32, byte[]> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popUint32(), up.popBytes());
		}
	}

	/**
	 * unmarshal map<string,string>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapStringString(Unpack up, Map<String, String> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popString(), up.popString());
		}
	}

	/**
	 * unmarshal map<string,string>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapBytesBytes(Unpack up, Map<byte[], byte[]> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popBytes(), up.popBytes());
		}
	}

	/**
	 * unmarshal map<string,string>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapStringBytes(Unpack up, Map<String, byte[]> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popString(), up.popBytes());
		}
	}

	/**
	 * unmarshal map<string,uint32>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapStringUint32(Unpack up, Map<String, Uint32> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popString(), up.popUint32());
		}
	}

	/**
	 * unmarshal map<string,uint32>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapBytesUint32(Unpack up, Map<byte[], Uint32> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			map.put(up.popBytes(), up.popUint32());
		}
	}

	/**
	 * unmarshal map<uint32,map<uint32,uint32>>
	 * @param up
	 * @param map
	 */
    public static void unmarshalMapUint32MapUint32Uint32(Unpack up, Map<Uint32, Map<Uint32, Uint32>> map)
    {
        Uint32 size = up.popUint32();
        for (int i = 0; i < size.intValue(); i++)
        {
            Uint32 key = up.popUint32();
            Map<Uint32, Uint32> innerMap = new HashMap<Uint32, Uint32>();
            unmarshalMapUint32Uint32(up, innerMap);
            map.put(key, innerMap);
        }
    }



	/**
	 * unmarshal map<uint32,list<uint32>>
	 * @param up
	 * @param map
	 */
	public static void unmarshalMapUint32ListUint32(Unpack up, Map<Uint32, List<Uint32>> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Uint32 key = up.popUint32();
			List<Uint32> innerList= new ArrayList<>();
			unmarshalColUint32(up, innerList);
			map.put(key, innerList);
		}
	}

	/**
	 * unmarshal map<uint32,XXXClass>
	 * @param up
	 * @param map
	 */
    public static void unmarshalMapUint32Marshallable(Unpack up, Map map, Class<? extends Marshallable> type)
    {
        Uint32 size = up.popUint32();
        for (int i = 0; i < size.intValue(); i++)
        {
            Uint32 key = up.popUint32();
            try
            {
                Marshallable m = type.newInstance();
                m.unmarshall(up);
                map.put(key, m);
            } catch (IllegalAccessException e)
            {
                throw new UnpackException(e);
            } catch (InstantiationException e)
            {
                throw new UnpackException(e);
            }

        }
    }

	/**
	 * unmarshal map<string,XXXClass>
	 * @param up
	 * @param map
	 */
    public static void unmarshalMapStringMarshallable(Unpack up, Map map, Class<? extends Marshallable> type)
    {
        Uint32 size = up.popUint32();
        for (int i = 0; i < size.intValue(); i++)
        {
            String key = up.popString();
            try
            {
                Marshallable m = type.newInstance();
                m.unmarshall(up);
                map.put(key, m);
            } catch (IllegalAccessException e)
            {
                throw new UnpackException(e);
            } catch (InstantiationException e)
            {
                throw new UnpackException(e);
            }

        }
    }

	/**
	 * unmarshal map<uint32, map<string, string>>
	 */
	public static void unmarshalMapUint32MapStringString(Unpack up, Map<Uint32, Map<String, String>> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			Uint32 key = up.popUint32();
			Map<String, String> innerMap = new HashMap<String, String>();
			unmarshalMapStringString(up, innerMap);
			map.put(key, innerMap);
		}
	}

	/**
	 * unmarshal map<string, map<string, string>>
	 */
	public static void unmarshalMapStringMapStringString(Unpack up, Map<String, Map<String, String>> map)
	{
		Uint32 size = up.popUint32();
		for (int i = 0; i < size.intValue(); i++)
		{
			String key = up.popString();
			Map<String, String> innerMap = new HashMap<String, String>();
			unmarshalMapStringString(up, innerMap);
			map.put(key, innerMap);
		}
	}
}
