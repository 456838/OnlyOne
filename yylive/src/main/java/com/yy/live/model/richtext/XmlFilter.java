package com.yy.live.model.richtext;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.salton123.util.LogUtils;
import com.yy.live.utils.StringUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ping on 15/1/13.
 */
public abstract class XmlFilter extends BaseRichTextFilter {

    /* <?xml version="1.0"?>
    <msg>
    <extra id="yyentnoble">
    <img data="nobleimg2" url="yyentnoble/lv2.png" priority="0" isCache="1"></img>
    <noble lv="2"></noble>
    </extra>
    <txt data="试一下PC的xml内容" />
    </msg>*/

    public static boolean isXmlMessage(String message) {
        if (!checkValid(message)) {
            return false;
        }

        return parserXml(message) != null;
    }

    public static Document parserXml(String message) {
        if (!checkValid(message)) {
            return null;
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new ByteArrayInputStream(message.getBytes()));
        } catch (Throwable e) {
            Log.e("XmlFilter", "parserXml error! " + e);
        }
        return null;
    }


    public static String parse1931Message(String message) {
        if (!checkValid(message)) {
            return message;
        }
        try {
            String fixedMsg = message.replaceFirst("\\u2029", "");
            Document parserDoc = parserXml(fixedMsg);
            if (parserDoc == null) {
                return message;
            }
            Element element = parserDoc.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("txt");
            if (nodeList != null) {
                Node node;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    node = nodeList.item(i);
                    if (node != null && node.getParentNode() != null && "msg".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("data");
                        if (node != null) {
                            return node.getNodeValue();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("XmlFilter", "parse1931Message error! " + e);
        }
        return message;
    }

    public static XmlChannelMessage parseNobleChannelMessage(String message) {
        XmlChannelMessage xmlChannelMessage = new XmlChannelMessage();
        if (!checkValid(message)) {
            xmlChannelMessage.text = message;
            return xmlChannelMessage;
        }
        try {
            String fixedMsg = message.replaceFirst("\\u2029", "");
            Document parserDoc = parserXml(fixedMsg);
            if (parserDoc == null) {
                xmlChannelMessage.text = message;
                return xmlChannelMessage;
            }
            Element element = parserDoc.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("txt");
            if (nodeList != null) {
                Node node;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    node = nodeList.item(i);
                    if (node != null && node.getParentNode() != null && "msg".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("data");
                        if (node != null) {
                            xmlChannelMessage.text = node.getNodeValue();
                        }
                    }
                }
            }
            NodeList nobleNodeList = element.getElementsByTagName("noble");
            if (nobleNodeList != null) {
                Node node;
                for (int i = 0; i < nobleNodeList.getLength(); i++) {
                    node = nobleNodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("lv");
                        if (node != null) {
                            xmlChannelMessage.nobleLevel = StringUtils.safeParseInt(node.getNodeValue());
                        }
                    }
                }
            }
            NodeList likelampNodeList = element.getElementsByTagName("atmospherelamp");
            if (likelampNodeList != null) {
                Node node;
                for (int i = 0; i < likelampNodeList.getLength(); i++) {
                    node = likelampNodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("lamp");
                        if (node != null) {
                            xmlChannelMessage.likelampId = node.getNodeValue();
                        }
                    }
                }
            }
            NodeList imgNodeList = element.getElementsByTagName("img");
            if (imgNodeList != null) {
                Node node;
                String trueloveMedal = "";
                String trueLoveLevel = "";
                for (int i = 0; i < imgNodeList.getLength(); i++) {
                    node = imgNodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName()) && node.getParentNode().getAttributes().getNamedItem("id") != null) {
                        String idValue = node.getParentNode().getAttributes().getNamedItem("id").getNodeValue();
                        if ("entertainment".equals(idValue)) {
                            NamedNodeMap attrs = node.getAttributes();
                            node = attrs.getNamedItem("data");
                            if (node != null) {
                                String data = node.getNodeValue();
                                if (!StringUtils.isEmpty(data)) {
                                    String[] strArr = data.split("/");
                                    if (strArr != null && strArr.length > 0) {
                                        trueloveMedal = strArr[strArr.length - 1];
                                        trueLoveLevel = strArr[strArr.length - 2];
                                        xmlChannelMessage.trueloveMedal = trueloveMedal;
                                        xmlChannelMessage.trueLoveLevel = StringUtils.safeParseInt(trueLoveLevel);
                                    }
                                }
                            }
                        }
                        if ("yyentactivitymedal".equals(idValue)) {
                            NamedNodeMap attrs = node.getAttributes();
                            node = attrs.getNamedItem("url");
                            if (node != null) {
                                xmlChannelMessage.actMedalUrl = node.getNodeValue();
                            }
                        }
                    }
                }
            }
            NodeList knightNodeList = element.getElementsByTagName("knight");
            if (knightNodeList != null) {
                Node node;
                for (int i = 0; i < knightNodeList.getLength(); i++) {
                    node = knightNodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("lv");
                        if (node != null) {
                            xmlChannelMessage.knightLevel = StringUtils.safeParseInt(node.getNodeValue());
                        }
                    }
                }
            }

            NodeList activityNodeList = element.getElementsByTagName("activitymedal");
            if (activityNodeList != null) {
                Node node;
                for (int i = 0; i < activityNodeList.getLength(); i++) {
                    node = activityNodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("lv");
                        if (node != null) {
                            xmlChannelMessage.actMedalLevel = node.getNodeValue();
                        }
                    }
                }
            }

        } catch (Throwable e) {
            Log.e("XmlFilter", "parseNobleChannelMessage error! " + e.getMessage());
            if (TextUtils.isEmpty(xmlChannelMessage.text)) {
                xmlChannelMessage.text = message;
            }
            return xmlChannelMessage;
        }
        if (TextUtils.isEmpty(xmlChannelMessage.text)) {
            xmlChannelMessage.text = message;
        }
        return xmlChannelMessage;
    }

    public static boolean checkValid(String str) {
        return str != null && str.length() > 5 && str.startsWith("<?xml");
    }

    public static String creatXmlNobleChannelMessage(String message, CreateMedalXmlInfo medalXmlInfo) {
        try {
            message = message.replaceAll("\\u0026", "&amp;");
            message = message.replaceAll("\\u003C", "&lt;");
            message = message.replaceAll("\\u003E", "&gt;");
            message = message.replaceAll("\\u0027", "&apos;");
            message = message.replaceAll("\\u0022", "&quot;");
        } catch (Throwable throwable) {
            Log.e("XmlFilter", throwable.getMessage() + "");
        }

        String trueLoveXml = "";
        if (!StringUtils.isEmpty(medalXmlInfo.trueLovelevel) && !StringUtils.isEmpty(medalXmlInfo.medalName)) {
            trueLoveXml = "<extra id=\"entertainment\">\n" +
                    "<img data=\"trueLoveGroupMedal/" + medalXmlInfo.uid + "/" + medalXmlInfo.trueLovelevel + "/" + medalXmlInfo.medalName + "\" priority=\"1\" isCache=\"0\" pos=\"100\"></img>\n" +
                    "</extra>\n";
        }
        String likelampXml = "";
        if (!StringUtils.isNullOrEmpty(medalXmlInfo.matchId)) {
            likelampXml = "<extra id=\"yyentatmospherelamp\">\n" +
                    "<atmospherelamp lamp=\"" + medalXmlInfo.matchId + "\"></atmospherelamp>\n" +
                    "<img data=\"yyentatmospherelamp/" + medalXmlInfo.matchId + "\"" + " priority=\"2\" pos=\"100\"></img>\n" +
                    "</extra>\n";
        }

        String activityMedalXml = "";
        if (!StringUtils.isNullOrEmpty(medalXmlInfo.actMedalUrl)) {
            activityMedalXml = "<extra id=\"yyentactivitymedal\">\n" +
                    "<img data=\"activitymedalimg" + medalXmlInfo.actMedalLevel + "\""
                    + " priority=\"0\" pos=\"100\" url=\"" + medalXmlInfo.actMedalUrl + "\"></img>\n" +
                    "<activitymedal lv=\"" + medalXmlInfo.actMedalLevel + "\"></activitymedal>\n" +
                    "</extra>\n";
        }

        return "<?xml version=\"1.0\"?>\n" +
                "<msg>\n" +
                "<extra id=\"yyentnoble\">\n" +
                "<img data=\"nobleimg" + medalXmlInfo.userNobleInfoLevel + "\" url=\"yyentnoble/lv" + medalXmlInfo.userNobleInfoLevel + ".png\" priority=\"0\" isCache=\"1\"></img>\n" +
                "<noble lv=\"" + medalXmlInfo.userNobleInfoLevel + "\"></noble>\n" +
                "</extra>\n" +
                trueLoveXml +
                "<extra id=\"yyentknight\">\n" +
                "<img data=\"knightimg" + medalXmlInfo.knightMedalLv + "\" pos=\"100\" url=\":/RewardTask/theme/entertainment5-template/RewardTask/RewardTaskMedalTail_" + medalXmlInfo.knightMedalLv + ".webp\" priority=\"0\" isCache=\"0\"></img>\n" +
                "<knight lv=\"" + medalXmlInfo.knightMedalLv + "\"></knight>\n" +
                "</extra>\n" +
                likelampXml +
                activityMedalXml +
                "<txt data=\"" + message + "\" />\n" +
                " </msg>";
    }

    public static String creatXmlMobileLiveChannelMessage(String message, String headUrl) {
        message = fixXmlString(message);
        headUrl = fixXmlString(URLEncoder.encode(headUrl));
        return "<?xml version=\"1.0\"?>\n" +
                "<msg>\n" +
                "<extra id=\"live\">\n" +
                "<img data=\"live" + "\" url=\"" + headUrl + "\" ></img>\n" +
                "</extra>\n" +
                "<txt data=\"" + message + "\" />\n" +
                " </msg>";
    }

    public static String fixXmlString(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        try {
            string = string.replaceAll("\\u0026", "&amp;");
            string = string.replaceAll("\\u003C", "&lt;");
            string = string.replaceAll("\\u003E", "&gt;");
            string = string.replaceAll("\\u0027", "&apos;");
            string = string.replaceAll("\\u0022", "&quot;");
        } catch (Throwable throwable) {
            Log.e("XmlFilter", "fixXmlString error!" + throwable.getMessage());
        }
        return string;
    }


    public static String createScenarioChannelMessage(String message, String scenarioJson) {
        LogUtils.e("createScenarioChannelMessage:before:" + scenarioJson);
        scenarioJson = fixXmlString(scenarioJson);
        LogUtils.e("createScenarioChannelMessage:fater:" + scenarioJson);

        return "<?xml version=\"1.0\"?>\n" +
                "<msg>\n" +
                "<extra id=\"hamburg-template\">\n" +
                "<txt data=\"" + scenarioJson + "\"></txt>\n" +
                "</extra>\n" +
                "<txt data=\"" + message + "\" />\n" +
                " </msg>";
    }

    /**
     * 解析带有设置剧本的消息
     *
     * @param message
     * @return
     */
    public static Pair<String, String> parseScenarioChannelMessage(String message) {
        String text = !TextUtils.isEmpty(message) ? message : "";
        String url = "";
        if (!checkValid(message)) {
            return new Pair<String, String>(text, url);
        }
        try {
            String fixedMsg = message.replaceFirst("\\u2029", "");
            Document parserDoc = parserXml(fixedMsg);
            if (parserDoc == null) {
                return new Pair<String, String>(text, url);
            }
            Element element = parserDoc.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("txt");
            if (nodeList != null) {
                Node node;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    node = nodeList.item(i);
                    if (node != null && node.getParentNode() != null && "msg".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("data");
                        if (node != null) {
                            text = node.getNodeValue();
                            break;
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("XmlFilter", "parseScenarioChannelMessage error! " + e);
        }
        return new Pair<String, String>(text, url);

    }


    /**
     * 解析公屏消息内容
     *
     * @param message
     * @return Pair, first是消息内容，second是头像url
     */
    public static Pair<String, String> parseMobileLiveChannelMessage(String message) {
        String text = !TextUtils.isEmpty(message) ? message : "";
        String url = "";
        if (!checkValid(message)) {
            return new Pair<String, String>(text, url);
        }
        try {
            String fixedMsg = message.replaceFirst("\\u2029", "");
            Document parserDoc = parserXml(fixedMsg);
            if (parserDoc == null) {
                return new Pair<String, String>(text, url);
            }
            Element element = parserDoc.getDocumentElement();
            NodeList nodeList = element.getElementsByTagName("txt");
            if (nodeList != null) {
                Node node;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    node = nodeList.item(i);
                    if (node != null && node.getParentNode() != null && "msg".equals(node.getParentNode().getNodeName())) {
                        NamedNodeMap attrs = node.getAttributes();
                        node = attrs.getNamedItem("data");
                        if (node != null) {
                            text = node.getNodeValue();
                            break;
                        }
                    }
                }
            }
            nodeList = element.getElementsByTagName("img");
            if (nodeList != null) {
                Node node;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    node = nodeList.item(i);
                    if (node != null && node.getParentNode() != null && "extra".equals(node.getParentNode().getNodeName())) {
                        String idValue = node.getParentNode().getAttributes().getNamedItem("id").getNodeValue();
                        if ("live".equals(idValue)) {
                            NamedNodeMap attrs = node.getAttributes();
                            node = attrs.getNamedItem("url");
                            if (node != null) {
                                url = URLDecoder.decode(node.getNodeValue());
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("XmlFilter", "parseMobileLiveChannelMessage error! " + e);
        }
        return new Pair<String, String>(text, url);
    }

    /**
     * 构建公屏透传Xml信息参数，以后新增可在这里增加
     */
    public static class CreateMedalXmlInfo {
        public int userNobleInfoLevel;
        public String uid;
        public String trueLovelevel;
        public String medalName;
        public int knightMedalLv;
        public String matchId;
        public String actMedalLevel;
        public String actMedalUrl;

        public CreateMedalXmlInfo(int userNobleInfoLevel, String uid, String trueLovelevel,
                                  String medalName, int knightMedalLv, String matchId,
                                  String activityMedalLevel, String activityMedalUrl) {
            this.userNobleInfoLevel = userNobleInfoLevel;
            this.uid = uid;
            this.trueLovelevel = trueLovelevel;
            this.medalName = medalName;
            this.knightMedalLv = knightMedalLv;
            this.matchId = matchId;
            this.actMedalLevel = activityMedalLevel;
            this.actMedalUrl = activityMedalUrl;
        }
    }
}
