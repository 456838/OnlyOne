package com.duowan.liveshow;

import com.duowan.liveshow.entity.YYHomeIndex;
import com.salton123.config.RetrofitManager;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * User: newSalton@outlook.com
 * Date: 2017/9/21 21:05
 * ModifyTime: 21:05
 * Description:
 */
public class LiveShowApi {
    public static final String YY_RECOMMEND_LIVESHOW = "http://idx.3g.yy.com/";

    public interface YYliveApiService {

        @GET("mobyy/nav/index/idx")
        Observable<YYHomeIndex> getRecommendLiveShow();
    }

    public static YYliveApiService GetYYliveApiService() {
        return RetrofitManager.getRetrofit(YY_RECOMMEND_LIVESHOW).create(YYliveApiService.class);
    }
}
