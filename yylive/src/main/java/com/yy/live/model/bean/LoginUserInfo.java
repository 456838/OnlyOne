package com.yy.live.model.bean;


import com.yy.live.model.UserInfo;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/16
 * Time: 18:56
 * Description: 登录用户信息类
 */
// @Table(name = "LoginUserInfo")
public class LoginUserInfo extends UserInfo {

    // @Column(name = "lastLoginTime")
    public long lastLoginTime; // 用最后登录的时间作为自动登录的账号


    @Override
    public String toString() {
        return "LoginUserInfo{" +
                "lastLoginTime=" + lastLoginTime +
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
}
