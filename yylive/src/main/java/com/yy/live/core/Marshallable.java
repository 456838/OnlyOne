package com.yy.live.core;


/**
 * marshal interface, marshal to pack and unmarshal to unpack
 * 
 * @author zhongyongsheng
 */
public interface Marshallable {
	
	public void marshall(Pack pack);
	public void unmarshall(Unpack up);

}
