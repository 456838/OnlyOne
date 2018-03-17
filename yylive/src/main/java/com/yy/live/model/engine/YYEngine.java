package com.yy.live.model.engine;

import android.util.Log;
import android.util.SparseArray;

import com.salton123.util.LogUtils;
import com.yy.mobile.YYHandlerMgr;
import com.yy.udbauth.AuthRequest;
import com.yyproto.base.ProtoReq;
import com.yyproto.db.DCHelper;
import com.yyproto.db.IDatabase;
import com.yyproto.db.IRow;
import com.yyproto.db.ITable;
import com.yyproto.db.ProtoTable;
import com.yyproto.outlet.ILogin;
import com.yyproto.outlet.IMediaVideo;
import com.yyproto.outlet.IProtoMgr;
import com.yyproto.outlet.ISession;
import com.yyproto.outlet.ISvc;
import com.yyproto.outlet.LoginRequest;
import com.yyproto.outlet.SDKParam;
import com.yyproto.outlet.SessEvent;
import com.yyproto.outlet.SessQuery;
import com.yyproto.outlet.SessRequest;
import com.yyproto.outlet.SvcRequest;
import com.yyproto.report.IReport;

import java.util.ArrayList;
import java.util.List;

public class YYEngine {
    public static final String TAG ="YYEngine";
    static volatile YYEngine defaultInstance;

    public ILogin mILogin = null;
    public ISession mISession = null;
    public ISvc mISvc = null;
    public IReport mIReport = null;
    public IDatabase mIDatabase = null;
    public static YYHandlerMgr mYYHandlerMgr = null;
    public IMediaVideo mIMediaVideo = null;
    private static boolean mIsOnline = false;      //非匿名登录

    public boolean isOnline() {
        return mIsOnline;
    }

    public void setOnline(boolean online) {
        mIsOnline = online;
    }
    //    public Boolean mIsForeGround = true;

    public static YYEngine getInstance() {
        if (defaultInstance == null) {
            synchronized (YYEngine.class) {
                if (defaultInstance == null) {
                    defaultInstance = new YYEngine();
                }
            }
        }
        return defaultInstance;
    }

    private YYEngine() {
    }

    public void init() {
        mILogin = IProtoMgr.instance().getLogin();
        mISession = IProtoMgr.instance().getSess();
        mIReport = IProtoMgr.instance().getReport();
        mISvc = IProtoMgr.instance().getSvc();
        mIMediaVideo = IProtoMgr.instance().getMedia();
        mIDatabase = DCHelper.createDatabase(ProtoTable.PROTO_DB_ID);
        mYYHandlerMgr = new YYHandlerMgr();
        mILogin.watch(mYYHandlerMgr);
        mIReport.watch(mYYHandlerMgr);
        mISvc.watch(mYYHandlerMgr);
        mISession.watch(mYYHandlerMgr);
    }

    /**
     * 进频道请求
     *
     * @param sid    频道id
     * @param subSid 子频道id
     * @param props  参数
     */
    public static void JoinChannel(long sid, long subSid, SparseArray<byte[]> props) {
        LogUtils.e("JoinChannel,subSid:" + sid + ",subSid:" + subSid);
        defaultInstance.mISession.join(sid, subSid, props, "app_join".getBytes());
        defaultInstance.mISession.watch(mYYHandlerMgr);
    }

    /**.
     * 退出频道
     */
    public static void LeaveChannel() {
        defaultInstance.mIMediaVideo.leave();
        defaultInstance.mISession.leave();
    }


    public void deInit() {
        IProtoMgr.instance().deInit();
    }


    /**
     * 发送登录相关的请求
     *
     * @param req
     * @return
     */
    public static int SendLoginReq(ProtoReq req) {
        int ret = IProtoMgr.instance().getLogin().sendRequest(req);
        HandlerRequestResult(ret);
        return ret;
    }

