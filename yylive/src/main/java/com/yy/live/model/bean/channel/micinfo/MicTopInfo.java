package com.yy.live.model.bean.channel.micinfo;


import com.yy.live.model.bean.channel.audience.AudienceInfo;

/**
 * Created by dexian on 2014/8/18.
 */
public class MicTopInfo extends AudienceInfo {

    //便于以后扩展
    public static final long MAI_XUN_TEXT_ID = -1; //表示显示 ：“麦序”字的一项的uid
    public static final long MAI_AUDIENCE_TEXT_ID = -2;//表示显示 ：“观众”字的一项的uid
    public int time = 0;//麦序发言倒计时
    public boolean isSpeaking = false;//是否正在讲话
    public boolean ismultiMic = false;//是否连麦
    public boolean is_actual_time_name = false;//是否实时昵称

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }


    @Override
    public String toString() {
        return "MicTopInfo{" +
                " uid = " + uid +
                " name = " + name +
                " ismultiMic=" + ismultiMic +
                " isSpeaking=" + isSpeaking +
                " time=" + time +
                " portraitUrl = " + portraitUrl +
                " portraitIndex = " + portraitIndex +
                " nobleLevel = " + nobleLevel +
                " guardianLevel = " + guardianLevel +
                " isAnchor = " + isAnchor +
                " is_actual_time_name = " + is_actual_time_name +
                '}';
    }
}
