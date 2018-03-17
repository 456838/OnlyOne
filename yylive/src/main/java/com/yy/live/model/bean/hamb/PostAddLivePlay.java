package com.yy.live.model.bean.hamb;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/5 18:41
 * Time: 18:41
 * Description:
 */
public class PostAddLivePlay {

    /**
     * ScenarioId : 27000
     * ChannelId : 123
     * SubChannelId : 123
     * SponsorId : 123
     */

    private int ScenarioId;
    private long ChannelId;
    private long SubChannelId;
    private int SponsorId;

    public int getScenarioId() {
        return ScenarioId;
    }

    public void setScenarioId(int ScenarioId) {
        this.ScenarioId = ScenarioId;
    }

    public long getChannelId() {
        return ChannelId;
    }

    public void setChannelId(int ChannelId) {
        this.ChannelId = ChannelId;
    }

    public long getSubChannelId() {
        return SubChannelId;
    }

    public void setSubChannelId(int SubChannelId) {
        this.SubChannelId = SubChannelId;
    }

    public int getSponsorId() {
        return SponsorId;
    }

    public void setSponsorId(int SponsorId) {
        this.SponsorId = SponsorId;
    }

    public PostAddLivePlay(int scenarioId, long channelId, long subChannelId, int sponsorId) {
        ScenarioId = scenarioId;
        ChannelId = channelId;
        SubChannelId = subChannelId;
        SponsorId = sponsorId;
    }
}
