package com.duowan.onlyone.fm;

import android.os.Bundle;
import android.view.View;

import com.duowan.onlyone.R;
import com.duowan.onlyone.fm.base.BaseHomeFragment;
import com.duowan.onlyone.fm.book.BookFragment;
import com.salton123.base.BaseSupportFragment;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TestFragment extends BaseHomeFragment{

    @Override
    public int GetLayout() {
        return R.layout.fm_test;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {

    }

    @Override
    public void InitViewAndData() {

    }

    @Override
    public void InitListener() {
        f(R.id.tv_hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(BaseSupportFragment.newInstance(BookFragment.class));
            }
        });
    }

}
