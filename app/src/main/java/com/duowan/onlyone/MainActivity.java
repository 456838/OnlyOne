package com.duowan.onlyone;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.duowan.onlyone.fm.MainFragment;
import com.salton123.base.BaseSupportActivity;
import com.salton123.base.BaseSupportFragment;
import com.salton123.util.LogUtils;
import com.salton123.util.log.MLog;
import com.salton123.xm.wrap.XmlyInitializer;
import com.salton123.xm.wrapper.XmAdsStatusAdapter;
import com.salton123.xm.wrapper.XmPlayerStatusAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.util.BaseUtil;

import io.reactivex.functions.Consumer;

/**
 * User: newSalton@outlook.com
 * Date: 2017/9/22 19:40
 * ModifyTime: 19:40
 * Description:
 */
public class MainActivity extends BaseSupportActivity {
    private static final String TAG = "MainActivity";

    @Override
    public int GetLayout() {
        return R.layout.fm_container;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
        checkPermission();
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, BaseSupportFragment.newInstance(MainFragment.class));
        }

        // 可以监听该Activity下的所有Fragment的18个 生命周期方法
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks() {

            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                LogUtils.d("onFragmentCreated->" + f.toString());
            }

            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v,
                                              Bundle savedInstanceState) {
                LogUtils.d("onFragmentViewCreated->" + f.toString());
            }

            public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                LogUtils.d("onFragmentViewDestroyed->" + f.toString());
            }
        }, true);

    }

    @Override
    public void InitViewAndData() {

    }

    @Override
    public void InitListener() {

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean == false) {
                        Toast.makeText(getApplicationContext(), "请给权限", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        initXm();
                    }
                }
            });
        }
    }


    private IXmPlayerStatusListener iXmPlayerStatusListener = new XmPlayerStatusAdapter() {

    };

    private IXmAdsStatusListener iXmAdsStatusListener = new XmAdsStatusAdapter() {

    };


    public void initXm() {
        if (BaseUtil.isMainProcess(this)) {
            // SaltonApplication.<SaltonApplication>getInstance().sayHello();
            MLog.info(TAG, "init Xm");
            XmlyInitializer.getInstance().attch(SaltonApplication.getInstance()).defaultDownloadManager().notify(MainActivity.class, iXmPlayerStatusListener, iXmAdsStatusListener).businessHandle().init();
        }
    }
}
