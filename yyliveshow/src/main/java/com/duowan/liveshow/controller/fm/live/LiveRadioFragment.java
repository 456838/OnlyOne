package com.duowan.liveshow.controller.fm.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duowan.liveshow.LiveShowApi;
import com.duowan.liveshow.R;
import com.duowan.liveshow.controller.fm.LiveShowHolderFragment;
import com.duowan.liveshow.entity.YYHomeIndex;
import com.duowan.liveshow.utils.BGAUtil;
import com.duowan.liveshow.view.adapter.HotLiveRadioAdapter;
import com.salton123.base.BaseSupportFragment;
import com.salton123.event.StartBrotherEvent;
import com.salton123.mvp.util.RxUtil;
import com.salton123.onlyonebase.view.widget.Divider;
import com.yy.live.model.bean.core.ChannelInfo;
import com.yy.live.model.proxy.MediaProxy;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2016/9/24 15:32
 * Time: 15:32
 * Description:
 */
public class LiveRadioFragment extends BaseSupportFragment implements BGAOnRVItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {

    RecyclerView mRecyclerView;
    BGARefreshLayout mBGARefreshLayout;
    RelativeLayout rl_live_top, rl_top_title;
    HotLiveRadioAdapter mAdapter;
    EditText et_input_search;
    MediaProxy mMediaImpl = MediaProxy.getInstance();

    @Override
    public int GetLayout() {
        return R.layout.fm_live_radio;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {

    }

    @Override
    public void InitViewAndData() {
        mBGARefreshLayout = f(R.id.bgaRefreshLayout);
        mRecyclerView = f(R.id.recyclerView);
        rl_live_top = f(R.id.rl_live_top);
        rl_top_title = f(R.id.rl_top_title);
        et_input_search = f(R.id.et_input_search);
        mBGARefreshLayout.setRefreshViewHolder(BGAUtil.getNormalRefreshViewHolder());
        mAdapter = new HotLiveRadioAdapter(mRecyclerView);
        mAdapter.setOnRVItemClickListener(this);
        mRecyclerView.addItemDecoration(new Divider(getContext()));
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setAdapter(mAdapter);
        //数据加载和初始化
        loadData();
    }

    @Override
    public void InitListener() {
        mBGARefreshLayout.setDelegate(this);
        et_input_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String inputContent = et_input_search.getText().toString().trim();

                    ChannelInfo channelInfo = new ChannelInfo();
                    channelInfo.topSid = Long.parseLong(inputContent);
                    mMediaImpl.currentChannelInfo = channelInfo;
                    EventBus.getDefault().post(new StartBrotherEvent(BaseSupportFragment.newInstance(LiveShowHolderFragment.class)));
                }
                return false;
            }
        });

    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Bundle bundle = new Bundle();
        YYHomeIndex.DataBeanX.DataBean dataBean = mAdapter.getItem(position);
        ChannelInfo channelInfo = new ChannelInfo();
        channelInfo.topSid = dataBean.getSid();
        channelInfo.subSid = dataBean.getSsid();
        mMediaImpl.currentChannelInfo = channelInfo;
        EventBus.getDefault().post(new StartBrotherEvent(BaseSupportFragment.newInstance(LiveShowHolderFragment.class)));
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.clear();
        loadData();
        mBGARefreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    private void loadData() {

        Observable<YYHomeIndex> observable = LiveShowApi.GetYYliveApiService().getRecommendLiveShow();
        Disposable subscription = observable.compose(RxUtil.<YYHomeIndex>rxSchedulerHelper())
                .subscribe(new Consumer<YYHomeIndex>() {
                    @Override
                    public void accept(YYHomeIndex content) throws Exception {
                        List<YYHomeIndex.DataBeanX.DataBean> data2Bean = new LinkedList<YYHomeIndex.DataBeanX.DataBean>();
                        if (content != null) {
                            for (int i = 0; i < content.getData().size(); i++) {
                                YYHomeIndex.DataBeanX dataBean = content.getData().get(i);
                                data2Bean.addAll(dataBean.getData());
                            }
                            mAdapter.addNewData(data2Bean);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mBGARefreshLayout.endLoadingMore();
                    }
                });
        mCompositeSubscription.add(subscription);
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        loadData();
    }


}