    /**
     * 发送信令相关的请求
     *
     * @param req
     * @return
     */
    public static int SendSessionReq(ProtoReq req) {
        int ret = IProtoMgr.instance().getSess().sendRequest(req);
        HandlerRequestResult(ret);
        return ret;
    }

    /**
     * 处理信令请求返回的结果
     *
     * @param resultCode
     */
    public static void HandlerRequestResult(int resultCode) {
//        String GetMethodName() = Thread.currentThread().getStackTrace()[2].GetMethodName();
        if (resultCode == SDKParam.RequestRes.REQ_SUCCESS) {
            LogUtils.d(GetMethodName() + ":请求成功");
        } else if (resultCode == SDKParam.RequestRes.REQ_TOO_QUICK) {
            LogUtils.d(GetMethodName() + ":请求过快");
        } else if (resultCode == SDKParam.RequestRes.REQ_FAILED) {
            LogUtils.d(GetMethodName() + ":请求失败");
        } else if (resultCode == SDKParam.RequestRes.REQ_MARSHALL_ERR) {
            LogUtils.d(GetMethodName() + ":请求MARSHALL错误");
        } else if (resultCode == SDKParam.RequestRes.REQ_SDK_NOT_INIT) {
            LogUtils.d(GetMethodName() + ":请求SDK未初始化");
        } else {
            LogUtils.d(GetMethodName() + ":请求出现未知错误");
        }
    }

    /**
     * 获取当前函数名字
     *
     * @return
     */
    private static String GetMethodName() {
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        return thisMethodStack.getMethodName();
    }


    /**
     * 判断频道内是否活动
     *
     * @return
     */
    public static boolean HasActiveSess() {
        return defaultInstance.mISession.hasActiveSess();
    }


    /**
     * 根据UID查询用户信息
     *
     * @param uids
     */
    public static void SessUinfoReq(int sid, long[] uids, String context) {
        SessRequest.SessUinfoReq _SessUinfoReq = new SessRequest.SessUinfoReq();
        _SessUinfoReq.setCtx(context);
        _SessUinfoReq.uids = uids;
        _SessUinfoReq.setSid(sid);
        HandlerRequestResult(SendSessionReq(_SessUinfoReq));
    }

    /**
     * 查询单个uid用户信息
     *
     * @param sid
     * @param uids
     * @param context
     */
    public static void SessSingleUinfoReq(long sid, long uids, String context) {
        SessRequest.SessUinfoReq _SessUinfoReq = new SessRequest.SessUinfoReq();
        _SessUinfoReq.setCtx(context);
        _SessUinfoReq.uids = new long[]{uids};
        _SessUinfoReq.setSid(sid);
        HandlerRequestResult(SendSessionReq(_SessUinfoReq));
    }

    /**
     * 查询指定数量的用户信息
     *
     * @param sid
     * @param subSid
     * @param pos
     * @param num
     */
    public static void SessUinfoPageReq(long sid, long subSid, int pos, int num) {
        LogUtils.e("SessUinfoPageReq,sid:" + sid + ",subSid:" + subSid + ",pos:" + pos + ",num:" + num);
        SessRequest.SessUinfoPageReq _SessUinfoPageReq = new SessRequest.SessUinfoPageReq(subSid, pos, num);
        _SessUinfoPageReq.setSid(sid);
        SendSessionReq(_SessUinfoPageReq);
    }

    /**
     * @param ctx 上下文，随请求返回
     * @param uid 用户标识
     * @return 获取一个用户信息请求
     */
    public static synchronized int GetOneIMUInfoReq(String ctx, long uid) {
        LogUtils.e("GetOneIMUInfoReq,ctx:" + ctx + ",uid:" + uid);
        List<Long> mUid = new ArrayList<>();
        mUid.add(uid);
        return GetIMUInfoReq(ctx, mUid);
    }

