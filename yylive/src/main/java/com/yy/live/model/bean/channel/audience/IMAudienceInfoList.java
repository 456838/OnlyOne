package com.yy.live.model.bean.channel.audience;

import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/26 19:50
 * Time: 19:50
 * Description:
 */
public class IMAudienceInfoList<T> {
    private String context;
    private List<T> audienceList;

    public IMAudienceInfoList(String context, List<T> audienceList) {
        this.context = context;
        this.audienceList = audienceList;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public List<T> getAudienceList() {
        return audienceList;
    }

    public void setAudienceList(List<T> audienceList) {
        this.audienceList = audienceList;
    }
}
