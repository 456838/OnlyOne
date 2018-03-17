package com.yy.live.core;

import java.util.Collection;
import java.util.Map;


/**
 * marshal collection or map util
 * 
 * @author zhongyongsheng
 */
public class MarshalContainer
{
	// ====================== marshal list, set below ======================
	/**
	 * marshal list<uint8>
	 * @param pack
	 * @param col
	 */
	public static void marshalColUint8(Pack pack, Collection<Uint8> col)
	{
		pack.push(new Uint32(col.size()));
		for (Uint8 o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<uint16>
	 * @param pack
	 * @param col
	 */
	public static void marshalColUint16(Pack pack, Collection<Uint16> col)
	{
		pack.push(new Uint32(col.size()));
		for (Uint16 o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<uint32>
	 * @param pack
	 * @param col
	 */
	public static void marshalColUint32(Pack pack, Collection<Uint32> col)
	{
		pack.push(new Uint32(col.size()));
		for (Uint32 o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<uint64>
	 * @param pack
	 * @param col
	 */
	public static void marshalColUint64(Pack pack, Collection<Uint64> col)
	{
		pack.push(new Uint32(col.size()));
		for (Uint64 o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<string>
	 * @param pack
	 * @param col
	 */
	public static void marshalColString(Pack pack, Collection<String> col)
	{
		pack.push(new Uint32(col.size()));
		for (String o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<string>
	 * @param pack
	 * @param col
	 */
	public static void marshalColBytes(Pack pack, Collection<byte[]> col)
	{
		pack.push(new Uint32(col.size()));
		for (byte[] o : col)
		{
			pack.push(o);
		}
	}

	/**
	 * marshal list<XXXClass>
	 * @param pack
	 * @param col
	 */
	public static void marshalColMarshallable(Pack pack, Collection<? extends Marshallable> col)
	{
		pack.push(new Uint32(col.size()));
		for (Marshallable m : col)
		{
			m.marshall(pack);
		}
	}

	/**
	 * marshal list<map<string,string>>
	 * @param pack
	 * @param col
	 */
	public static void marshalColMapStringString(Pack pack, Collection<Map<String, String>> col)
	{
		pack.push(new Uint32(col.size()));
		for (Map<String, String> m : col)
		{
			marshalMapStringString(pack, m);
		}
	}

	/**
	 * marshal list<map<string,string>>
	 * @param pack
	 * @param col
	 */
	public static void marshalColMapStringBytes(Pack pack, Collection<Map<String, byte[]>> col)
	{
		pack.push(new Uint32(col.size()));
		for (Map<String, byte[]> m : col)
		{
			marshalMapStringBytes(pack, m);
		}
	}

	/**
	 * marshal list<map<uint32,string>>
	 * @param pack
	 * @param col
	 */
	public static void marshalColMapUint32String(Pack pack, Collection<Map<Uint32, String>> col)
	{
		pack.push(new Uint32(col.size()));
		for (Map<Uint32, String> m : col)
		{
			marshalMapUint32String(pack, m);
		}
	}

	/**
	 * marshal list<map<uint32,uint32>>
	 * @param pack
	 * @param col
	 */
    public static void marshalColMapUint32Uint32(Pack pack, Collection<Map<Uint32, Uint32>> col)
    {
        pack.push(new Uint32(col.size()));
        for (Map<Uint32, Uint32> m : col)
        {
            marshalMapUint32Uint32(pack, m);
        }
    }

	/**
	 * marshal list<map<uint32, map<string, string>>>
	 * @param pack
	 * @param col
	 */
	public static void marshalColMapUint32MapStringString(Pack pack, Collection<Map<Uint32, Map<String, String>>> col)
	{
		pack.push(new Uint32(col.size()));
		for (Map<Uint32, Map<String, String>> m : col)
		{
			marshalMapUint32MapStringString(pack, m);
		}
	}

	// ===================== marshal map below =======================
	/**
	 * marshal map<uint8,uint32>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapUint8Uint32(Pack pack, Map<Uint8, Uint32> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint8 key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<uint16,uint32>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapUint16Uint32(Pack pack, Map<Uint16, Uint32> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint16 key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<uint16,string>
	 * @param pack
	 * @param map
	 */
  	public static void marshalMapUint16String(Pack pack, Map<Uint16, String> map)
    {
        pack.push(new Uint32(map.size()));
        for (Uint16 key : map.keySet())
        {
            pack.push(key);
            pack.push(map.get(key));
        }
  	}

	/**
	 * marshal map<uint32,uint32>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapUint32Uint32(Pack pack, Map<Uint32, Uint32> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint32 key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<uint32,string>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapUint32String(Pack pack, Map<Uint32, String> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint32 key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<uint32,string>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapUint32Bytes(Pack pack, Map<Uint32, byte[]> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint32 key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<string,string>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapStringString(Pack pack, Map<String, String> map)
	{
		pack.push(new Uint32(map.size()));
		for (String key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<string,string>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapBytesBytes(Pack pack, Map<byte[], byte[]> map)
	{
		pack.push(new Uint32(map.size()));
		for (byte[] key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<string,string>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapStringBytes(Pack pack, Map<String, byte[]> map)
	{
		pack.push(new Uint32(map.size()));
		for (String key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<string,uint32>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapStringUint32(Pack pack, Map<String, Uint32> map)
	{
		pack.push(new Uint32(map.size()));
		for (String key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<string,uint32>
	 * @param pack
	 * @param map
	 */
	public static void marshalMapBytesUint32(Pack pack, Map<byte[], Uint32> map)
	{
		pack.push(new Uint32(map.size()));
		for (byte[] key : map.keySet())
		{
			pack.push(key);
			pack.push(map.get(key));
		}
	}

	/**
	 * marshal map<uint32,map<uint32,uint32>>
	 * @param pack
	 * @param map
	 */
    public static void marshalMapUint32MapUint32Uint32(Pack pack, Map<Uint32, Map<Uint32, Uint32>> map)
    {
        pack.push(new Uint32(map.size()));
        for (Uint32 key : map.keySet())
        {
            pack.push(key);
            marshalMapUint32Uint32(pack, map.get(key));
        }
    }

	/**
	 * marshal map<uint32,XXXClass>
	 * @param pack
	 * @param map
	 */
    public static void marshalMapUint32Marshallable(Pack pack, Map<Uint32, ? extends Marshallable> map){
        pack.push(new Uint32(map.size()));
        for (Uint32 key : map.keySet())
        {
            pack.push(key);
            Marshallable value = map.get(key);
            if (value != null)
                value.marshall(pack);
        }
    }

	/**
	 * marshal map<uint32, map<string, string>>
	 */
	public static void marshalMapUint32MapStringString(Pack pack, Map<Uint32, Map<String, String>> map)
	{
		pack.push(new Uint32(map.size()));
		for (Uint32 key : map.keySet())
		{
			pack.push(key);
			marshalMapStringString(pack, map.get(key));
		}
	}

}
