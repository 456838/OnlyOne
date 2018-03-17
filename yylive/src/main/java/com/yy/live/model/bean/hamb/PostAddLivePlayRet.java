package com.yy.live.model.bean.hamb;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/5 18:43
 * Time: 18:43
 * Description:
 */
public class PostAddLivePlayRet {


    /**
     * id : 427
     * meta : {"id":"27000","period":"其他类型","plot":["欢脱/搞笑"],"title":"越来尸恋","category":"普通本","author":"千落","cover":"http://yycloudvod1285246627.bs2dl.yy.com/djkxYWFhOTZhOTE3YmYxZTY3ZjRkNzFkNjk1YTY4NGJiMTY1NjA2MjU4","digest":"欢脱/搞笑","createdat":"2016-11-01 14:19:17","updatedat":"2017-03-15 22:09:51","length":6537,"maleRoleCount":1,"femaleRoleCount":1,"collectionCount":2,"bgm":[{"name":"宫灯帏","url":""}]}
     * topChannel : 123
     * subChannel : 123
     * sponsorId : 123
     * startTime : 2017-04-05 18:40:12
     * endTime :
     */

    private String id;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PostAddLivePlayRet{");
        sb.append("id='").append(id).append('\'');
        sb.append(", meta=").append(meta);
        sb.append(", topChannel=").append(topChannel);
        sb.append(", subChannel=").append(subChannel);
        sb.append(", sponsorId=").append(sponsorId);
        sb.append(", startTime='").append(startTime).append('\'');
        sb.append(", endTime='").append(endTime).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * id : 27000
     * period : 其他类型
     * plot : ["欢脱/搞笑"]
     * title : 越来尸恋
     * category : 普通本
     * author : 千落
     * cover : http://yycloudvod1285246627.bs2dl.yy.com/djkxYWFhOTZhOTE3YmYxZTY3ZjRkNzFkNjk1YTY4NGJiMTY1NjA2MjU4
     * digest : 欢脱/搞笑
     * createdat : 2016-11-01 14:19:17
     * updatedat : 2017-03-15 22:09:51
     * length : 6537
     * maleRoleCount : 1
     * femaleRoleCount : 1
     * collectionCount : 2
     * bgm : [{"name":"宫灯帏","url":""}]
     */



    private MetaBean meta;
    private int topChannel;
    private int subChannel;
    private int sponsorId;
    private String startTime;
    private String endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public int getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(int topChannel) {
        this.topChannel = topChannel;
    }

    public int getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(int subChannel) {
        this.subChannel = subChannel;
    }

    public int getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(int sponsorId) {
        this.sponsorId = sponsorId;
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


}
