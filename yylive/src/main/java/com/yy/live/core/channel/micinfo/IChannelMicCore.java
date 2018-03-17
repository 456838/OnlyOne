package com.yy.live.core.channel.micinfo;

import com.yy.live.core.IBaseCore;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/23 19:45
 * Time: 19:45
 * Description:
 */
public interface IChannelMicCore extends IBaseCore {

    //获取首麦信息;对象MicTopInfo
    public MicTopInfo getTopMicInfo();

    //查询麦序信息
    public List<MicTopInfo> getMicQueueListInfo();

    //查询麦序信息
    public List<Long> getMicUidListInfo();

}
