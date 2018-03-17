/**
 * 每个core实现类都应该继承此类
 * 提供一些基础设施给子类使用
 */
package com.yy.live.core;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

/**
 * @author daixiang
 */
public abstract class AbstractBaseCore implements IBaseCore {
    public AbstractBaseCore() {
        // 确保有默认构造函数
        EventBus.getDefault().register(this);
    }

    protected Context getContext() {
        return  ICoreManagerBase.getContext();
    }

    protected void notifyClients(Class<? extends ICoreClient> clientClass, String methodName, Object... args) {
        ICoreManagerBase.notifyClients(clientClass, methodName, args);
    }

    public void destory() {
        EventBus.getDefault().unregister(this);
    }

}
