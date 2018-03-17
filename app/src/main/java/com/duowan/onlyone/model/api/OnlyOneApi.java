package com.duowan.onlyone.model.api;

import com.duowan.onlyone.model.config.RetrofitManager;
import com.duowan.onlyone.model.entity.VideoListBean;
import com.duowan.onlyone.model.entity.yinyuetai.AreaBean;
import com.duowan.onlyone.model.entity.yinyuetai.MVListBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/10 15:41
 * Time: 15:41
 * Description:
 */

public class OnlyOneApi {



    public interface KyApiService {

        @GET("tabs/selected")
        Observable<VideoListBean> getVideoList(@Query("date") String date);
    }


    public interface MusicApiService {
        @GET("get_mv_areas.json")
        Observable<List<AreaBean>> getAreaList(@Query("deviceinfo") String info);

        @GET("list.json")
        Observable<MVListBean> getVideoList(@Query("deviceinfo") String info,@Query("area") String area,@Query("offset") int offset,@Query("size") int size);
    }


    public static final String KAIYAN_HOST = "https://baobab.kaiyanapp.com/api/v4/";


    public static final String HOT_MUSIC = "http://mapi.yinyuetai.com/video/";
    public static final String deviceinfo = "{\"aid\":\"10201036\",\"os\":\"Android\","
            + "\"ov\":" + "\"" + android.os.Build.VERSION.RELEASE + "\"" + ","
            + "\"rn\":\"720*1280\","
            + "\"dn\":" + "\"" + android.os.Build.MODEL + "\"" + ","
            + "\"cr\":\"46000\","
            + "\"as\":"
            + "\"WIFI\","
            + "\"uid\":"
            + "\"dbcaa6c4482bc05ecb0bf39dabf207d2\","
            + "\"clid\":110025000}";


    public static KyApiService GetKyApiService() {
        return RetrofitManager.getRetrofit(KAIYAN_HOST).create(KyApiService.class);
    }




    public static MusicApiService GetMusicApiService() {
        return RetrofitManager.getRetrofit(HOT_MUSIC).create(MusicApiService.class);
    }

}
