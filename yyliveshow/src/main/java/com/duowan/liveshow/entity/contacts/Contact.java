package com.duowan.liveshow.entity.contacts;


import com.salton123.util.HanziToPinyin;

/**
 * @author xiaobo.cui 2014年11月24日 下午5:36:29
 */
public class Contact {

    /**
     * 显示数据拼音的首字母
     */
    public String sortLetters;
    private String pinyin;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long size = 0;//单位m
    public String musicFilePath = "";
    public String musicName;
    public String musicSinger;
    public String simpleNumber;
    public String sortKey;

    public Contact() {

    }

    public Contact(String musicName, String musicSinger, String sortKey) {
        this.musicName = musicName;
        this.musicSinger = musicSinger;
        this.sortKey = sortKey;
        setPinyin(HanziToPinyin.getPinYin(sortKey));
        if (musicSinger != null) {
            this.simpleNumber = musicSinger.replaceAll("\\-|\\s", "");
        }
    }


    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
        String first = pinyin.substring(0, 1);
        if (first.matches("[A-Za-z]")) {
            sortLetters = first.toUpperCase().charAt(0) + "";
        } else {
            sortLetters = "#";
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((musicName == null) ? 0 : musicName.hashCode());
        result = prime * result + ((musicSinger == null) ? 0 : musicSinger.hashCode());
        result = prime * result + ((sortKey == null) ? 0 : sortKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        if (musicName == null) {
            if (other.musicName != null)
                return false;
        } else if (!musicName.equals(other.musicName))
            return false;
        if (musicSinger == null) {
            if (other.musicSinger != null)
                return false;
        } else if (!musicSinger.equals(other.musicSinger))
            return false;
        if (sortKey == null) {
            if (other.sortKey != null)
                return false;
        } else if (!sortKey.equals(other.sortKey))
            return false;
        return true;
    }

}
