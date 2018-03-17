package com.duowan.onlyone.fm.book;

import android.os.Bundle;
import android.view.View;

import com.duowan.onlyone.R;
import com.duowan.onlyone.fm.TestFragment;
import com.duowan.onlyone.fm.base.BaseHomeFragment;
import com.salton123.base.BaseSupportFragment;

/**
 * User: 巫金生(newSalton@163.com)
 * Date: 2017/5/25 22:24
 * Description:
 * Update:
 */
public class BookFragment extends BaseHomeFragment {

    @Override
    public int GetLayout() {
        return R.layout.fm_book;
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
                start(BaseSupportFragment.newInstance(TestFragment.class));
            }
        });
    }
}
