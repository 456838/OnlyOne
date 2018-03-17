package com.yy.live.model.bean.hamb;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/5 14:56
 * Time: 14:56
 * Description:
 */
public class PostLivePlayRet {


    /**
     * id : 47
     * topChannel : 29019838
     * subChannel : 29019838
     * startTime : 2017-04-04 17:16:50
     * endTime : 0000-00-00 00:00:00
     * sponsorId : 1554329742
     * meta : {"id":"22575","period":"古风/仙侠","plot":["甜蜜/温情"],"title":"【剑三同人】搞笑耽美本","category":"普通本","author":"安宁","cover":"http://yycloudvod1285246627.bs2dl.yy.com/djk3MGYxMTlkOTFhN2M1OGUxNmE3NTUxNzcwMDlhY2JkMTY1NTgxMjIzMg","digest":"甜蜜/温情 欢脱/搞笑","createdat":"2016-11-01 14:12:22","updatedat":"2017-04-04 00:03:32","length":3177,"maleRoleCount":4,"femaleRoleCount":3,"collectionCount":0,"bgm":[{"name":"猪八戒背媳妇-纯音乐版，仙境传说、背景音乐-欢快，欢快中国风 游戏 热血江湖背景音乐","url":""}]}
     */

    private String id;
    private long topChannel;
    private long subChannel;
    private String startTime;
    private String endTime;
    private int sponsorId;
    private int onlinePeople;

    public int getOnlinePeople() {
        return onlinePeople;
    }

    public void setOnlinePeople(int onlinePeople) {
        this.onlinePeople = onlinePeople;
    }

    /**
     * id : 22575
     * period : 古风/仙侠
     * plot : ["甜蜜/温情"]
     * title : 【剑三同人】搞笑耽美本
     * category : 普通本
     * author : 安宁
     * cover : http://yycloudvod1285246627.bs2dl.yy.com/djk3MGYxMTlkOTFhN2M1OGUxNmE3NTUxNzcwMDlhY2JkMTY1NTgxMjIzMg
     * digest : 甜蜜/温情 欢脱/搞笑
     * createdat : 2016-11-01 14:12:22
     * updatedat : 2017-04-04 00:03:32
     * length : 3177
     * maleRoleCount : 4
     * femaleRoleCount : 3
     * collectionCount : 0
     * bgm : [{"name":"猪八戒背媳妇-纯音乐版，仙境传说、背景音乐-欢快，欢快中国风 游戏 热血江湖背景音乐","url":""}]
     */

    private MetaBean meta;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(long topChannel) {
        this.topChannel = topChannel;
    }

    public long getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(long subChannel) {
        this.subChannel = subChannel;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PostLivePlayRet{");
        sb.append("id='").append(id).append('\'');
        sb.append(", topChannel=").append(topChannel);
        sb.append(", subChannel=").append(subChannel);
        sb.append(", startTime='").append(startTime).append('\'');
        sb.append(", endTime='").append(endTime).append('\'');
        sb.append(", sponsorId=").append(sponsorId);
        sb.append(", meta=").append(meta);
        sb.append('}');
        return sb.toString();
    }

}
