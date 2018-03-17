package com.yy.live.model.proxy;

import com.salton123.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/15 18:30
 * Time: 18:30
 * Description:
 */
public class BaseProxy {
    public void p(Object object) {
        if (object != null) {
            EventBus.getDefault().post(object);
//            EventUtil.sendEvent(object);
        } else {
            LogUtils.e("BaseProxy: object is null");
        }
    }
}