    /**
     * @param ctx    上下文，随请求返回
     * @param userId 用户集
     * @return 请求用户信息
     */
    public static synchronized int GetIMUInfoReq(String ctx, List<Long> userId) {
        if (userId == null || userId.size() == 0) {
            return -1;
        }
        long[] mUids = new long[userId.size()];
        for (int i = 0; i < userId.size(); i++) {
            long id = userId.get(i);
            mUids[i] = id;
        }
        LoginRequest.GetIMUInfoReq req = new LoginRequest.GetIMUInfoReq();
        req.setCtx(ctx);
        //get partial
//        req.mGetall = true;
        req.mUids = mUids;
        req.mProps.add(SDKParam.IMUInfoPropSet.uid.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.nick.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.sex.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.birthday.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.area.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.province.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.city.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.sign.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.intro.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.jifen.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.yyno.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.logo_index.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.custom_logo.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_100.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_144.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_640.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.stage_name.getBytes());
        return SendLoginReq(req);
    }

    public static synchronized int GetIMLogoReq(String ctx, List<Long> userId) {
        LogUtils.e("GetIMLogoReq,ctx:" + ctx + ",userIdList:" + userId.size());
        if (userId == null || userId.size() == 0) {
            return -1;
        }
        long[] mUids = new long[userId.size()];
        for (int i = 0; i < userId.size(); i++) {
            long id = userId.get(i);
            mUids[i] = id;
        }
        LoginRequest.GetIMUInfoReq req = new LoginRequest.GetIMUInfoReq();
        req.setCtx(ctx);
        req.mUids = mUids;
        req.mProps.add(SDKParam.IMUInfoPropSet.uid.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.yyno.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.logo_index.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.custom_logo.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_100.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_144.getBytes());
        req.mProps.add(SDKParam.IMUInfoPropSet.hd_logo_640.getBytes());
        return SendLoginReq(req);
    }

    /**
     * 信令登出
     */
    public static void Logout() {
        LoginRequest.LoginReqLogout req = new LoginRequest.LoginReqLogout();
        HandlerRequestResult(SendLoginReq(req));
    }


    /**
     * 取大频道消息
     *
     * @param sid 大频道sid
     */
    public static void SessGetChInfoReq(int sid) {
        SessRequest.SessGetChInfoReq req = new SessRequest.SessGetChInfoReq(sid);
        SendSessionReq(req);
    }

    /**
     * 取得某子频道信息请求
     *
     * @param sid    大频道sid
     * @param subSid 小频道subsid
     */
    public static void SessGetOneSubChInfoReq(long sid, long subSid) {
        long[] subSids = new long[1];
        subSids[0] = subSid;
        SessRequest.SessGetSubChInfoReq req2 = new SessRequest.SessGetSubChInfoReq(sid, subSids, true);
        HandlerRequestResult(SendSessionReq(req2));
    }


    /**
     * 更新频道模式为自由模式
     *
     * @param sid
     * @param subSid
     */
    public static void SessUpdateChStyleInfoReq(long sid, long subSid) {
        SessRequest.SessUpdateChInfoReq sessUpdateChInfoReq = new SessRequest.SessUpdateChInfoReq(sid, subSid);
        sessUpdateChInfoReq.setProps((short) SDKParam.SessInfoItem.SIT_STYLE, new String(SDKParam.SessChannelStyle.FREE_STYLE + "").getBytes());
        SendSessionReq(sessUpdateChInfoReq);
    }

    /**
     * 查询麦克风状态
     */
    public static void QueryMicState() {
        defaultInstance.mIMediaVideo.queryMicState();        //初始化的时候查询一下麦克风状态
    }


    /**
     * 上麦请求
     *
     * @param sid     大频道sid
     * @param context 上下文
     */
    public static void SessMicJoinReq(long sid, String context) {
        SessRequest.SessMicJoinReq join = new SessRequest.SessMicJoinReq(sid);
        join.setCtx(context);
        HandlerRequestResult(SendSessionReq(join));
        QueryMicState();
    }


