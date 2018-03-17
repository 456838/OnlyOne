package com.duowan.liveshow;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.salton123.base.BaseSupportActivity;

/**
 * User: newSalton@outlook.com
 * Date: 2017/9/21 17:35
 * ModifyTime: 17:35
 * Description:
 */
@Route(path = "/module/testaty")
public class TestActivity extends BaseSupportActivity {

    @Override
    public int GetLayout() {
        return R.layout.aty_test;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {

    }

    @Override
    public void InitViewAndData() {

    }

    @Override
    public void InitListener() {

    }
}
