/**
 *
 */
package com.yy.live.core.auth;

import com.yy.live.core.CoreError;
import com.yy.live.core.ICoreClient;


public interface IAuthClient extends ICoreClient {

    public void onLoginSucceed(boolean anonymous, String userId);

    public void onLoginFail(CoreError coreError);


    public void onLogout();

    public void onKickOff(byte[] strReason, int uReason);

}