    /**
     * 下麦请求
     *
     * @param sid     大频道sid
     * @param context 上下文
     */
    public static void MicLeaveReq(long sid, String context) {
        SessRequest.SessMicLeaveReq leave = new SessRequest.SessMicLeaveReq(sid);
        leave.setCtx(context);
        HandlerRequestResult(SendSessionReq(leave));
        QueryMicState();
    }

    /**
     * 打开mic
     */
    public static void OpenMicReq() {
        defaultInstance.mIMediaVideo.openMic();
        QueryMicState();
    }

    /**
     * 关闭mic
     */
    public static void CloseMicReq() {
        defaultInstance.mIMediaVideo.closeMic();
        QueryMicState();
    }


    /**
     * 连麦请求
     *
     * @param topSid     频道id
     * @param fistMicUid 首mic用户uid
     * @param inviteeUid 受邀uid
     * @param invited    邀请or删除
     */
    public static void SessInviteModChorusMic(long topSid, long fistMicUid, long inviteeUid, boolean invited) {
        Log.e(TAG,"连麦请求：" + topSid + "-" + fistMicUid + "-" + inviteeUid + "-" + invited);
        SessRequest.SessInviteModChorusMic sessInviteModChorusMic = new SessRequest.SessInviteModChorusMic(topSid);
        sessInviteModChorusMic.bAdd = invited;
        sessInviteModChorusMic.invitee = inviteeUid;
        sessInviteModChorusMic.mic_first = fistMicUid;
        SendSessionReq(sessInviteModChorusMic);
    }

    /**
     * 受邀者处理联麦请求
     *
     * @param topSid       频道id
     * @param fromUid      邀请者uid
     * @param responseCode 回复码 0为接收， 1为拒绝
     */
    public static void SessInviteChorusMicRes(long topSid, long fromUid, int responseCode) {
        Log.e(TAG,"连麦请求：" + topSid + "-" + fromUid + "-" + responseCode);
        SessRequest.SessInviteChorusMicRes sessInviteChorusMicRes = new SessRequest.SessInviteChorusMicRes(topSid);
        sessInviteChorusMicRes.mic_first = fromUid;
        sessInviteChorusMicRes.res = responseCode;
        SendSessionReq(sessInviteChorusMicRes);
    }

    /**
     * 管理员连麦请求
     *
     * @param topSid     频道id
     * @param fistMicUid 首mic用户uid
     * @param inviteeUid 受邀uid
     * @param invited    添加或者删除联麦， true为添加， false为删除
     */
    public static void SessAdminModChorusMic(long topSid, long fistMicUid, long inviteeUid, boolean invited) {
        Log.e(TAG,"连麦请求：" + topSid + "-" + fistMicUid + "-" + inviteeUid + "-" + invited);
        SessRequest.SessAdminModChorusMic sessInviteModChorusMic = new SessRequest.SessAdminModChorusMic(topSid);
        sessInviteModChorusMic.bAdd = invited;
        sessInviteModChorusMic.invitee = inviteeUid;
        sessInviteModChorusMic.mic_first = fistMicUid;
        SendSessionReq(sessInviteModChorusMic);
    }

    /**
     * 拉取管理员请求
     *
     * @param sid 大频道sid
     */
    public static void SessPullAdminReq(int sid) {
        SessRequest.SessPullAdminReq sessPullAdminReq = new SessRequest.SessPullAdminReq(sid);
        HandlerRequestResult(SendSessionReq(sessPullAdminReq));
    }


    public static void VideoLiveStart(int appid) {
        defaultInstance.mIMediaVideo.videoLiveStart(appid);
    }

    /**
     * 获取当前登录用户的uid
     *
     * @return
     */
    public static long GetCurrentUid() {
        IDatabase db = defaultInstance.mIDatabase;
        ITable table = db.getTable(ProtoTable.TABLE_ID.E_TBL_LOGINUINFO.ordinal());
        IRow row = table.getRow(ProtoTable.ONE_DIM_KEY);
        int uid = row.getInt32(ProtoTable.LOGINUINFO.dwUid.ordinal());
        return uid;
    }

