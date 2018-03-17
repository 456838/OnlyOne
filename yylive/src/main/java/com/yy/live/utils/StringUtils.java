package com.yy.live.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.duowan.mobile.utils.FP;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Zhanghuiping on 14/6/26.
 */

public class StringUtils {
    public static final boolean IGNORE_CASE = true;
    public static final boolean IGNORE_WIDTH = true;
    /**逗号分隔的数字格式化:"#,###"*/
    public static final DecimalFormat commaSeperateFormat = new DecimalFormat("#,###");

    public static boolean isNullOrEmpty(String str) {
        return FP.empty(str);
    }

    public static boolean isAllWhitespaces(String str) {
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static boolean isAllDigits(String str) {
	    if(isEmpty(str))return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean equal(String s1, String s2) {
        return equal(s1, s2, false);
    }

    public static boolean equal(String s1, String s2, boolean ignoreCase) {
        if (s1 != null && s2 != null) {
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            }
            else {
                return s1.equals(s2);
            }
        }
        else {
            return ((s1 == null && s2 == null) ? true : false);
        }
    }

    public static Vector<String> parseMediaUrls(String str, String beginTag, String endTag) {
        Vector<String> list = new Vector<String>();
        if (!isNullOrEmpty(str)) {
            int beginIndex = str.indexOf(beginTag, 0);
            int endIndex = str.indexOf(endTag, 0);
            while ((beginIndex != -1 && endIndex != -1) && (endIndex > beginIndex)) {
                beginIndex += beginTag.length();
                String imgUrl = str.substring(beginIndex, endIndex);
                if (!isNullOrEmpty(imgUrl) && imgUrl.charAt(0) != '[') {
                    list.add(imgUrl);
                }
                endIndex += endIndex + endTag.length();
                beginIndex = str.indexOf(beginTag, endIndex);
                endIndex = str.indexOf(endTag, endIndex);
            }
        }
        return list;
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive ver.
     */
    public static int find(String pattern, String s) {
        return find(pattern, s, !IGNORE_CASE);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive can be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase) {
        return find(pattern, s, ignoreCase, !IGNORE_WIDTH);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive and Full/Half width ignore can
     * be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase, boolean ignoreWidth) {
        if (FP.empty(s))
            return -1;
        pattern = FP.ref(pattern);
        if (ignoreCase) {
            pattern = pattern.toLowerCase();
            s = s.toLowerCase();
        }
        if (ignoreWidth) {
            pattern = narrow(pattern);
            s = narrow(s);
        }
        return s.indexOf(pattern);
    }

    public static String narrow(String s) {
        if (FP.empty(s))
            return "";
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; ++i)
            cs[i] = narrow(cs[i]);
        return new String(cs);
    }

    public static char narrow(char c) {
        int code = c;
        if (code >= 65281 && code <= 65373)// Interesting range
            return (char) (code - 65248); // Full-width to half-width
        else if (code == 12288) // Space
            return (char) (code - 12288 + 32);
        else if (code == 65377)
            return (char) (12290);
        else if (code == 12539)
            return (char) (183);
        else if (code == 8226)
            return (char) (183);
        else if (code == 8233){ //ctrl+enter
            return (char) (10);
        }
        else
            return c;
    }

    public static int ord(char c) {
        if ('a' <= c && c <= 'z')
            return (int)c;
        if ('A' <= c && c <= 'Z')
            return c - 'A' + 'a';
        return 0;
    }

    public static int compare(String x, String y) {
        return FP.ref(x).compareTo(FP.ref(y));
    }

    private final static int SHA1_LENGTH = 40; //SHA1 digest consists of 40 hex digits, total 160 bits
    public static String getHashIfPassIsPlainText(String password) {
        //if password is plain text, it's length will be shorter than SHA1_LENGTH
        if (!StringUtils.isNullOrEmpty(password) && password.length() < SHA1_LENGTH) {
            return sha1(password);
        }
        else {
            return password;
        }
    }

    public static String sha1(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            java.security.MessageDigest sha1 = java.security.MessageDigest
                    .getInstance("SHA1");
            byte[] digest = sha1.digest(str.getBytes());
            sb.append(bytesToHexString(digest));
        } catch (NoSuchAlgorithmException e) {
            Log.e("StringUtils",e.getMessage()+"");
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString();
    }

    public static boolean isValidMobileNumber(String phone) {
        if (phone == null || phone.length() != 11 || !phone.startsWith("1"))
            return false;

        if(!isAllDigits(phone))
            return false;

        return true;
    }

    public static boolean isNameMatchMobilePattern(String name) {
        return (name != null && name.matches("1\\d{10}(y*|s*)"));
    }

    public static String getMobileFromName(String name) {
        Log.e("StringUtils","mobile user name %s"+name);
        if(name == null) {
            return "";
        }
        if (name.startsWith("1") && name.length() >= 11) {
            String mobile = name.substring(0, 11);
            if (isValidMobileNumber(mobile)) {
                return mobile;
            }
        }
        return "";
    }

    public static boolean isIpv4Addr(String addr) {
        return (addr != null && addr.matches("(\\d{1,3}\\.){3}\\d{1,3}"));
    }

    public static <A, B> String fromPair(Pair<A, B> p) {
        return new StringBuilder().append(p.first).append(":").append(p.second).toString();
    }

    public static <A, B> String join(CharSequence delim, List<Pair<A, B>> xs) {
        return TextUtils.join(delim, FP.map(new FP.UnaryFunc<String, Pair<A, B>>() {
            @Override
            public String apply(Pair<A, B> p) {
                return fromPair(p);
            }
        }, xs));
    }

    public static <E> String join(CharSequence delim, SparseArray<E> xs) {
        return join(delim, FP.toList(xs));
    }

    public static String join(CharSequence delim, SparseIntArray xs) { return join(delim, FP.toList(xs)); }

    public static String join(Collection collection, String separator)
    {
        if(collection == null)
            return null;
        else
            return join(collection.iterator(), separator);
    }

    public static String join(Iterator iterator, String separator)
    {
        if(iterator == null)
            return null;
        if(!iterator.hasNext())
            return "";
        Object first = iterator.next();
        if(!iterator.hasNext())
            return objectToString(first);
        StringBuffer buf = new StringBuffer(256);
        if(first != null)
            buf.append(first);
        do
        {
            if(!iterator.hasNext())
                break;
            if(separator != null)
                buf.append(separator);
            Object obj = iterator.next();
            if(obj != null)
                buf.append(obj);
        } while(true);
        return buf.toString();
    }

    public static String objectToString(Object obj)
    {
        return obj != null ? obj.toString() : "";
    }

    public static int getLevenshteinDistance(String s, String t)
    {
        if(s == null || t == null)
            throw new IllegalArgumentException("Strings must not be null");
        int n = s.length();
        int m = t.length();
        if(n == 0)
            return m;
        if(m == 0)
            return n;
        if(n > m)
        {
            String tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }
        int p[] = new int[n + 1];
        int d[] = new int[n + 1];
        for(int i = 0; i <= n; i++)
            p[i] = i;

        for(int j = 1; j <= m; j++)
        {
            char t_j = t.charAt(j - 1);
            d[0] = j;
            for(int i = 1; i <= n; i++)
            {
                int cost = s.charAt(i - 1) != t_j ? 1 : 0;
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            int _d[] = p;
            p = d;
            d = _d;
        }

        return p[n];
    }

    /**
     *
     * @param str
     * @return
     */
    public static String filterSpecificChar(String str) throws Exception {
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }



	//高亮显示关键字
	public static SpannableString getHighlightText(String text, String keyword, int color)
	{
        if (FP.empty(text)) text="";
		SpannableString s = new SpannableString(text);
		try {
			Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);//忽略大小写
			Matcher m = p.matcher(s);

			while (m.find()) {
				int start = m.start();
				int end = m.end();
				s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			return s;
		}catch (PatternSyntaxException e){
			Log.i("xxf-kaede","包含特殊字符");
			return  s;
		}
	}

	public static SpannableString getHighlightText(String text, String keyword){
		return getHighlightText(text,keyword, Color.parseColor("#fac200"));
	}

	/**
	 * 获取指定TEXT的缩略形式
	 * @param text 原始文本
	 * @param length 最大长度
	 * @param suffix 后缀
	**/
	public static String getTextPolished(String text, int length, String suffix){
		if (text.length()<=length)return text;
		return text.substring(0,length-1)+suffix;
	}

	public static Boolean isEmpty(String str){
		if (str==null||str.equals(""))return  true;
		else return false;
	}

    public static int safeParseInt(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int value = 0;
        try {
            value = Integer.valueOf(str);
        } catch (Throwable e) {
            Log.e("StringUtils", "safeParseInt " + str);
        }
        return value;
    }

    public static long safeParseLong(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        long value = 0;
        try {
            value = Long.valueOf(str);
        } catch (Throwable e) {
            Log.e("StringUtils", "safeParseLong " + str);
        }
        return value;
    }

    public static float safeParseFloat(String str) {
        if (str == null || str.length() == 0) {
            return 0f;
        }
        float value = 0f;
        try {
            value = Float.valueOf(str);
        } catch (Throwable e) {
            Log.e("StringUtils", "safeParseFloat " + str);
        }
        return value;
    }

    public static double safeParseDouble(String str) {
        if (str == null || str.length() == 0) {
            return 0f;
        }
        double value = 0f;
        try {
            value = Double.valueOf(str);
        } catch (Throwable e) {
            Log.e("StringUtils", "safeParseDouble " + str);
        }
        return value;
    }

    public static boolean safeParseBoolean(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        boolean value = false;
        try {
            value = Boolean.valueOf(str);
        } catch (Throwable e) {
            Log.e("StringUtils", "safeParseLong " + str);
        }
        return value;
    }


    // 半角转化为全角的方法
    public static String ToSBC(String input) {
        if (input==null){
            return  input;
        }
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i]>32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    // 全角转化为半角的方法
    public static String ToDBC(String input) {
        if (input==null){
            return  input;
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (isChinese(c[i])) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375)
                    c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    //利用编码的方式判断字符是否为汉字的方法：
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    /* 去除特殊字符或将所有中文标号替换为英文标号。
     * 利用正则表达式将所有特殊字符过滤，或利用replaceAll（）
     * 将中文标号替换为英文标号。则转化之后，则可解决排版混乱问题。
     * 去除特殊字符或将所有中文标号替换为英文标号
     * @param str
     * @return
     */

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 字符串为null或长度为0时返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 添加数字分隔符
     * @param format 默认为千分位分隔
     */
    public static String parseLong(String format, long num) {
        if(null == format) {
            format = ",###,###";
        }
        BigDecimal bd=new BigDecimal(num);
        DecimalFormat df=new DecimalFormat(format);
        return df.format(bd);
    }
}
