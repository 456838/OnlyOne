package com.duowan.onlyone.fm.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.duowan.onlyone.R;
import com.duowan.onlyone.model.api.OnlyOneApi;
import com.duowan.onlyone.model.entity.yinyuetai.AreaBean;
import com.duowan.onlyone.model.entity.yinyuetai.MVListBean;
import com.duowan.onlyone.model.entity.yinyuetai.VideoBean;
import com.duowan.onlyone.view.adapter.HotMusicItemAdapter;
import com.salton123.base.BaseSupportFragment;
import com.salton123.mvp.util.RxUtil;
import com.salton123.util.LogUtils;
import com.salton123.util.log.MLog;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/7/10 10:47
 * Time: 10:47
 * Description:
 */
public class HotMusicItemFragment extends BaseSupportFragment {

    public static final String TAG = "HotMusicItemFragment";
    RecyclerView rv_hot_music_item;
    SwipeRefreshLayout srl_hot_music_item;
    private AreaBean mAreaBean;
    private HotMusicItemAdapter mHotMusicItemAdapter;
    private ArrayList<VideoBean> videosList = new ArrayList<>();

    protected boolean refresh;
    protected int lastVisibleItem;
    protected boolean hasMore = true;
    protected static final int SIZE = 20;
    protected int mOffset = 0;

    @Override
    public int GetLayout() {
        return R.layout.fm_hot_music_item;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
    }

    @Override
    public void InitViewAndData() {
        srl_hot_music_item = f(R.id.srl_hot_music_item);
        rv_hot_music_item = f(R.id.rv_hot_music_item);
        mHotMusicItemAdapter = new HotMusicItemAdapter(videosList);
        rv_hot_music_item.setLayoutManager(new LinearLayoutManager(getActivity()));
        srl_hot_music_item.setColorSchemeResources(R.color.colorPrimary);
        srl_hot_music_item.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources()
                        .getDisplayMetrics()));
        srl_hot_music_item.setRefreshing(true);
        rv_hot_music_item.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((HotMusicItemAdapter.ViewHolder) holder).videoplayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return true;
        } else {
            return super.onBackPressedSupport();
        }
    }

    @Override
    public void InitListener() {
        rv_hot_music_item.setAdapter(mHotMusicItemAdapter);
        rv_hot_music_item.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1 == mHotMusicItemAdapter.getItemCount()) && hasMore) {
                    loadHotMusic(mAreaBean.code, mOffset + 1, SIZE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

            }
        });
        srl_hot_music_item.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                videosList.clear();
                mOffset = 0;
                loadHotMusic(mAreaBean.code, mOffset, SIZE);


            }
        });
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        MLog.info(TAG, "onLazyInitView");
        mAreaBean = getArguments().getParcelable(ARG_ITEM);
        loadHotMusic(mAreaBean.code, mOffset, SIZE);
    }

    //初始化音乐
    private void loadHotMusic(String areaCode, int offset, int size) {
        showLoading();
        Observable<MVListBean> observable = OnlyOneApi.GetMusicApiService().getVideoList(OnlyOneApi.deviceinfo, areaCode, offset, size);
        Disposable subscription = observable.compose(RxUtil.<MVListBean>rxSchedulerHelper()).subscribe(new Consumer<MVListBean>() {
            @Override
            public void accept(MVListBean bean) throws Exception {
                if (bean != null) {
                    setData(bean.getVideos());
                } else {
                    toast("网络错误，请重试");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                toast("网络异常，请重试！");
                throwable.printStackTrace();
                LogUtils.e(throwable.getMessage());
            }
        });
        mCompositeSubscription.add(subscription);
    }

    public void setData(List<VideoBean> videoList) {
        dismissLoading();
        if (refresh) {
            refresh = false;
            mOffset = 0;
            videosList.clear();
        }
        if (videoList == null || videoList.size() == 0) {
            hasMore = false;
        } else {
            hasMore = true;
            int pos = videosList.size() - 1;
            videosList.addAll(videoList);
            mHotMusicItemAdapter.notifyItemRangeChanged(pos, videoList.size());
            mOffset += videoList.size();
        }
    }

    public void showLoading() {
        srl_hot_music_item.setRefreshing(true);
    }

    public void dismissLoading() {
        srl_hot_music_item.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.info(TAG, "onCreate,instance=" + HotMusicItemFragment.this.toString() + ",mOffset=" + mOffset);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MLog.info(TAG, "onViewCreated,instance=" + HotMusicItemFragment.this.toString() + ",mOffset=" + mOffset);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MLog.info(TAG, "onDestroyView,instance=" + HotMusicItemFragment.this.toString() + ",mOffset=" + mOffset);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.info(TAG, "onDestroy,instance=" + HotMusicItemFragment.this.toString() + ",mOffset=" + mOffset);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MLog.info(TAG, "onSaveInstanceState=" + outState);
    }
}