    /**
     * 获取当前子频道的角色
     *
     * @param subSid
     * @return
     */
    public static int GetCurrentChannelRole(long subSid) {
        SessQuery.SessUserRole userRole = new SessQuery.SessUserRole();
        List<SessEvent.SubChannelRoler> subChannelRolers = userRole.mRolers;
        for (SessEvent.SubChannelRoler role : subChannelRolers) {
            if (role.mSubSid == subSid) {
                return role.mRoler;
            }
        }
        return -1;
    }


    /**
     * 查询当前的麦序列表
     *
     * @param onMicUidList
     */
    public static void GetOnMicListUserInfo(String ctx,List<Long> onMicUidList) {
        GetIMUInfoReq(ctx, onMicUidList);
    }


//    /**
//     * 获取自己创建的公会
//     */
//    public static void getMyChanListReq (){
//        LoginRequest.GetMyChanListReq myChanListReq = new LoginRequest.GetMyChanListReq();
//        SendLoginReq(myChanListReq);
//    }

//    /**
//     * 获取我收藏的频道
//     */
//    public static void getMyFavorReq() {
//        LoginRequest.SyncMyListReq sycReq = new LoginRequest.SyncMyListReq();
//        sycReq.cmd = (LoginRequest.SyncMyListReq.CMD_SYNC_SLIT_FLAG);
//        sycReq.setCtx("slit_" + System.currentTimeMillis());
//        SendLoginReq(sycReq);
//    }

    /**
     * 获取我创建的频道请求
     */
    public static void SyncMyGuildListReq() {
        LoginRequest.SyncMyListReq myListReq = new LoginRequest.SyncMyListReq();
        myListReq.cmd = LoginRequest.SyncMyListReq.CMD_YSNC_GUILD_FLAG;
        myListReq.setCtx("guild_" + System.currentTimeMillis());
        SendLoginReq(myListReq);
    }

    public static void CreditLoginReq(String uidStr, String credit, String context) {
        AuthRequest.CreditLoginReq creditLoginReq = new AuthRequest.CreditLoginReq(uidStr, AuthRequest.STRATEGY.NONE, credit, context);
        //将Auth数据转为byte[]再发送出去
        LoginRequest.LoginWithAuthReq req = new LoginRequest.LoginWithAuthReq();
        req.setCtx(context);
        req.mAuthData = creditLoginReq.marshall();
        SendLoginReq(req);
    }

    public static int AuthRequest(String username, String passwdSha1, String context) {
        AuthRequest.LoginReq auth = new AuthRequest.LoginReq(username, passwdSha1, AuthRequest.STRATEGY.NONE, null,
                context);
        //将Auth数据转为byte[]再发送出去
        LoginRequest.LoginWithAuthReq req = new LoginRequest.LoginWithAuthReq();
        req.mAuthData = auth.marshall();
        return SendLoginReq(req);
    }


    /**
     * 订阅频道内的广播请求（频道内人数在线变更）
     *
     * @param topSid 大频道subsid
     */
    public static void SessSubOnlineUserBroadcastReq(long topSid) {
        //订阅频道内的广播请求（频道内人数在线变更）
        SessRequest.SessSubBroadcastReq sessReq = new SessRequest.SessSubBroadcastReq(topSid, true, SDKParam.ChannelSvcType.ONLINE_USER_BC_SVC);
        HandlerRequestResult(defaultInstance.mISession.sendRequest(sessReq));
    }

    /**
     * 订阅service平台业务
     *
     * @param svcTypes 订阅类型数组
     */
    public static void SvcSubscribeReq(int[] svcTypes) {
        SvcRequest.SvcSubscribeReq svcreq = new SvcRequest.SvcSubscribeReq(svcTypes);
        HandlerRequestResult(defaultInstance.mISvc.sendRequest(svcreq));
    }

