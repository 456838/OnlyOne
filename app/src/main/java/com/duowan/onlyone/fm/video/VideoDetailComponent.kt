package com.duowan.onlyone.fm.video

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.duowan.onlyone.R
import com.duowan.onlyone.model.entity.kaiyan.DataBean
import com.duowan.onlyone.model.entity.kaiyan.ItemListBean
import com.duowan.onlyone.model.entity.utils.DateUtil
import com.salton123.base.BaseSupportSwipeBackFragment
import com.salton123.onlyonebase.ImageLoader
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevideoplayer.NiceVideoPlayerManager
import com.xiao.nicevideoplayer.TxVideoPlayerController
import kotlinx.android.synthetic.main.status_title_bar.*
import kotlinx.android.synthetic.main.video_home_detail_fragment.*

/**
 * User: newSalton@outlook.com
 * Date: 2018/4/12 20:35
 * ModifyTime: 20:35
 * Description:
 */
class VideoDetailComponent : BaseSupportSwipeBackFragment() {

    companion object {
        fun newInstance(bundle: Bundle): VideoDetailComponent {
            var fragment = VideoDetailComponent()
            fragment.arguments = bundle
            return fragment
        }
    }

    var position: Int = 0
    lateinit var dataList: ArrayList<ItemListBean>
    lateinit var mPlayerController: NiceVideoPlayerController
    private lateinit var dataBean: DataBean
    override fun GetLayout(): Int {
        return R.layout.video_home_detail_fragment
    }


    override fun InitVariable(p0: Bundle?) {
        dataList = arguments.getParcelableArrayList(ARG_ITEM)
        position = arguments.getInt("position")
        dataBean = dataList[position].data
    }

    override fun InitViewAndData() {
        mPlayerController = TxVideoPlayerController(activity)
        videoplayer.setController(mPlayerController)
        videoplayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE)
        dataBean?.let {
            ImageLoader.display(mPlayerController.imageView(), dataBean.cover.detail)
            mPlayerController.setTitle(dataBean.title)
            videoplayer.setUp(dataBean.playUrl, null)
            title.text = dataBean.title
            val stringBuilder = StringBuilder()
            stringBuilder.append("#").append(dataBean.category)
                    .append(" ")
                    .append(" / ")
                    .append(" ")
                    .append(DateUtil.formatTime2(dataBean.duration.toLong()))
            type.text = stringBuilder.toString()
            description.text = dataBean.description
            title.text = dataBean.category
        }

    }

    override fun InitListener() {
        tv_title_back.visibility = View.VISIBLE
        tv_title_back.setOnClickListener { pop() }
    }

    override fun onPause() {
        super.onPause()
        NiceVideoPlayerManager.instance().suspendNiceVideoPlayer()
    }

    override fun onResume() {
        super.onResume()
        NiceVideoPlayerManager.instance().resumeNiceVideoPlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer()
    }

    override fun onBackPressedSupport(): Boolean {
        return if (NiceVideoPlayerManager.instance().onBackPressd()) {
            true
        } else {
            super.onBackPressedSupport()
        }
    }
}