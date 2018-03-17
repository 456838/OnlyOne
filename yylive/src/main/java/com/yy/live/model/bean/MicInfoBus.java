package com.yy.live.model.bean;

import com.medialib.video.MediaVideoMsg;
import com.yy.live.model.bean.channel.micinfo.MicTopInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/6 14:47
 * Time: 14:47
 * Description:
 */
public class MicInfoBus {
    public List<MicTopInfo> micTopInfos = new ArrayList<>();        //麦序列表用户信息集
    public long firstMicUid;            //首麦用户uid
    public int currentMicState = MediaVideoMsg.MicStateInfo.Close;      //当前麦状态
    public List<Long> micUidList = new ArrayList<>();       //麦序列表用户uid集
    public List<Long> mutiMicList = new ArrayList<>();      //连麦列表用户uid集

    public boolean disableMic;//禁麦状态,带上马甲权限。如果马甲权限高，禁麦状态也变化

    public boolean isOnMic(long uid) {     //是否在麦序上
        //debug info
        for (Long id : micUidList) {
            System.out.println("MicInfoBus,onMic:" + id);
        }
        return micUidList.contains(uid);
    }

    public boolean isMutiMic(long uid) {     //是否在连麦列表中
        return mutiMicList.contains(uid);
    }


}
