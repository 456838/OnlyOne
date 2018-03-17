package com.yy.live.model.bean.media;


import java.util.*;

public class MicSpeaker {

    private long uid;
    private String nick;
    private long muteTime;
    private long speakTime;
    private String headUrl;
    private boolean isSpeaking;
    private boolean isAuchor;

    /**
     * Last speaking time.
     */

    public MicSpeaker(long usrid, String nickname, String url) {
        uid = usrid;
        nick = nickname;
        headUrl = url;
        muteTime = 0;
        speakTime = 0;
        isSpeaking = false;
        isAuchor=false;
    }

    public long uid() {
        return uid;
    }

    public String nickname() {
        return nick;
    }

    public String headUrl() {
        return headUrl;
    }

    public void setHeadUrl(String url) {
        headUrl = url;
    }

    public void setNick(String nickName) {
        nick = nickName;
    }

    public boolean isSpeaking() {
        return isSpeaking;
    }

    public boolean isAuchor() {
        return isAuchor;
    }

    public void setAuchor(boolean isAuchor) {
        this.isAuchor = isAuchor;
    }

    public long  getSpeakTime(){
        return speakTime;
    }

    public long getLastMuteTime() {
        return muteTime;
    }

    public MicSpeaker stopSpeak() {
        if (isSpeaking) {
            isSpeaking = false;
        }
        muteTime = System.currentTimeMillis();
        //MLog.debug("xiaoming", "%d stop speak and stopTime is %d ", uid,muteTime);
        return this;
    }

    public MicSpeaker startSpeak() {
        if (!isSpeaking) {
            isSpeaking = true;
        }
        speakTime = System.currentTimeMillis();
        return this;
    }


    @Override
    public String toString() {
        return String.valueOf(uid) + nick + headUrl + String.valueOf(muteTime) + String.valueOf(speakTime) + String.valueOf(isSpeaking);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MicSpeaker speaker = (MicSpeaker) o;
        if (uid != speaker.uid){
            return false;
        }
        /*if (headUrl != null ? !headUrl.equals(speaker.headUrl) : speaker.headUrl != null){
            return false;
        }
        if (nick != null ? !nick.equals(speaker.nick) : speaker.nick != null){
            return false;
        }
        if (isSpeaking != speaker.isSpeaking){
            return false;
        }*/
        return true;
    }

    @Override
    public int hashCode() {
        //int result = uid;
        /* result = 31 * result + (nick != null ? nick.hashCode() : 0);
        result = 31 * result + (headUrl != null ? headUrl.hashCode() : 0);
        result = 31 * result + (isSpeaking ? 1 : 0);*/
        int result= Long.valueOf(uid).hashCode();
        return result;
    }

    public static List<MicSpeaker> listFromMap(Map<Integer, MicSpeaker> map) {
        List<MicSpeaker> list = new ArrayList<MicSpeaker>(map.values());
        return list;
    }

    public static Comparator<MicSpeaker> mMuteTimeComparator = new Comparator<MicSpeaker>() {
        @Override
        public int compare(MicSpeaker a, MicSpeaker b) {
            return Long.signum(b.muteTime - a.muteTime);
        }
    };

    public static Comparator<MicSpeaker> mSpeakTimeComparator = new Comparator<MicSpeaker>() {
        @Override
        public int compare(MicSpeaker a, MicSpeaker b) {
            return Long.signum(b.speakTime - a.speakTime);
        }
    };

    public static MicSpeaker getFurthestSpeaker(List<MicSpeaker> list) {
         if (list!=null&&list.size() == 1) {
             return list.get(0);
         } else if (list!=null&&list.size() > 1) {
            Collections.sort(list, mSpeakTimeComparator);
            return list.get(list.size()-1);
        }
        return null;
    }

    public static MicSpeaker getRecentlySpeaker(List<MicSpeaker> list) {
        if (list!=null&&list.size() == 1) {
            return list.get(0);
        } else if (list!=null&&list.size() > 1) {
            Collections.sort(list, mSpeakTimeComparator);
            return list.get(0);
        }
        return null;
    }


    public static List<MicSpeaker> getNoSpeakerList(List<MicSpeaker> list) {
        List<MicSpeaker> noSpeakList=null;
        if (list!=null&&list.size() > 0) {
            noSpeakList= new ArrayList<MicSpeaker>();
            for (MicSpeaker micSpeaker : list) {
                if (!micSpeaker.isSpeaking()) {
                    noSpeakList.add(micSpeaker);
                }
            }
        }
        return noSpeakList;
    }


    public static MicSpeaker getFurthestNoSpeaker(List<MicSpeaker> list) {
        List<MicSpeaker> noSpeakerList = getNoSpeakerList(list);
        if (noSpeakerList!=null&&noSpeakerList.size() == 1) {
            return noSpeakerList.get(0);
        } else if (noSpeakerList!=null&&noSpeakerList.size() > 1) {
            Collections.sort(noSpeakerList, mMuteTimeComparator);
            return noSpeakerList.get(noSpeakerList.size()-1);
        }
        return null;
    }

    public static MicSpeaker getRecentlyNoSpeaker(List<MicSpeaker> list) {
        List<MicSpeaker> noSpeakerList = getNoSpeakerList(list);
        if (noSpeakerList!=null&&noSpeakerList.size() == 1) {
            return noSpeakerList.get(0);
        } else if (noSpeakerList!=null&&noSpeakerList.size() > 1) {
            Collections.sort(noSpeakerList, mSpeakTimeComparator);
            return noSpeakerList.get(0);
        }
        return null;
    }


    public static List<MicSpeaker> take(int n, List<MicSpeaker> list) {
        Collections.sort(list, mSpeakTimeComparator);
        if (list.size() <= n) {
            return list;
        }

        List<MicSpeaker> speakingList = new ArrayList<MicSpeaker>();
        for (MicSpeaker speaker : list) {
            if (speaker.isSpeaking) {
                speakingList.add(speaker);
            }
        }

        if (speakingList.size() >= n) {
            return speakingList.subList(0, n);
        }
        int complement = n - speakingList.size();
        speakingList.clear();
        int i = 0;
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            MicSpeaker speaker = (MicSpeaker)iterator.next();
            if (speaker.isSpeaking) {
                speakingList.add(speaker);
            } else if (i < complement) {
                speakingList.add(speaker);
                ++i;
            }
        }
        return speakingList;
    }


}
