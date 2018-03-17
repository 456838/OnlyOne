package com.yy.live.model.bean.hamb;

import java.util.List;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/4/11 14:20
 * Time: 14:20
 * Description:
 */
public class MetaBean {
    private String id;
    private String period;
    private String title;
    private String category;
    private String author;
    private String cover;
    private String digest;
    private String createdat;
    private String updatedat;
    private int length;
    private int maleRoleCount;
    private int femaleRoleCount;
    private int collectionCount;
    private List<String> plot;
    /**
     * name : 宫灯帏
     * url :
     */

    private List<BgmBean> bgm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMaleRoleCount() {
        return maleRoleCount;
    }

    public void setMaleRoleCount(int maleRoleCount) {
        this.maleRoleCount = maleRoleCount;
    }

    public int getFemaleRoleCount() {
        return femaleRoleCount;
    }

    public void setFemaleRoleCount(int femaleRoleCount) {
        this.femaleRoleCount = femaleRoleCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public List<String> getPlot() {
        return plot;
    }

    public void setPlot(List<String> plot) {
        this.plot = plot;
    }

    public List<BgmBean> getBgm() {
        return bgm;
    }

    public void setBgm(List<BgmBean> bgm) {
        this.bgm = bgm;
    }

    public static class BgmBean {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
