package com.duowan.onlyone.func.video.model

import com.duowan.onlyone.model.entity.kaiyan.ItemListBean

/**
 * User: newSalton@outlook.com
 * Date: 2018/4/12 20:12
 * ModifyTime: 20:12
 * Description:
 */
data class VideoInfoEventArgs(var pageList: List<ItemListBean> = emptyList(), var position: Int = 0)