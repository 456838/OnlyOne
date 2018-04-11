package com.duowan.onlyone.func.video.model;

import android.net.Uri;
import android.support.annotation.DrawableRes;

import java.io.Serializable;


public class SmallVideoPlayInfo implements Serializable {

    public Uri videoUrl;
    public @DrawableRes
    int snapshotUrl;

    public SmallVideoPlayInfo() {
    }

    public SmallVideoPlayInfo(Uri videoUrl, @DrawableRes int snapshotUrl) {
        this.videoUrl = videoUrl;
        this.snapshotUrl = snapshotUrl;
    }


    @Override
    public String toString() {
        return "SmallVideoPlayInfo{" +
                ", videoUrl='" + videoUrl + '\'' +
                ", snapshotUrl='" + snapshotUrl + '\'' +
                '}';
    }
}