    /**
     * app状态请求
     *
     * @param isForeGround
     */
    public static void AppStatusReq(boolean isForeGround) {
        LoginRequest.AppStatusReq req = new LoginRequest.AppStatusReq(isForeGround);
        HandlerRequestResult(SendLoginReq(req));
    }

    public static void SvcTextChatReq(int svcType, long topSid, long subSid, String msg, String ctx) {
        LogUtils.e("SvcTextChatReq,svcType:" + svcType + ",topSid:" + topSid + ",subsid:" + subSid + ",msg:" + msg + ",ctx:" + ctx);
        SvcRequest.SvcTextChatReq req = new SvcRequest.SvcTextChatReq(svcType, topSid, subSid, 0, msg);
        req.setCtx(ctx);
        HandlerRequestResult(defaultInstance.mISvc.sendRequest(req));
    }

//
//    /**
//     * 通过指定UID方式登录
//     *
//     * @param uid      指定uid
//     * @param tokenHex token
//     */
//    public static void loginToProtoServerByUid(long uid, String tokenHex) {
//        ProtoReq.LoginByUidReq _LoginByUidReq = new ProtoReq.LoginByUidReq(uid, tokenHex);
//        HandlerRequestResult(sendProtoRequest(_LoginByUidReq.getBytes()));
//    }
//
//
//    /**
//     * 进频道请求
//     *
//     * @param subSid      频道标识
//     * @param tokenHex token
//     */
//    public static void joinQueue(int subSid, String tokenHex) {
//        ProtoReq.SessJoinReq _SessJoinReq = new ProtoReq.SessJoinReq(subSid);
//        _SessJoinReq.topSid = subSid;
//        _SessJoinReq.subSid = 0;
//        _SessJoinReq.appToken = tokenHex;
//        HandlerRequestResult(sendProtoRequest(_SessJoinReq.getBytes()));
//    }
//
//    /**
//     * 进频道请求
//     *
//     * @param subSid      频道标识
//     * @param subSid   子频道标识
//     * @param tokenHex token
//     */
//    public static void joinQueue(int subSid, int subSid, String tokenHex) {
//        ProtoReq.SessJoinReq _SessJoinReq = new ProtoReq.SessJoinReq(subSid);
//        _SessJoinReq.topSid = subSid;
//        _SessJoinReq.subSid = subSid;
//        _SessJoinReq.appToken = tokenHex;
//        HandlerRequestResult(sendProtoRequest(_SessJoinReq.getBytes()));
//    }
//
//    /**
//     * 发送信令消息
//     *
//     * @param subSid     频道标识
//     * @param message 消息体
//     */
//    public static void sendProtoTestMsg(int subSid, String message) {
//        ProtoReq.SessTextChatReq textChatReq = new ProtoReq.SessTextChatReq();
//        textChatReq.chat = message;
//        textChatReq.topSid = subSid;
//        HandlerRequestResult(sendProtoRequest(textChatReq.getBytes()));
//    }
//
//    /**
//     * 发送信令消息,带自定义消息
//     *
//     * @param subSid
//     * @param context
//     * @param message
//     */
//    public static void sendProtoTestMsg(int subSid, String context, String message) {
//        ProtoReq.SessTextChatReq textChatReq = new ProtoReq.SessTextChatReq();
//        textChatReq.chat = message;
//        textChatReq.topSid = subSid;
//        textChatReq.context = context;
//        HandlerRequestResult(sendProtoRequest(textChatReq.getBytes()));
//    }
//
//
//    /**
//     * 获取频道内成员信息请求
//     *
//     * @param subSid 频道标识
//     */
//    public static void getQueueMembers(int subSid) {
//        ProtoReq.SessPullUserListReq _SessPullUserListReq = new ProtoReq.SessPullUserListReq();
//        _SessPullUserListReq.topSid = subSid;
//        HandlerRequestResult(sendProtoRequest(_SessPullUserListReq.getBytes()));
//    }


}
