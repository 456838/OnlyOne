package com.duowan.onlyone.model.engine;

import com.duowan.onlyone.model.api.OnlyOneApi;
import com.duowan.onlyone.model.entity.VideoListBean;

import io.reactivex.Observable;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/10 16:04
 * Time: 16:04
 * Description:
 */
public class OnlyOneApiEngine {


//    Observable<NewAcVideo> videoObservable = NewAcApi.getNewAcVideo().onResult(videoId);
//        videoObservable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new Action1<NewAcVideo>() {
//        @Override
//        public void call(NewAcVideo newAcVideo) {
//            List<NewAcVideo.DataEntity.FilesEntity> list = newAcVideo.getData().getFiles();
//            Collections.reverse(list);
//            String url = list.get(0).getUrl().get(0);
//
//            request.setUrl(url);
//            request.setFilePath(filePath);
//
//            mDownloadRequests.put(videoId, request);
//            download(videoId, request);
//        }
//    });

    public static void getMusicList(String data) {
        Observable<VideoListBean> videoObservable = OnlyOneApi.GetKyApiService().getVideoList(data);

    }

}
