package com.duowan.liveshow.utils;

import com.salton123.base.ApplicationBase;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/11 15:45
 * Time: 15:45
 * Description:
 */
public class BGAUtil {

    public static BGANormalRefreshViewHolder getNormalRefreshViewHolder() {
        BGANormalRefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(ApplicationBase.getInstance(), true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("加载更多");
        // 设置整个加载更多控件的背景颜色资源 id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);

        return refreshViewHolder;
    }
}
