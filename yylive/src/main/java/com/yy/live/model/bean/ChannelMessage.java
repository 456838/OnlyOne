package com.yy.live.model.bean;

import android.text.SpannableStringBuilder;

public class ChannelMessage {
//    public static final String nobleCode = "[noblelv]";             //贵族占位符
//    public static final String trueloveCode = "[truelove]";         //珍爱占位符
//    public static final String knightCode = "[knight]";             //骑士占位符,活动勋章公用骑士勋章位置，优先显示活动勋章
//    public static final String lftCode = "[lft]";                  //转盘乐翻天
//    public static final String roleLevelCode = "[role]";           //马甲等级
//    public static final int nickLengthLimit = 10;                   //昵称长度限制
//    public static final String nickColor = "#f1b168";           //橙  昵称颜色
//    public static final String giftTxtColor = "#ff5555";        //红  礼物文案颜色
//    public static final String noticeColor = "#3ed161";         //绿  通知颜色
//    public static final String contentColor = "#3ed161";         //绿  通知颜色
//    public static final int NOBLEEMOTION_MESSAGE_TYPE = 1000;//贵族消息类型
//    public static final int NOTICE_MESSAGE_TYPE = 110;//通知消息类型
//    public static final int TURNTABLE_MESSAGE_TYPE = 109;//大转盘消息类型
//    public static final int SHARE_MESSAGE_TYPE = 108;//分享消息类型
//    public static final int SUBSCRIBE_MESSAGE_TYPE = 107;//关注消息类型
//    public static final int CUSTOMS_MESSAGE_TYPE = 77;//自定义消息类型
//    public static final int TURE_LOVE_TYPE = 1550;//珍爱首送消息类型
//
//    public static final int HIGHT_PRIORITY = 3;
//    public static final int COMMON_PRIORITY = 2;
//    public static final int LOW_PRIORITY = 1;

    public String nickname = "";//昵称
    public String text = "";//文字
//    public String pureText = "";//纯发送内容
    public long uid;//uid
    public long sid = 0;//当前频道id
    /**
     * 爵位类别
     */
//    public int nobleLevel;
//    public int roleLevel;
//    public int knightLevel;
//    public String trueloveMedal = "";
//    public int trueLoveLevel = 0;//珍爱等级
//    public String avatarUrl = "";//手机开播的url头像
//    public String gifUri = "";//扩展的gifuir 属性
//    public int channel_message_type = 0;//消息类型
//    public boolean isReplay = false;//是否回放信息
    public SpannableStringBuilder spannable = null;
//    public StringBuilder medals = new StringBuilder();
//    public StringBuilder tail = new StringBuilder();
//    public int priority = 3;                //优先级   3-高  2-中 1-低
//    public WeakReference<TextView> spannableTarget;


    public String scenarioInfo;

    public ChannelMessage() {
    }

    public ChannelMessage(ChannelMessage msg) {
        if (msg != null) {
            this.nickname = msg.nickname;
            this.text = msg.text;
//            this.pureText = msg.pureText;
            this.uid = msg.uid;
            this.sid = msg.sid;
//            this.nobleLevel = msg.nobleLevel;
//            this.roleLevel = msg.roleLevel;
//            this.knightLevel = msg.knightLevel;
//            this.trueloveMedal = msg.trueloveMedal;
//            this.trueLoveLevel = msg.trueLoveLevel;
//            this.avatarUrl = msg.avatarUrl;
//            this.gifUri = msg.gifUri;
//            this.channel_message_type = msg.channel_message_type;
//            this.isReplay = msg.isReplay;
//            this.spannable = msg.spannable;
//            this.medals = msg.medals;
//            this.tail = msg.tail;
//            this.priority = msg.priority;
//            this.spannableTarget = msg.spannableTarget;
        }
    }

    @Override
    public String toString() {
        return "ChannelMessage{" +
                "nickname='" + nickname + '\'' +
                ", text='" + text + '\'' +
                ", uid=" + uid +
                ", subSid=" + sid +
//                ", nobleLevel=" + nobleLevel +
//                ", channel_message_type=" + channel_message_type +
//                ", gifUri=" + gifUri +
//                ", avatarUrl = " + avatarUrl +
//                ", spannable = " + spannable +
                '}';
    }
}
