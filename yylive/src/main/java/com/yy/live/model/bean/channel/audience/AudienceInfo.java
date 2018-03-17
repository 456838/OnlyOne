package com.yy.live.model.bean.channel.audience;

/**
 * Created by Zhanghuiping on 14/6/30.
 */

public class AudienceInfo {
	public Boolean showMenu=false; //是否显示关注 私聊菜单
    public long uid;//uid
    public String name;//昵称
    public String portraitUrl;//头像
    public int portraitIndex;//系统头像的下标
    public int nobleLevel;//贵族等级
    public int guardianLevel;//
    public int isAnchor;//是否主播
    public boolean isSubscribe = false;//是否已关注
    private static int USERISANCHOR = 1; //主播
    private static int USERNOTANCHOR = 0;//非主播

    public boolean isAnchor() {
        if(isAnchor == USERISANCHOR)
            return true;
        else if(isAnchor == USERNOTANCHOR)
            return false;
        return false;
    }

    @Override
    public String toString() {
        return "AudienceInfo{" +
                " uid = " + uid +
                " name = " + name +
                " portraitUrl = " + portraitUrl +
                " portraitIndex = " + portraitIndex +
                " nobleLevel = " + nobleLevel +
                " guardianLevel = " + guardianLevel +
                " isAnchor = " + isAnchor +
                '}';
    }

    @Override
    public int hashCode() {
        return Long.valueOf(uid).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudienceInfo audienceInfo = (AudienceInfo) o;
        if (uid != audienceInfo.uid){
            return false;
        }
        return true;
    }
}
