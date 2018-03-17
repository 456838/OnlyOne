/**
 * 
 */
package com.yy.live.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author daixiang
 * 
 */
@DatabaseTable(tableName = "User_UserInfo")
	public class UserInfo implements Serializable {

	public enum Gender {
		Female, Male, Unknown
	}

	public static final String USER_ID_FIELD = "userId";
	public static final String FLOWER_NUM_FIELD = "flowerNum";
	public static final String NICK_NAME_FIELD = "nickName";
	//public static final String STAGE_NAME_FIELD = "stage_name";
	public static final String SIGNATURE_FIELD = "signature";
	public static final String GENDER_FIELD = "gender";
	public static final String ICON_INDEX_FIELD = "iconIndex";
	public static final String ICON_URL_FIELD = "iconUrl";

	public static final String USERINFO_CREDITS = "credits";
	public static final String USERINFO_BIRTHDAY = "birthday";
	public static final String USERINFO_AREA = "area";
	public static final String USERINFO_PROVINCE = "province";
	public static final String USERINFO_CITY = "city";
	public static final String USERINFO_DESC = "description";
	public static final String USERINFO_UPDATETIME = "updatetime";
	
	
	public static final String ICON_60_60="60*60";
	public static final String ICON_100_100="100*100";
	public static final String ICON_144_144="144*144";
	public static final String ICON_640_640="640*640";
	

	@DatabaseField(id = true, columnName = USER_ID_FIELD)
	public long userId; // uid

	@DatabaseField
	public long yyId; // yy号

	@DatabaseField(columnName = NICK_NAME_FIELD)
	public String nickName = ""; // 昵称

	//@DatabaseField(columnName = STAGE_NAME_FIELD)
	//public String stageName; // 艺名

	@DatabaseField(columnName = SIGNATURE_FIELD)
	public String signature = ""; // 签名

	@DatabaseField(columnName = GENDER_FIELD)
	public int gender; // 性别

	@DatabaseField(columnName = ICON_INDEX_FIELD)
	public int iconIndex; // 内置头像，-1是默认值

	@DatabaseField(columnName = ICON_URL_FIELD)
	public String iconUrl = ""; // 头像图片地址

	@DatabaseField
	public String iconUrl_100_100 = "";

	@DatabaseField
	public String iconUrl_144_144 = "";

	@DatabaseField
	public String iconUrl_640_640 = "";

	@DatabaseField
	public int credits; // 积分

	@DatabaseField(columnName = FLOWER_NUM_FIELD)
	public int flowerNum; // 鲜花数

	@DatabaseField
	public int birthday; // 生日

	@DatabaseField
	public int area; // 国家/地区

	public int province = -1; // 省

	public int city = -1; // 市

	@DatabaseField
	public String description = ""; // 个人说明

	@DatabaseField
	public long updateTime; // 更新时间

	public enum OnlineDevice {
		PC, Mobile, PC_Mobile
	}

	public OnlineDevice onlineDevice;

	public enum OnlineState {
		Offline, Invisible, Left, InGame, Busy, Online,
	}

	public OnlineState onlineState;

	@DatabaseField
	public String reserve1;//艺名

	@DatabaseField
	public String reserve2;

	@DatabaseField
	public String reserve3;

	@DatabaseField
	public String reserve4;

	public UserInfo() {
		iconIndex = -1;
	}

    @Override
    public String toString() {
        return "UserInfo{" +
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
                ", onlineDevice=" + onlineDevice +
                ", onlineState=" + onlineState +
                ", reserve1='" + reserve1 + '\'' +
                ", reserve2='" + reserve2 + '\'' +
                ", reserve3='" + reserve3 + '\'' +
                ", reserve4='" + reserve4 + '\'' +
                '}';
    }
}
