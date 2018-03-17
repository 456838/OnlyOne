package com.duowan.liveshow.controller.fm;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;

import com.duowan.liveshow.R;
import com.duowan.liveshow.controller.fm.phone.PhoneCallFragment;

import com.duowan.liveshow.view.adapter.MyViewPagerAdapter;
import com.salton123.base.BaseSupportFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/20 11:51
 * Time: 11:51
 * Description:
 */
public class LiveShowHolderFragment extends BaseSupportFragment {
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList;

    @Override
    public int GetLayout() {
        return R.layout.fm_live_show_holder;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
    }

    @Override
    public void InitViewAndData() {
        mViewPager = f(R.id.viewPager);
        mFragmentList = new ArrayList<>();
//        mFragmentList.add(BaseSupportFragment.newInstance(ContactsFragement.class));
        mFragmentList.add(BaseSupportFragment.newInstance(PhoneCallFragment.class));
        mFragmentList.add(BaseSupportFragment.newInstance(LiveShowFragment.class));
//        mFragmentList.add(BaseSupportFragment.newInstance(TestFragment.class));
        mViewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), mFragmentList));
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void InitListener() {

    }

    @Override
    public boolean onBackPressedSupport() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(_mActivity);
        builder.
                setTitle("Hi").
                setMessage(Html.fromHtml("要离开直播间吗？")).
                setPositiveButton("嗯", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pop();
                    }
                }).setNegativeButton("再看看", null)

                .show();
        return true;
    }
}
