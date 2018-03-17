package com.duowan.onlyone.model.entity.yinyuetai;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/5/10
 * YinYueTai
 */
public class AreaBean implements Parcelable {
    /**
     * name : 首播
     * code : FS
     */
    public  String name;
    public String code;


    protected AreaBean(Parcel in) {
        name = in.readString();
        code = in.readString();
    }

    public static final Creator<AreaBean> CREATOR = new Creator<AreaBean>() {
        @Override
        public AreaBean createFromParcel(Parcel in) {
            return new AreaBean(in);
        }

        @Override
        public AreaBean[] newArray(int size) {
            return new AreaBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(code);
    }
}
