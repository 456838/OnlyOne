package com.yy.live.model.richtext;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuanxiaoming on 2014/6/20.
 */
public class VipEmoticonFilter extends BaseRichTextFilter {
    /**
     * yy://yyvip-[=大喇叭]
     */

    public static final String PREFIX = "yy://yyvip-";
    public static final String EMOTICON_BEGIN = "[=";
    public static final String EMOTICON_END = "]";
    public static final String VIPEMOTICON_DEFAULT="[会员表情]";
    protected static final String REG_CONTENT = ".*?";
    public static final Pattern EmoticonPattern = getEmoticonPattern();


    @Override
    public void parseSpannable(Context context, Spannable spannable, int maxWidth) {
        parseSpannable(context,spannable,maxWidth,null);
    }

    @Override
    public void parseSpannable(Context context, Spannable spannable, int maxWidth, Object tag) {


    }

    private static String convertTagToReg(String tag) {
        return tag.replace("[", "\\[").replace("]", "\\]");
    }

    private static Pattern getEmoticonPattern(){
        StringBuilder reg = new StringBuilder(PREFIX);
        reg.append(convertTagToReg(EMOTICON_BEGIN));
        reg.append(convertTagToReg(REG_CONTENT));
        reg.append(convertTagToReg(EMOTICON_END));
        return Pattern.compile(reg.toString());
    }


    /**
     * 判断消息是含有vip表情的代码
     */
    public static boolean isEmojiMessage(String str){
        return EmoticonPattern.matcher(str).find();
    }


    /**
     * 检测str中是否含Vip表情代码，
     * 如果有，且只包含表情，层处理为默认字符串 [会员表情]
     * 如果有，文字与会员表情混合发布时，只将表情过滤掉，文字保留下来
     * @param message 待检测的字符串
     * @param givenStr 待替换的字符串
     */
    public static String replaceVipEmoticonWithGivenStr(String message, String givenStr) {
        //MLog.debug("VipEmoticonFilter start "," message = "+message);
        if (!isEmojiMessage(message)){
            return message;
        }
        // 清除掉所有特殊字符
        Matcher m = EmoticonPattern.matcher(message);
        String messageReplce= m.replaceAll(givenStr).trim();
        //查询是否只有会员表情
        messageReplce =messageReplce.replaceAll(convertTagToReg(givenStr),"").trim();
        if (TextUtils.isEmpty(messageReplce)){
            messageReplce=givenStr;
        }
       // MLog.debug("VipEmoticonFilter end"," messageReplce = "+messageReplce);
        return messageReplce;
    }

}
