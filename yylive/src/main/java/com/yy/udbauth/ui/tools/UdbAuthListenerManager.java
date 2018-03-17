package com.yy.udbauth.ui.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UdbAuthListenerManager {

	private Map<OpreateType, OnUdbAuthListener> mListenerCache;

	private static UdbAuthListenerManager sInstance;

	public UdbAuthListenerManager() {
		mListenerCache = new ConcurrentHashMap<OpreateType, OnUdbAuthListener>();
	}

	public static UdbAuthListenerManager getInstance() {
		if (sInstance == null) {
			synchronized (UdbAuthListenerManager.class) {
				sInstance = new UdbAuthListenerManager();
			}
		}
		return sInstance;
	}

	/**
	 * 添加一个回调监听器，旧的Listener将被取代
	 * @param type		操作类型
	 * @param listener	监听器
	 * @return	返回旧的Listener，如果不存在，返回null
	 */
	public OnUdbAuthListener put(OpreateType type, OnUdbAuthListener listener) {
		return mListenerCache.put(type, listener);
	}

	/**
	 * 获取一个监听器，并把它给删除掉
	 * @param type	操作类型
	 * @return	如果不存在，返回null
	 */
	public OnUdbAuthListener getAndRemove(OpreateType type) {
		return mListenerCache.remove(type);
	}

	public boolean contain(OpreateType type) {
		return mListenerCache.containsKey(type);
	}
}
