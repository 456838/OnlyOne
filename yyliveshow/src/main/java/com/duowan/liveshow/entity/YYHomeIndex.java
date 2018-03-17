package com.duowan.liveshow.entity;

import java.io.Serializable;
import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/2 18:56
 * Time: 18:56
 * Description:
 */
public class YYHomeIndex {


    private int code;
    private String message;
    private List<DataBeanX> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {

        private int id;
        private String name;
        private int type;
        private String url;
        private int icon;
        private int head;
        private int pageable;
        private int serv;
        private int tagType;
        private int duplicate;
        private int recommend;
        private int showImpress;
        private int topContentBanner;
        private List<DataBean> data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public int getHead() {
            return head;
        }

        public void setHead(int head) {
            this.head = head;
        }

        public int getPageable() {
            return pageable;
        }

        public void setPageable(int pageable) {
            this.pageable = pageable;
        }

        public int getServ() {
            return serv;
        }

        public void setServ(int serv) {
            this.serv = serv;
        }

        public int getTagType() {
            return tagType;
        }

        public void setTagType(int tagType) {
            this.tagType = tagType;
        }

        public int getDuplicate() {
            return duplicate;
        }

        public void setDuplicate(int duplicate) {
            this.duplicate = duplicate;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
        }

        public int getShowImpress() {
            return showImpress;
        }

        public void setShowImpress(int showImpress) {
            this.showImpress = showImpress;
        }

        public int getTopContentBanner() {
            return topContentBanner;
        }

        public void setTopContentBanner(int topContentBanner) {
            this.topContentBanner = topContentBanner;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{

            private int sort;
            private String biz;
            private int type;
            private int uid;
            private long sid;
            private long ssid;
            private String name;
            private String desc;
            private int startTime;
            private int liveTime;
            private String thumb;
            private int users;
            private int fans;
            private String tag;
            private int tagStyle;
            private int tpl;
            private int yyNum;
            private String thumb2;
            private String snapshot;
            private int ru;
            private int linkMic;
            private int distance;
            private String avatar;
            private String pid;
            private String token;
            private String site;
            private int verify;
            private int totalUser;
            private int totalLike;
            private int quality;
            private int deviceType;
            private int groupId;

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getBiz() {
                return biz;
            }

            public void setBiz(String biz) {
                this.biz = biz;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public long getSid() {
                return sid;
            }

            public void setSid(long sid) {
                this.sid = sid;
            }

            public long getSsid() {
                return ssid;
            }

            public void setSsid(long ssid) {
                this.ssid = ssid;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getStartTime() {
                return startTime;
            }

            public void setStartTime(int startTime) {
                this.startTime = startTime;
            }

            public int getLiveTime() {
                return liveTime;
            }

            public void setLiveTime(int liveTime) {
                this.liveTime = liveTime;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public int getUsers() {
                return users;
            }

            public void setUsers(int users) {
                this.users = users;
            }

            public String getTag() {
                return tag;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public int getTagStyle() {
                return tagStyle;
            }

            public void setTagStyle(int tagStyle) {
                this.tagStyle = tagStyle;
            }

            public int getTpl() {
                return tpl;
            }

            public void setTpl(int tpl) {
                this.tpl = tpl;
            }

            public int getYyNum() {
                return yyNum;
            }

            public void setYyNum(int yyNum) {
                this.yyNum = yyNum;
            }

            public String getThumb2() {
                return thumb2;
            }

            public void setThumb2(String thumb2) {
                this.thumb2 = thumb2;
            }

            public String getSnapshot() {
                return snapshot;
            }

            public void setSnapshot(String snapshot) {
                this.snapshot = snapshot;
            }

            public int getRu() {
                return ru;
            }

            public void setRu(int ru) {
                this.ru = ru;
            }

            public int getLinkMic() {
                return linkMic;
            }

            public void setLinkMic(int linkMic) {
                this.linkMic = linkMic;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }

            public int getVerify() {
                return verify;
            }

            public void setVerify(int verify) {
                this.verify = verify;
            }

            public int getTotalUser() {
                return totalUser;
            }

            public void setTotalUser(int totalUser) {
                this.totalUser = totalUser;
            }

            public int getTotalLike() {
                return totalLike;
            }

            public void setTotalLike(int totalLike) {
                this.totalLike = totalLike;
            }

            public int getQuality() {
                return quality;
            }

            public void setQuality(int quality) {
                this.quality = quality;
            }

            public int getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(int deviceType) {
                this.deviceType = deviceType;
            }

            public int getGroupId() {
                return groupId;
            }

            public void setGroupId(int groupId) {
                this.groupId = groupId;
            }
        }
    }
}
