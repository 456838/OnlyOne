package com.duowan.onlyone.fm;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duowan.onlyone.R;
import com.duowan.onlyone.fm.base.BaseHomeFragment;
import com.duowan.onlyone.fm.music.HotMusicFragment;
import com.salton123.base.BaseSupportFragment;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/15 11:35
 * Time: 11:35
 * Description:
 */
public class SecondFragment extends BaseHomeFragment {

    @Override
    public int GetLayout() {
        return R.layout.fm_container;
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_container, BaseSupportFragment.newInstance(HotMusicFragment.class));
        }
    }
}
