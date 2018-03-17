package com.yy.live.utils;


import com.yyproto.outlet.SDKParam;
import com.yyproto.outlet.SessEvent;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2016/7/1 16:45
 * Time: 16:45
 * Description:
 */
public class YYHintUtils {

    public static String getSessJoinResCode(int code) {
        switch (code) {
            case SDKParam.SessJoinResCode.KICK_OFF:
                return "频道内用户被踢出频道了";
            case SDKParam.SessJoinResCode.BAN_ID:
                return "此用户被禁止进入此频道";
            case SDKParam.SessJoinResCode.BAN_IP:
                return "此用户所属IP被禁止进入此频道";

            case SDKParam.SessJoinResCode.BAN_PC:
                return "此用户所属设备被禁止进入此频道";
            case SDKParam.SessJoinResCode.USER_LOGIN_DUOWAN_LIMIT:
                return "禁止非欢聚公司用户进入公司频道";
            case SDKParam.SessJoinResCode.NEED_PASSWD:
                return "目标频道需要密码";

            case SDKParam.SessJoinResCode.USER_MUTIJOIN:
                return "多端同时在频道互踢";

            case SDKParam.SessJoinResCode.CHANNEL_FULL:
                return "频道人数达到上限";

            case SDKParam.SessJoinResCode.CHANNEL_CONGEST:
                return "进频道拥塞（同时进频道人数太多）";

            case SDKParam.SessJoinResCode.CHANNEL_NOT_EXIST:
                return "频道不存在";

            case SDKParam.SessJoinResCode.CHANNEL_FROZEN:
                return "目标频道被冻结";

            case SDKParam.SessJoinResCode.CHANNEL_LOCKED:
                return "目标频道被锁了";


            case SDKParam.SessJoinResCode.CHANNEL_ASID_RECYLED:
                return "目标频道短号被回收";

            case SDKParam.SessJoinResCode.USER_LOGIN_TOPSID_LIMIT:
                return "目标频道被锁了";

            case SDKParam.SessJoinResCode.CHANNEL_SUBSID_FULL:
                return "子频道人数满了";
            case SDKParam.SessJoinResCode.CHANNEL_SUBSID_LIMIT:
                return "子频道权限不满足";
            case SDKParam.SessJoinResCode.CHANNEL_CHARGE_LIMIT:
                return "目标频道收费， 用户不满足要求.";
            case SDKParam.SessJoinResCode.GUSET_ACCESS_LIMIT:
                return "目标频道不允许游客进入";
            case SDKParam.SessJoinResCode.CHANNEL_VIP_LIMIT:
                return "目标频道权限要求VIP用户";

            case SDKParam.SessJoinResCode.APP_TYPE_LIMIT:
                return "目标频道要求特定的app才能进入， 用户不满足条件 ";
            case SDKParam.SessJoinResCode.ONLY_OW_CAN_JOIN:
                return "只容许OW进入";
        }
        return "原因未知";
    }

    public static String getChatSvcResult(int reason) {
        switch (reason) {
            case SessEvent.ETTextChatSvcResultRes.PASS:
                return "";
            case SessEvent.ETTextChatSvcResultRes.GLOBAL_BLACK_LIST:
                return "发送失败: 全局黑名单过滤掉";
            case SessEvent.ETTextChatSvcResultRes.TID_NOT_FOUND:
                return "发送失败: 子频道没有找到";
            case SessEvent.ETTextChatSvcResultRes.SID_NOT_FOUND:
                return "发送失败, 顶级频道没有找到";
            case SessEvent.ETTextChatSvcResultRes.USER_NOT_EXIST:
                return "发送失败, 用户发言子频道和实际所在子频道不一致";
            case SessEvent.ETTextChatSvcResultRes.USR_DISABLE_TEXT:
                return "发送失败， 发送用户被禁止发言";
            case SessEvent.ETTextChatSvcResultRes.VISITOR_DISALBE_TEXT:
                return "发送失败， 发送者游客禁止发言";
            case SessEvent.ETTextChatSvcResultRes.ACCESS_TIME_LIMIT:
                return "发送失败 ， 需要进频道等待N秒才能才能发言";
            case SessEvent.ETTextChatSvcResultRes.INTERVAL_TIME_LIMIT:
                return "发送失败, 两次发言间隔需要一段时间";
            case SessEvent.ETTextChatSvcResultRes.BIND_PHONE_LIMIT:
                return "发送失败， 用户需要绑定手机才能发言";
            case SessEvent.ETTextChatSvcResultRes.TEXT_COUNTER_LIMITED:
                return "发送失败, 超过后台设置的频道发言频率";
            case SessEvent.ETTextChatSvcResultRes.FILTER_LIMITED:
                return "发送失败， 文本中含有违禁字";
            case SessEvent.ETTextChatSvcResultRes.ANONYMOUS_UID:
                return "发送失败，  匿名用户不允许发送公屏";
            case SessEvent.ETTextChatSvcResultRes.REQ_LIMITED:
                return "发送失败， 超过服务器所能承受最大请求";
            case SessEvent.ETTextChatSvcResultRes.TEXT_MAX_LONG_LIMITED:
                return "发送失败， 文本过长";
            case SessEvent.ETTextChatSvcResultRes.TEXT_LENGTH_LIMITED:
                return "发送失败， 文本长度超过限制";
            case SessEvent.ETTextChatSvcResultRes.TICKET_OR_URL_LIMITED:
                return "发送失败， 飞机票或URL限制";
        }
        return "原因未知";
    }

    /**
     * 语音模式
     *
     * @param style
     * @return
     */
    public static String getSessChannelStyle(int style) {
        switch (style) {
            case 0:
                return "自由模式";
            case 1:
                return "主席模式";
            case 2:
                return "麦序模式";
        }
        return "模式未知";
    }

}
