package com.yy.live.core.auth;

/**
 * Created by zhangge on 2015/1/7.
 */
public class LoginResCodeUtils {

    public static String getLoginFailMsg(int code) {
        String errMsg = "";
        switch (code) {
            case 5:
                errMsg = "代理用户名密码错误";
                break;
            case 300:
                errMsg = "暂时无法受理,建议稍后再试";
                break;
            case 400:
                errMsg = "碰到无法理解的功能请求";
                break;
            case 401:
                errMsg = "请求者身份未经认证,因此后台拒绝完成功能";
                break;
            case 403:
                errMsg = "对方实体没授权(可不是后台不受理)";
                break;
            case 405:
                //errMsg = "无权限请求此项功能";
                //修改为用户能看懂的提示
                errMsg = "你的帐号已被封禁";
                break;
            case 406:
                //errMsg = "用户数据存储超过了限额";
                //修改为用户能看懂的提示
                errMsg = "服务器人数已满";
                break;
            case 407:
                errMsg = "某些有生存时间限制的资源已经超时不可用";
                break;
            case 408:
                errMsg = "请求过程超时";
                break;
            case 409:
                //errMsg = "资源或者对象冲突(比如重名)";
                //修改为用户能看懂的提示
                errMsg = "你的帐号已经登录";
                break;
            case 414:
                errMsg = "参数数据出错.(越界,超长,不完整等原因) ";
                break;
            case 415:
                errMsg = "数据库操作失败 ";
                break;
            case 416:
                errMsg = "数据库暂时不可用，可能是正在维护";
                break;
            case 417:
                errMsg = "连接URS的错误";
                break;
            case 418:
                errMsg = "节点已经被转移";
                break;
            case 419:
                errMsg = "接收缓冲区溢出";
                break;
            case 500:
                errMsg = "出错了.但是原因未明,或是不便透露给你";
                break;
            case 504:
                errMsg = "后台忙,拒绝处理";
                break;
            case 505:
                errMsg = "后台不支持此协议版本";
                break;
            case 453:
                errMsg = "操作次数太多了";
                break;
            case 506:
                errMsg = "与数据库数据不同步,client需要重新获得数据";
                break;
            case 507:
                errMsg = "数量不够";
                break;
            case 508:
                errMsg = "目标(对象或用户)不存在";
                break;
            case 550:
                errMsg = "不支持的服务";
                break;
            case 551:
                errMsg = "服务器未找到";
                break;
            case 552:
                errMsg = "服务暂时不可用";
                break;
            case 553:
                errMsg = "内部错误，根据connid找不到连接";
                break;
            case 554:
                errMsg = "发送请求给内部服务器时，缓冲不够，瓶颈保护用。";
                break;
            case 555:
                errMsg = "内部服务连接不上";
                break;
            case 556:
                errMsg = "发送请求发生异常";
                break;
            case 557:
                errMsg = "内部配置错误";
                break;
            case 558:
                errMsg = "请求数据包错误 ";
                break;
            case 559:
                errMsg = "定位错误，服务器收到不属于自己的请求";
                break;
            default:
                break;
        }
        return errMsg;
    }
}
