/**
 *
 */
package com.yy.live.core.auth;


import com.yy.live.core.IBaseCore;
import com.yy.live.model.bean.LoginUserInfo;
import com.yy.udbauth.ui.info.AccountInfo;

/**
 * @author daixiang
 */
public interface IAuthCore extends IBaseCore {

    /**
     * 注销
     */
    void logout();

    /**
     * 当前或上次登录成功的uid
     *
     * @return
     */
    long getUserId();

    /**
     * 是否已登录成功
     *
     * @return
     */
    boolean isLogined();

    /**
     * 是否匿名登陆
     *
     * @return
     */
    public boolean isAnoymousLogined();


    /**
     * 获取匿名登陆的uid
     *
     * @return
     */
    long getAnoymousUid();


    /**
     * 判断一个uid是否匿名用户
     *
     * @param uid uid
     * @return true or fasle
     */
    public boolean isAnonymousUser(long uid);

    public AccountInfo getActivitedAccount();

    public LoginUserInfo getLoginUserInfo();
}
