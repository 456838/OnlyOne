package com.duowan.onlyone.fm.video

import android.os.Bundle
import android.view.View
import com.duowan.onlyone.MainActivity
import com.duowan.onlyone.MainActivity.MyTouchListener
import com.duowan.onlyone.R
import com.duowan.onlyone.func.video.view.adapter.VerticalSwitchAdapter
import com.duowan.onlyone.func.video.view.widget.SmallVideoPlayView
import com.duowan.onlyone.model.entity.kaiyan.DataBean
import com.duowan.onlyone.model.entity.kaiyan.ItemListBean
import com.duowan.onlyone.model.entity.utils.DateUtil
import com.salton123.base.BaseSupportSwipeBackFragment
import com.salton123.onlyonebase.ImageLoader
import com.salton123.onlyonebase.view.verticalswitch.MoveDirection
import com.salton123.onlyonebase.view.verticalswitch.ScrollItem
import com.salton123.onlyonebase.view.verticalswitch.SimpleCallback
import com.salton123.onlyonebase.view.verticalswitch.VerticalSwitchManager
import com.salton123.util.log.MLog
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.NiceVideoPlayerController
import com.xiao.nicevideoplayer.TxVideoPlayerController
import kotlinx.android.synthetic.main.top_title.*
import kotlinx.android.synthetic.main.video_home_detail_holder.*


/**
 * User: newSalton@outlook.com
 * Date: 2018/4/12 20:35
 * ModifyTime: 20:35
 * Description:
 */
class VideoDetailComponent : BaseSupportSwipeBackFragment() {
    override fun InitListener() {
//        tv_title_back.visibility = View.VISIBLE
//        tv_title_back.setOnClickListener { pop() }
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
        /** 触摸事件的注册 */
        (this.activity as MainActivity).registerMyTouchListener(myTouchListener)
    }


    override fun InitViewAndData() {
        initSwitchManager()
        mSwitchManager.setCanLoadMore(false)
        if (position < 0 || position > dataList.size - 1) {
            position = 0
        }
        mSwitchManager.setData(dataList, dataList[position])
        mPlayerController = TxVideoPlayerController(activity)
        videoplayer.setController(mPlayerController)
        videoplayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE)
        if (dataBean != null) {
            ImageLoader.display(mPlayerController.imageView(), dataBean.cover.detail)
            mPlayerController.setTitle(dataBean.title)
            videoplayer.setUp(dataBean.playUrl, null)
//            title.text = dataBean.title
//            val stringBuilder = StringBuilder()
//            stringBuilder.append("#").append(dataBean.category)
//                    .append(" ")
//                    .append(" / ")
//                    .append(" ")
//                    .append(DateUtil.formatTime2(dataBean.duration.toLong()))
//            type.text = stringBuilder.toString()
//            description.text = dataBean.description
//            title.setText(dataBean.category)
        }
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
        mSwitchManager!!.addViewToMoveWithCenterView(videoplayer)
        mSwitchManager!!.setLoadMoreCallback(object : VerticalSwitchManager.LoadMoreCallback {
            override fun onLoadMore() {
                loadMoredataList()
            }
        })
        mSwitchManager!!.usePreload(true)
        mSwitchManager!!.initPreloadParam(5)
    }


    private fun onPageSelected(selectedItem: ScrollItem<ItemListBean>, moveDirection: MoveDirection) {
        val playInfo = selectedItem.data as ItemListBean
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

        if (smallVideoPlayInfo != null) {
            if (videoplayer.isPlaying()) {
                videoplayer.pause()
            }
            videoplayer.setUp(smallVideoPlayInfo.getData().playUrl, null)
            videoplayer.start()
        } else {
            MLog.info(TAG, "playVideo smallVideoPlayInfo is null")
        }
    }

    fun getCurrentVideoInfo(): ItemListBean? {
        return if (mCurrentVideoPlayView == null) null else mCurrentVideoPlayView!!.playInfo
    }

    private fun onPageUnSelected(unSelectedItem: ScrollItem<ItemListBean>) {
        val view = unSelectedItem.mView as SmallVideoPlayView
        view.onUnSelected()
    }


    private fun scrollPlayerView(dy: Float) {
        val tY = -dy + videoplayer.y
        videoplayer.translationY = tY
    }


    private fun loadMoredataList() {
        MLog.info(TAG, "loadMoredataList")
    }


//    fun dispatchTouchEvent(ev: MotionEvent): Boolean {
//        mSwitchManager.onDispatchTouchEvent(ev)
//        return super.dispatchTouchEvent(ev)
//    }

    /** 接收MainActivity的Touch回调的对象，重写其中的onTouchEvent函数  */
    var myTouchListener: MyTouchListener = MyTouchListener { event ->
        //处理手势事件（根据个人需要去返回和逻辑的处理）
        mSwitchManager.onDispatchTouchEvent(event)
        true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /** 触摸事件的注销  */
        (this.activity as MainActivity).unRegisterMyTouchListener(myTouchListener)
    }
}