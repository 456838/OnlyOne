package com.duowan.onlyone.fm.video;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.duowan.onlyone.R;
import com.duowan.onlyone.business.video.VideoFragmentContract;
import com.duowan.onlyone.business.video.VideoFragmentPresenter;
import com.duowan.onlyone.func.video.model.VideoInfoEventArgs;
import com.duowan.onlyone.model.entity.VideoListBean;
import com.duowan.onlyone.view.adapter.VideoAdapter;
import com.duowan.onlyone.view.callback.EndLessOnScrollListener;
import com.salton123.event.StartBrotherEvent;
import com.salton123.mvp.ui.BaseSupportPresenterFragment;
import com.salton123.onlyonebase.view.widget.StatusTitleBar;
import com.salton123.util.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/7 13:47
 * Time: 13:47
 * Description:
 */
public class VideoFragment extends BaseSupportPresenterFragment<VideoFragmentPresenter> implements BGARefreshLayout.BGARefreshLayoutDelegate, VideoFragmentContract.View {
    RecyclerView recycler;
    BGARefreshLayout bgaRefresh;
    StatusTitleBar mHeaderView;
    private String date;
    private VideoAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    public int GetLayout() {
        return R.layout.video_home_fragment;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
        mPresenter = new VideoFragmentPresenter();
    }


    @Override
    public void InitListener() {
        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                mAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ARG_ITEM, (ArrayList<? extends Parcelable>) mAdapter.getData());
                bundle.putInt("position", position);
                EventBus.getDefault().post(new StartBrotherEvent(VideoDetailFragment.newInstance(VideoDetailFragment.class, bundle)));
            }
        });
        recycler.addOnScrollListener(new EndLessOnScrollListener(mLinearLayoutManager, 0) {
            @Override
            public void onLoadMore() {
                mPresenter.getData(date);
            }
        });
        bgaRefresh.setDelegate(this);
    }

    @Override
    public void InitViewAndData() {
        recycler = f(R.id.recycler);
        bgaRefresh = f(R.id.bgaRefresh);

        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(mLinearLayoutManager);
        mAdapter = new VideoAdapter(recycler);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(_mActivity, true);
        // 设置下拉刷新和上拉加载更多的风格
        bgaRefresh.setRefreshViewHolder(refreshViewHolder);
        recycler.setAdapter(mAdapter.getHeaderAndFooterAdapter());
        int statusHeight = ScreenUtils.getStatusHeight(_mActivity);
        System.out.println("statusHeight:" + statusHeight);
        mHeaderView = (StatusTitleBar) inflater().inflate(R.layout.simple_title_layout, null);
        mHeaderView.setTitleText("热门", View.VISIBLE).setTitleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("hello");
            }
        });
        mAdapter.addHeaderView(mHeaderView);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.getData(date);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mAdapter.clear();
        date = "";
        mPresenter.getData(date);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
//        mPresenter.getData(date);
        return false;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {
        bgaRefresh.endRefreshing();
//        bgaRefresh.endLoadingMore();
    }

    @Override
    public void showData(VideoListBean datalist) {
        mAdapter.addMoreData(datalist.getItemList());
        int end = datalist.getNextPageUrl().lastIndexOf("&num");
        int start = datalist.getNextPageUrl().lastIndexOf("date=");
        date = datalist.getNextPageUrl().substring(start + 5, end);
//        dismissLoading();
    }

    @Override
    public void showError(String errorMsg) {
        toast(errorMsg);
    }
}
