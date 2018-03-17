package com.yy.live.core;

import android.content.Context;

import com.yy.live.core.auth.AuthCoreImpl;
import com.yy.live.core.auth.IAuthCore;
import com.yy.live.core.channel.micinfo.ChannelMicCoreImpl;
import com.yy.live.core.channel.micinfo.IChannelMicCore;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/23 21:21
 * Time: 21:21
 * Description:
 */
public class CoreManager extends ICoreManager {
    /**
     * 程序启动时需要先调用此接口（如在Application的onCreate）
     *
     * @param app
     */
    public static void init(Context app) {
        context = app;
        if (!CoreFactory.hasRegisteredCoreClass(IChannelMicCore.class)) {
            CoreFactory.registerCoreClass(IChannelMicCore.class, ChannelMicCoreImpl.class);
        }
        if (!CoreFactory.hasRegisteredCoreClass(IAuthCore.class)) {
            CoreFactory.registerCoreClass(IAuthCore.class, AuthCoreImpl.class);
        }
        getCore(IChannelMicCore.class);
        getCore(IAuthCore.class);
    }

}
