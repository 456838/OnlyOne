package com.salton123.xm.fm.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.salton123.base.BaseSupportFragment;
import com.salton123.event.StartBrotherEvent;
import com.salton123.util.EventUtil;
import com.salton123.util.log.MLog;
import com.salton123.xm.R;
import com.salton123.xm.fm.AlbumListFragment;
import com.salton123.xm.fm.TracksMultiTypeFragment;
import com.salton123.xm.model.ApiMethod;
import com.salton123.xm.view.EndLessOnScrollListener;
import com.salton123.xm.view.adapter.AlbumAdapter;
import com.salton123.xm.xmUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;

import cn.bingoogolapple.baseadapter.BGAOnRVItemClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * User: newSalton@outlook.com
 * Date: 2017/9/19 15:52
 * ModifyTime: 15:52
 * Description:
 */
public class GuoDeGangComponent extends BaseSupportFragment {
    private static final String TAG = AlbumListFragment.class.getName();
    private SwipeRefreshLayout refresh;
    private RecyclerView recycler;
    private AlbumAdapter mAdapter;
    private int categoryId = 12;
    private int page = 1;
    private int pageSize = 20;
    private int calcDimension = 2;
    private String tagName ="郭德纲";

    public static GuoDeGangComponent newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("arg_item", title);
        GuoDeGangComponent fragment = new GuoDeGangComponent();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int GetLayout() {
        return R.layout.fm_album_list;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {
//        String argItem = getArguments().getString(ARG_ITEM);
//        if (!StringUtil.isBlank(tagName)) {
//            tagName = argItem;
//        }
    }

    @Override
    public void InitViewAndData() {
        refresh = f(R.id.refresh);
        recycler = f(R.id.recycler);
        mAdapter = new AlbumAdapter(recycler);
        refresh.setProgressViewOffset(false, 100, 200);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getAlbumList(tagName, categoryId, calcDimension, page++, pageSize);
            }
        });
        LinearLayoutManager layout = new LinearLayoutManager(_mActivity);
        recycler.setLayoutManager(layout);
        recycler.addOnScrollListener(new EndLessOnScrollListener(layout, 1) {
            @Override
            public void onLoadMore() {
                getAlbumList(tagName, categoryId, calcDimension, page++, pageSize);
            }
        });
        recycler.setAdapter(mAdapter);
        getAlbumList(tagName, categoryId, calcDimension, page++, pageSize);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
//        getAlbumList(tagName, categoryId, calcDimension, page++, pageSize);
    }

    @Override
    public void InitListener() {
        mAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Album album = mAdapter.getItem(position);
                xmUtils.saveAlbum(_mActivity, album);
                EventUtil.sendEvent(new StartBrotherEvent(BaseSupportFragment.newInstance(TracksMultiTypeFragment.class, album)));
            }
        });
    }

    public void showAlbum(AlbumList list) {
        if (list.getAlbums().size() < pageSize) {
            toast("数据加载完毕");
        }
        if (refresh.isRefreshing()) {
            refresh.setRefreshing(false);
            page = 1;
            mAdapter.clear();
            mAdapter.setData(list.getAlbums());
        } else {
            mAdapter.addMoreData(list.getAlbums());
        }
    }

    public void onShowAlbumError(int resCode, String errorMsg) {
        MLog.error(TAG, "[onShowAlbumError] resCode= " + resCode + ",errorMsg=" + errorMsg);
        toast(errorMsg);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.info(TAG, "[onCreate]");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MLog.info(TAG, "[onViewCreated]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.info(TAG, "[onDestroy]");
    }

    public void getAlbumList(String tagName, int categoryId, int calc_dimension, int page, int pageSize) {
        ApiMethod.getAlbumList(categoryId, tagName, calc_dimension, page, pageSize).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<AlbumList>() {
            @Override
            public void accept(AlbumList albumList) throws Exception {
//                MLog.info(TAG, new GsonBuilder().setPrettyPrinting().create().toJson(albumList));
                showAlbum(albumList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                onShowAlbumError(0, throwable.getMessage());
            }
        });
    }

}
