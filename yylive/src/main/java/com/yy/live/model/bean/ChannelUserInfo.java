package com.yy.live.model.bean;


import com.salton123.util.LogUtils;
import com.yy.live.model.UserInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/22.
 */
public class ChannelUserInfo  extends UserInfo implements Serializable {
    public Map<Long,Integer> channelRolerMap=new HashMap<Long, Integer>();

    @Override
    public String toString() {
        return "ChannelUserInfo{" +
                "userId=" + userId +
                ", yyId=" + yyId +
                ", nickName='" + nickName + '\'' +
                //", stageName='" + stageName + '\'' +
                ", signature='" + signature + '\'' +
                ", gender=" + gender +
                ", iconIndex=" + iconIndex +
                ", iconUrl='" + iconUrl + '\'' +
                ", iconUrl_100_100='" + iconUrl_100_100 + '\'' +
                ", iconUrl_144_144='" + iconUrl_144_144 + '\'' +
                ", iconUrl_640_640='" + iconUrl_640_640 + '\'' +
                ", credits=" + credits +
                ", flowerNum=" + flowerNum +
                ", birthday=" + birthday +
                ", area=" + area +
                ", province=" + province +
                ", city=" + city +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                ", reserve3='" + reserve3 + '\'' +
                ", reserve4='" + reserve4 + '\'' +
                '}';
    }

    //表示是否是游客
    public  boolean isChannelGuest(long topSid,long subSid){
        Integer role=channelRolerMap.get(subSid);
        if (role!=null){
            LogUtils.d("ChannelUserInfo", "isChannelGuest subSid:role = " + role);
            if (role== MemberType.NUL_ROLE){
                return true;
            }else if (role== MemberType.VISITOR){
                return true;
            }else if (role== MemberType.NORMAL){
                return true;
            }else if (role== MemberType.DELETED){
                return true;
            }else {
                return false;
            }
        }else {
            role=channelRolerMap.get(topSid);
            if (role!=null){
                LogUtils.d("ChannelUserInfo", "isChannelGuest topSid:role = " + role);
                if (role== MemberType.NUL_ROLE){
                    return true;
                }else if (role== MemberType.VISITOR){
                    return true;
                }else if (role== MemberType.NORMAL){
                    return true;
                }else if (role== MemberType.DELETED){
                    return true;
                }else {
                    return false;
                }
            }else{
                return true;
            }
        }

    }

    //表示是否是会员
    public  boolean isChannelMember(long topSid,long subSid){
        Integer role=channelRolerMap.get(subSid);
        if (role!=null){
            LogUtils.d("ChannelUserInfo", "isChannelMember subSid:role = " + role);
            if (role== MemberType.MEMBER){
                return true;
            }else if (role== MemberType.TMPVIP){
                return true;
            }else if (role== MemberType.VIP){
                return true;
            }else {
                return false;
            }
        }else {
            role=channelRolerMap.get(topSid);
            if (role!=null){
                LogUtils.d("ChannelUserInfo", "isChannelMember topSid:role = " + role);
                if (role== MemberType.MEMBER){
                    return true;
                }else if (role== MemberType.TMPVIP){
                    return true;
                }else if (role== MemberType.VIP){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    //表示是否是管理员权限以上
    public  boolean isChannelAdmin(long topSid,long subSid){
        Integer role=channelRolerMap.get(subSid);
        if (role!=null){
            LogUtils.d("ChannelUserInfo", "isChannelAdmin subSid:role = " + role);
            if (role>= MemberType.CMANAGER){
                return true;
            }else {
                return false;
            }
        }else {
            role=channelRolerMap.get(topSid);
            if (role!=null){
                LogUtils.d("ChannelUserInfo", "isChannelAdmin topSid:role = " + role);
                if (role>= MemberType.CMANAGER){
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
    }

    //表示是否是网监
    public  boolean isChannelPOLICE(long topSid,long subSid){
       /* Integer role=channelRolerMap.get(0L);
        if (role!=null){
            MLog.debug("ChannelUserInfo", "isChannelPOLICE subSid =0 :role = " + role);
            if (role== MemberType.POLICE){
                return true;
            }else {
                return false;
            }
        }else {
            role = channelRolerMap.get(subSid);
            if (role != null) {
                MLog.debug("ChannelUserInfo", "isChannelPOLICE subSid:role = " + role);
                if (role == MemberType.POLICE) {
                    return true;
                } else {
                    return false;
                }
            } else {
                role = channelRolerMap.get(topSid);
                if (role != null) {
                    MLog.debug("ChannelUserInfo", "isChannelPOLICE topSid:role = " + role);
                    if (role == MemberType.POLICE) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }*/
        return false;
    }


    public class MemberType {
        public static final int NUL_ROLE = 0;
        public static final int VISITOR = 20; // VISITOR
        public static final int NORMAL = 25; // U
        public static final int DELETED = 50; // unuse
        public static final int TMPVIP = 66; // TMPVIP
        public static final int VIP = 88; // VIP
        public static final int MEMBER = 100; // R
        public static final int CMANAGER = 150; // CA
        public static final int PMANAGER = 175; // PA
        public static final int MANANGER = 200; // MAN
        public static final int VICE_OWNER = 230; // VO
        public static final int OWNER = 255; // OW
        public static final int KEFU = 300; // CS
        public static final int POLICE = 400; //
        public static final int SA = 1000; // SA
    }

}
