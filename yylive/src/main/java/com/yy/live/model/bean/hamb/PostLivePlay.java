package com.yy.live.model.bean.hamb;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/5 15:01
 * Time: 15:01
 * Description:
 */
public class PostLivePlay {

    /**
     * topChannel : 29019838
     * subChannel : 29019838
     */

    private long topChannel;
    private long subChannel;

    public long getTopChannel() {
        return topChannel;
    }

    public void setTopChannel(int topChannel) {
        this.topChannel = topChannel;
    }

    public long getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(int subChannel) {
        this.subChannel = subChannel;
    }

    public PostLivePlay(long topChannel, long subChannel) {
        this.topChannel = topChannel;
        this.subChannel = subChannel;
    }
}
