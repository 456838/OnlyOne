package com.salton123.xm.fm;

import android.os.Bundle;

import com.salton123.mvp.ui.BaseSupportPresenterFragment;
import com.salton123.xm.R;
import com.salton123.xm.business.OneToNContract;

/**
 * User: newSalton@outlook.com
 * Date: 2018/3/20 21:27
 * ModifyTime: 21:27
 * Description:
 */
public class TingShuFragment extends BaseSupportPresenterFragment<OneToNContract.Presenter> implements OneToNContract.TingShuIView {

    @Override
    public int GetLayout() {
        return R.layout.fm_ting_shu;
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
