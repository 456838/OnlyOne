package com.yy.live.model.bean.core;

import java.io.Serializable;

public class MyChannelInfo implements Serializable {
    public int topSid;     //频道的父频道号
    public int asid;    //频道短位号
    public int subSid;     //频道id号
    public int users;   //频道中的人数
    public int owner;   //频道的ow的uid
    public int sex;     //暂时未提供
    public int roler;   //自己在频道中的角色
    public int type; //频道的类型
    public String channelNick;      //频道昵称
    public String logo;     //频道的logo
    public String templateid;   //模板id
    public boolean isLiving;

    public MyChannelInfo() {
    }

    public MyChannelInfo(int topSid, int asid, int subSid, int users, int owner, int sex, int roler, int type, String channelNick, String logo, String templateid, boolean isLiving) {
        this.topSid = topSid;
        this.asid = asid;
        this.subSid = subSid;
        this.users = users;
        this.owner = owner;
        this.sex = sex;
        this.roler = roler;
        this.type = type;
        this.channelNick = channelNick;
        this.logo = logo;
        this.templateid = templateid;
        this.isLiving = isLiving;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MyChannelInfo{");
        sb.append("subSid=").append(subSid);
        sb.append(", asid=").append(asid);
        sb.append(", topSid=").append(topSid);
        sb.append(", users=").append(users);
        sb.append(", owner=").append(owner);
        sb.append(", sex=").append(sex);
        sb.append(", roler=").append(roler);
        sb.append(", type=").append(type);
        sb.append(", channelNick='").append(channelNick).append('\'');
        sb.append(", logo='").append(logo).append('\'');
        sb.append(", templateid='").append(templateid).append('\'');
        sb.append(", isLiving=").append(isLiving);
        sb.append('}');
        return sb.toString();
    }
}
