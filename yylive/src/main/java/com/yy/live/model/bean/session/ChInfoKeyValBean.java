package com.yy.live.model.bean.session;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/21 19:32
 * Time: 19:32
 * Description:
 */
public class ChInfoKeyValBean extends SessionBase {

    public String chSid;        //此次指的是子频道
    public String chName;
    public String chStyle;
    public String chHasPassword;
    public String chLogo;
    public String chPid;
    public String chTemplateId;
    public String chOrder;
    public String chJieDai;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChInfoKeyValBean{");
        sb.append("chSid='").append(chSid).append('\'');
        sb.append(", chName='").append(chName).append('\'');
        sb.append(", chStyle='").append(chStyle).append('\'');
        sb.append(", chHasPassword='").append(chHasPassword).append('\'');
        sb.append(", chLogo='").append(chLogo).append('\'');
        sb.append(", chPid='").append(chPid).append('\'');
        sb.append(", chTemplateId='").append(chTemplateId).append('\'');
        sb.append(", chOrder='").append(chOrder).append('\'');
        sb.append(", chJieDai='").append(chJieDai).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public ChInfoKeyValBean(String chSid, String chName, String chStyle, String chHasPassword, String chLogo, String chPid, String chTemplateId, String chOrder, String chJieDai) {
        this.chSid = chSid;
        this.chName = chName;
        this.chStyle = chStyle;
        this.chHasPassword = chHasPassword;
        this.chLogo = chLogo;
        this.chPid = chPid;
        this.chTemplateId = chTemplateId;
        this.chOrder = chOrder;
        this.chJieDai = chJieDai;
    }

    public ChInfoKeyValBean() {
    }
}
