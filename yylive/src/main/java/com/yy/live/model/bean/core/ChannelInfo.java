package com.yy.live.model.bean.core;

import java.io.Serializable;
import java.util.List;

public class ChannelInfo implements Serializable {
	
    public static final String TOP_ASID_FIELD = "topASid";
    public static final String TOP_SID_FIELD = "topSid";
	public static final String SUB_SID_FIELD = "subSid";
	public static final String CHANNEL_TOP_NAME_FIELD = "channelTopName";
	public static final String CHANNEL_NAME_FIELD = "channelName";
	public static final String CHANNEL_LOGO_FIELD = "channelLogo";
	public static final String CHANNEL_TYPE_FIELD = "channelType";
	public static final String CHANNEL_MODE_FIELD = "channelMode";
	public static final String HASPASSWD_FIELD = "hasPassword";
	public static final String PARENT_SID_FIELD = "parentSid";
	public static final String CHINFO_CHANNEL_ORDER = "order";


	//频道类型
	public enum ChannelType {
        NULL_TYPE,          //未获取到频道模板信息
	    Base_Type,			//基本模板
	    Ent_Type,			//娱乐模板  16777217：娱乐模板
	    Ent_1931_Type,		//1931模板
        Game_Type,		//游戏模板 67108867：游戏直播
		FRIEND,
		EDUCATION,
		RUI_XUE,
		FINANCE,
		WO_DI,
		CONCERT,
		NEW_1931,
		JU_BA
	}
    
	//频道模式
	public enum ChannelMode {
	    Free_Mode,			//自由模式
	    ADMIN_Mode,		//主席模式
	    MicQueue_Mode		//麦序模式
	}
	
	public String  id;
	
    public long topASid;						//频道顶级频道短号
	
    public long topSid;						//频道顶级频道号
    	
    public long subSid;						//频道子级频道号
	
	public String channelTopName;				//顶级频道名称
    	
	public String channelName;				//频道名称
	
	public String channelLogo;				//频道LOGO
	
	public ChannelType channelType;			//频道类型
	
	public ChannelMode channelMode;			//频道模式
	
	public Boolean hasPassWord;				//频道是否有密码
	
	public long parentSid;					//频道的父级频道
	
	public int order;

	public Boolean isRootChannel;				//是否顶级频道
	public int onlineCount;					//道频道在线人数
	public List<ChannelInfo> subChannelList;		//该频道的子频道列表
    public int guestWaitingTime;                 //游客进频道后允许发言的等待时间
    public int guestMaxLength;                   // 游客最大发言长度
	public boolean forbidGuestSendUrl;               //是否限制发送带url的公聊信息
    public boolean forbidMemberSendUrl;              //会员是否限制发送带url的公聊信息
    public boolean needBindMobile;                   //绑定手机用户才能公屏发言
    public boolean isGuestLimited;                  //是否启用游客限制
    public boolean forbidGuestVoice;                //是否禁止游客语音
    public long enterChannelTime;                   //首次进入频道记录的时间
    public boolean disableAllText ;       // 当前子频道是否禁止所以人打字
    public boolean disableVisitorText;    // 当前子频道是否禁止游客打字
    public boolean isControlMic;          //当前频道管理员是否控麦
    public boolean isDisableMic;          //当前频道管理员是禁麦
    public boolean guestJoinMaixu=true;          //当前频道是否禁止游客抢麦
	public boolean isTxtLimit = false; // 文字聊天是否限制
	public int txtLimitTime; //发言速度限制，每句间隔 单位秒
	public boolean guestTxtLimit = true; //发言速度限制 是否只针对游客
	public boolean is1931Type = false;//设置是否1931专属小类
	public String templateid;  //模板id 以频道 templateid为住

	/**
	 * 构造函数
	 */
	public ChannelInfo() {
	    reset();
	}
	
	public void reset() {
	    topASid = 0;
	    topSid = 0;
		subSid = 0;
		channelName = "";
		channelTopName = "";
		channelLogo = "";
		channelType = ChannelType.NULL_TYPE;
		channelMode = ChannelMode.MicQueue_Mode;
		onlineCount = 0;
		isRootChannel = false;
		hasPassWord = false;
		subChannelList = null;
		parentSid = 0;
		order = 0;
        guestWaitingTime = 0;
        guestMaxLength = 0;
        forbidGuestSendUrl = false;
        forbidMemberSendUrl = false;
        needBindMobile = false;
        isGuestLimited = false;
        forbidGuestVoice = false;
        enterChannelTime=0;
        disableAllText=false;
        disableVisitorText=false;
        isControlMic=false;
        isDisableMic=false;
        guestJoinMaixu=true;
        isTxtLimit = false;
        txtLimitTime = 0;
        guestTxtLimit = true;
		is1931Type=false;
		templateid=null;
	}
	
	public String getId () {
		if (id == null) {
			id = topSid + " " + subSid;
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * 0 表示顶级频道
	 * 1 表示子频道
	 * 2 表示二级子频道
	 * -1 表示没有查询到频道信息
	 * @return
	 */

	public int channelOrderType(){
		if (topSid!=0){
			if (parentSid==0&&isRootChannel){
				return 0;
			}else  if (parentSid==topSid){
				return 1;
			}else {
				return 2;
			}
		}else {
			return -1;
		}
	}

    @Override
    public String toString() {
        return "[topASid = " + topASid +
                "; topSid = " + topSid +
                "; subSid = " + subSid +
                "; channelTopName = " + channelTopName +
                "; channelName = " + channelName +
                "; channelType = " + channelType +
                "; channelMode = " + channelMode +
                "; isRootChannel = " + isRootChannel +
                "; hasPassWord = " + hasPassWord +
                "; logo = " + channelLogo +
                "; guestWaitingTime = " + guestWaitingTime +
                "; guestMaxLength = " + guestMaxLength +
                "; needBindMobile = " + needBindMobile +
                "; forbidGuestSendUrl = " + forbidGuestSendUrl +
                "; forbidMemberSendUrl = " + forbidMemberSendUrl +
				"; forbidGuestVoice = " + forbidGuestVoice +
				"; disableAllText = " + disableAllText +
				"; disableVisitorText = " + disableVisitorText +
                "; isGuestLimited = " + isGuestLimited +
                "; isControlMic = " + isControlMic +
                "; isDisableMic = " + isDisableMic +
                "; guestJoinMaixu = " + guestJoinMaixu +
				"; is1931Type = " + is1931Type +
				"; templateid = " + templateid +
				"; parentSid = " + parentSid +
                " ]";
    }

}
