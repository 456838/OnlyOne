package com.duowan.onlyone.fm.video

import android.os.Bundle
import android.view.MotionEvent
import com.duowan.onlyone.R
import com.duowan.onlyone.func.video.model.SmallVideoPlayInfo
import com.duowan.onlyone.func.video.view.adapter.VerticalSwitchAdapter
import com.duowan.onlyone.func.video.view.widget.SmallVideoPlayView
import com.duowan.onlyone.model.entity.kaiyan.DataBean
import com.duowan.onlyone.model.entity.kaiyan.ItemListBean
import com.salton123.base.BaseSupportSwipeBackFragment
import com.salton123.onlyonebase.view.verticalswitch.MoveDirection
import com.salton123.onlyonebase.view.verticalswitch.ScrollItem
import com.salton123.onlyonebase.view.verticalswitch.SimpleCallback
import com.salton123.onlyonebase.view.verticalswitch.VerticalSwitchManager
import com.salton123.util.log.MLog
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import kotlinx.android.synthetic.main.video_home_detail_fragment.*
import kotlinx.android.synthetic.main.video_home_detail_holder.*

/**
 * User: newSalton@outlook.com
 * Date: 2018/4/12 20:35
 * ModifyTime: 20:35
 * Description:
 */
class VideoDetailComponent : BaseSupportSwipeBackFragment() {
    override fun InitListener() {

    }

    private val TAG = "VideoDetailComponent"
    private lateinit var mSwitchManager: VerticalSwitchManager<ItemListBean>
    private var mCurrentVideoPlayView: SmallVideoPlayView? = null

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
        return R.layout.video_home_detail_holder
    }


    override fun InitVariable(p0: Bundle?) {
        dataList = arguments.getParcelableArrayList(ARG_ITEM)
        position = arguments.getInt("position")
        dataBean = dataList[position].data
    }


    override fun InitViewAndData() {
        initSwitchManager()
        mSwitchManager.setCanLoadMore(false)
        if (position < 0 || position > dataList.size - 1) {
            position = 0
        }
        mSwitchManager.setData(dataList, dataList[position])
//        mSmallVideoPlayer!!.setVideoURI(dataList[position].videoUrl)
//        mSmallVideoPlayer!!.setOnInfoListener(MediaPlayer.OnInfoListener { mp, what, extra ->
//            if (mCurrentVideoPlayView != null) {
//                mCurrentVideoPlayView!!.hideCoverView()
//            }
//            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
//                mSmallVideoPlayer!!.setBackgroundColor(Color.TRANSPARENT)
//            }
//            true
//        })
//        mSmallVideoPlayer!!.setOnCompletionListener(MediaPlayer.OnCompletionListener { mediaPlayer -> mediaPlayer.start() })
//        mSmallVideoPlayer!!.start()
    }


    private fun initSwitchManager() {
        val viewSwitchAdapter = VerticalSwitchAdapter()
        mSwitchManager = VerticalSwitchManager(vertical_switch_layout, viewSwitchAdapter)
        mSwitchManager.setCallback(object : SimpleCallback<ItemListBean>() {
            override fun onSelected(selectedItem: ScrollItem<ItemListBean>?, nextItem: ScrollItem<ItemListBean>?, preItem: ScrollItem<ItemListBean>?, moveDirection: MoveDirection) {
                super.onSelected(selectedItem, nextItem, preItem, moveDirection)
                //选中的时候，请求数据
                if (selectedItem != null && selectedItem.mView is SmallVideoPlayView) {
                    onPageSelected(selectedItem, moveDirection)
                }
                if (nextItem != null && nextItem.mView is SmallVideoPlayView) {
                    onPageUnSelected(nextItem)
                }
                if (preItem != null && preItem.mView is SmallVideoPlayView) {
                    onPageUnSelected(preItem)
                }
            }

            override fun onScroll(dy: Float) {
                super.onScroll(dy)
                scrollPlayerView(dy)
            }

            override fun canScroll(): Boolean {
                return true
            }

        })
        mSwitchManager!!.addViewToMoveWithCenterView(ll_detail)
        mSwitchManager!!.setLoadMoreCallback(object : VerticalSwitchManager.LoadMoreCallback {
            override fun onLoadMore() {
                loadMoredataList()
            }
        })
        mSwitchManager!!.usePreload(true)
        mSwitchManager!!.initPreloadParam(5)
    }


    private fun onPageSelected(selectedItem: ScrollItem<ItemListBean>, moveDirection: MoveDirection) {
        val playInfo = selectedItem.data as SmallVideoPlayInfo
        if (playInfo == null) {
            MLog.error(TAG, "onPageSelected, play info is null, scrollItem: %s", selectedItem)
            return
        }
        MLog.info(TAG, "onPageSelected, playInfo: %s", playInfo)
        mCurrentVideoPlayView = selectedItem.mView as SmallVideoPlayView?
        mCurrentVideoPlayView!!.onSelected()
        rootView.translationY = 0f
        playVideo()
    }

    fun playVideo() {
        val smallVideoPlayInfo = getCurrentVideoInfo()
//        if (smallVideoPlayInfo != null) {
//            if (mSmallVideoPlayer!!.isPlaying()) {
//                mSmallVideoPlayer!!.pause()
//                mSmallVideoPlayer!!.suspend()
//            }
//            mSmallVideoPlayer!!.setVideoURI(smallVideoPlayInfo.videoUrl)
//            mSmallVideoPlayer!!.start()
//        } else {
//            MLog.info(TAG, "playVideo smallVideoPlayInfo is null")
//        }
    }

    fun getCurrentVideoInfo(): SmallVideoPlayInfo? {
        return if (mCurrentVideoPlayView == null) null else mCurrentVideoPlayView!!.playInfo
    }

    private fun onPageUnSelected(unSelectedItem: ScrollItem<ItemListBean>) {
        val view = unSelectedItem.mView as SmallVideoPlayView
        view.onUnSelected()
    }


    private fun scrollPlayerView(dy: Float) {
        val tY = -dy + ll_detail.y
        ll_detail.translationY = tY
    }


    private fun loadMoredataList() {
        MLog.info(TAG, "loadMoredataList")
    }


//    fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        mSwitchManager.onDispatchTouchEvent(ev)
//        return super.dispatchTouchEvent(ev)
//    }



}