package com.duowan.onlyone.fm;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duowan.onlyone.R;
import com.duowan.onlyone.fm.base.BaseHomeFragment;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/15 11:35
 * Time: 11:35
 * Description:
 */
public class FourthFragment extends BaseHomeFragment {
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
//        if (findChildFragment(AvatarFragment.class) == null) {
//            loadFragment();
//        }
    }

//    private void loadFragment() {
//        loadRootFragment(R.id.fl_fourth_container_upper, AvatarFragment.newInstance());
//        loadRootFragment(R.id.fl_fourth_container_lower, MeFragment.newInstance());
//    }

//    @Override
//    public int GetLayout() {
//        return R.layout.fm_container;
//    }
//
//    @Override
//    public void InitVariable(Bundle savedInstanceState) {
//
//    }
//
//
//    @Override
//    public void InitViewAndData() {
//    }
//
//    @Override
//    public void InitListener() {
//
//    }
//
//    @Override
//    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//        super.onLazyInitView(savedInstanceState);
//        if (savedInstanceState == null) {
////            loadRootFragment(R.id.fl_container, BaseSupportFragment.newInstance(PersonCenterFragment.class));
//            loadRootFragment(R.id.fl_container, AvatarFragment.newInstance());
//        }
//    }
}
