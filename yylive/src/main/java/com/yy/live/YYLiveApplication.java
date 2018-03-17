package com.yy.live;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.duowan.mobile.YYApp;
import com.salton123.base.ApplicationBase;
import com.salton123.util.LogUtils;
import com.salton123.util.asynctask.ScheduledTask;
import com.salton123.util.log.MLog;
import com.yy.live.core.CoreManager;
import com.yy.live.model.YYLiveParams;
import com.yy.live.model.engine.YYEngine;
import com.yy.live.model.proxy.LoginProxy;
import com.yy.live.model.proxy.MediaProxy;
import com.yy.udbauth.AuthSDK;
import com.yy.udbauth.ui.AuthUI;
import com.yyproto.outlet.IProtoMgr;
import com.yyproto.outlet.SDKParam;

import java.io.File;
import java.util.List;


/**
 * User: 巫金生(newSalton@163.com)
 * Date: 2017/6/12 23:42
 * Description:
 * Updated:
 */
public class YYLiveApplication extends ApplicationBase {

    private MediaProxy mMediaProxy;
    private LoginProxy mLoginProxy;


    @Override
    public void onCreate() {
        super.onCreate();

        initYYSDK();
        asyncInit();
        CoreManager.init(this);
    }

    /**
     * 不需要第一时间就处理的初始化，延迟5秒后在子线程做
     */
    private void asyncInit() {
        ScheduledTask.getInstance().scheduledDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 3000);
    }

    public void initYYSDK() {
        YYApp.gContext = this;      //设置全局的Ctx
        String path = YYLiveParams.BASE_FILE_PATH + File.separator + "logs" + File.separator;
//        YLog.setLogFilePath(path + File.separator + "logs" + File.separator);
        initMLog();
        if (isPkgMainProc()) {
            SDKParam.AppInfo app = new SDKParam.AppInfo();
            app.appname = "yymand".getBytes();
            app.appVer = "yymand".getBytes();
//            app.verInt = 1;
            app.logLevel = SDKParam.LogLevel.LOG_VERBOSE;
            app.type2Icon.put(SDKParam.UInfoExWDType.TYPE_WD_HD, "4099".getBytes());
            app.logPath = null;
            IProtoMgr.instance().init(this, app);
            LogUtils.i("YYSDK", "YYApplication yysdk init done.");
            YYEngine.getInstance().init();     //将YYSDK消息组合
            //初始化AuthSDK,设置APPID,APPKEY,终端类型标识,匿名登录开启情况
            if (AuthSDK.init(this, YYLiveParams.YY_APP_ID, YYLiveParams.YY_APP_KEY, "0", true)) {
                AuthSDK.insertVerifyAppid("payplf");
                AuthSDK.insertVerifyAppid("5034");
//            AuthSDK.insertVerifyAppid("5060");
            } else {
                MLog.error(this, "AuthSDK init failed!");
            }
            AuthUI.getInstance().init(getApplicationContext(), YYLiveParams.YY_APP_ID, YYLiveParams.YY_APP_KEY, "0", true, null);
            mMediaProxy = MediaProxy.getInstance();
            mMediaProxy.init();
            mLoginProxy = LoginProxy.getInstance();
            mLoginProxy.init();
        }
    }

    public static void initMLog() {
        MLog.LogOptions options = new MLog.LogOptions();
        options.logLevel = MLog.LogOptions.LEVEL_INFO;
        options.honorVerbose = false;
        options.logFileName = "logs.txt";
        String path = YYLiveParams.BASE_FILE_PATH + File.separator + "logs" + File.separator;
        MLog.initialize(path, options);
        MLog.info("Logger", "init MLog, logFilePath = " + path + options.logFileName);
    }


    public boolean isPkgMainProc() {
        ActivityManager am = ((ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = this.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
