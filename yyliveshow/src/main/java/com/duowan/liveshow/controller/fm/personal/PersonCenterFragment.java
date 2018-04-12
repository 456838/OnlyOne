package com.duowan.liveshow.controller.fm.personal;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.duowan.liveshow.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.salton123.base.BaseSupportFragment;
import com.salton123.common.image.FrescoImageLoader;
import com.salton123.util.LogUtils;
import com.salton123.util.PreferencesUtils;
import com.yy.live.core.CoreManager;
import com.yy.live.core.auth.IAuthCore;
import com.yy.live.model.bean.LoginUserInfo;
import com.yy.live.model.engine.YYEngine;
import com.yy.udbauth.AuthEvent;
import com.yy.udbauth.ui.AuthUI;
import com.yy.udbauth.ui.style.PageStyle;
import com.yy.udbauth.ui.tools.OnUdbAuthListener;
import com.yy.udbauth.ui.tools.OpreateType;

/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/6/27 20:49
 * Time: 20:49
 * Description:
 */
public class PersonCenterFragment extends BaseSupportFragment implements View.OnClickListener {
    private SimpleDraweeView sdv_thumbnail;
    private TextView tv_signature;
    private TextView tv_nickname;
    private TextView tv_id;
    Button btn_logout;
    private String demoPluginName = "plugindemo01-debug.apk";

    @Override
    public int GetLayout() {
        return R.layout.fm_personal_center;
    }

    @Override
    public void InitVariable(Bundle savedInstanceState) {

    }

    @Override
    public void InitViewAndData() {
        sdv_thumbnail = f(R.id.sdv_thumbnail);
        tv_signature = f(R.id.tv_signature);
        tv_nickname = f(R.id.tv_nickname);
        tv_id = f(R.id.tv_user_id);
        btn_logout = f(R.id.btn_logout);
    }

    @Override
    public void InitListener() {
        f(R.id.rel_provider_setting).setOnClickListener(this);
        f(R.id.rel_address).setOnClickListener(this);
        f(R.id.rel_feedback).setOnClickListener(this);
        f(R.id.rl_user_profile_header).setOnClickListener(this);
        f(R.id.btn_logout).setOnClickListener(this);
        f(R.id.rel_aboutus).setOnClickListener(this);
        f(R.id.rel_connect).setOnClickListener(this);
        f(R.id.rel_guide).setOnClickListener(this);
        f(R.id.rel_protocol).setOnClickListener(this);
        f(R.id.tv_xianzhi).setOnClickListener(this);
        f(R.id.tv_jianzhi).setOnClickListener(this);
        f(R.id.tv_liuxue).setOnClickListener(this);
        f(R.id.tv_yiliao).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.rel_provider_setting) {

        } else if (i == R.id.rl_user_profile_header) {
            if (!CoreManager.getCore(IAuthCore.class).isLogined()) {
                PageStyle ps = new PageStyle(getContext());
                ps.titlebarColor = getResources().getColor(R.color.colorPrimary);
                ps.titlebarTextColor = Color.WHITE;
                ps.buttonColor = Color.parseColor("#2ed3b8");
                ps.buttonPressedColor = Color.parseColor("#23a993");
                ps.textStrikingColor = Color.parseColor("#2ed3b8");
                //使用全局样式
                AuthUI.getInstance().resetLayoutRes();
                AuthUI.getInstance().setGlobalPageStyle(ps);
                AuthUI.getInstance().showLoginActivity(getActivity(), onUdbAuthListener);
            } else {
                toast("资料编辑页");
            }
        } else if (i == R.id.rel_address) {
        } else if (i == R.id.rel_aboutus) {
        } else if (i == R.id.rel_guide) {
        } else if (i == R.id.rel_connect) {

        } else if (i == R.id.rel_protocol) {

        } else if (i == R.id.tv_xianzhi) {

        } else if (i == R.id.tv_jianzhi) {

        } else if (i == R.id.tv_liuxue) {

        } else if (i == R.id.tv_yiliao) {

        } else if (i == R.id.btn_logout) {

        } else if (i == R.id.rel_feedback) {

        }
    }

    LoginUserInfo mLoginUserInfo = new LoginUserInfo();

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mLoginUserInfo = CoreManager.getCore(IAuthCore.class).getLoginUserInfo();
        updateUserInfoUI(mLoginUserInfo);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mLoginUserInfo = CoreManager.getCore(IAuthCore.class).getLoginUserInfo();
        updateUserInfoUI(mLoginUserInfo);
    }

    private void updateUserInfoUI(LoginUserInfo mUser) {
        if (mUser != null) {
            FrescoImageLoader.displayCircle(sdv_thumbnail, mUser.iconUrl);
            tv_nickname.setText("" + mUser.nickName);
            tv_id.setText("" + mUser.yyId);
            tv_signature.setText("" + mUser.signature);
            btn_logout.setVisibility(View.VISIBLE);
        } else {
            tv_nickname.setText("昵称未知");
            tv_id.setText("" + "用户ID");
            tv_signature.setText("个性签名");
            sdv_thumbnail.setImageResource(R.drawable.yy_bear_logo);
            btn_logout.setVisibility(View.GONE);
        }
    }


    OnUdbAuthListener onUdbAuthListener = new OnUdbAuthListener() {

        @Override
        public void onSuccess(AuthEvent.AuthBaseEvent event, OpreateType type) {
            //CoreManager.notifyClients(IAuthClient.class, "onLoginSucceed", event.getUid());
            //显示用户的基本信息
            final StringBuilder sb = new StringBuilder();
            String uid = null;
            String yyid = null;
            String passport = null;
            String credit = null;
            String emailMask = null;
            String mobileMask = null;
            switch (type) {
                case FIND_MY_PWD:
                    sb.append("操作：找回密码==>>成功");
                    passport = ((AuthEvent.SmsModPwdEvent) event).passport;
                    credit = ((AuthEvent.SmsModPwdEvent) event).credit;
                    uid = ((AuthEvent.SmsModPwdEvent) event).uid;
                    yyid = ((AuthEvent.SmsModPwdEvent) event).yyid;
                    emailMask = ((AuthEvent.SmsModPwdEvent) event).emailMask;
                    mobileMask = ((AuthEvent.SmsModPwdEvent) event).mobileMask;
                    break;
                case MODIFY_PWD:
                    sb.append("操作：修改密码==>>成功");
                    passport = ((AuthEvent.SmsModPwdEvent) event).passport;
                    credit = ((AuthEvent.SmsModPwdEvent) event).credit;
                    uid = ((AuthEvent.SmsModPwdEvent) event).uid;
                    yyid = ((AuthEvent.SmsModPwdEvent) event).yyid;
                    emailMask = ((AuthEvent.SmsModPwdEvent) event).emailMask;
                    mobileMask = ((AuthEvent.SmsModPwdEvent) event).mobileMask;
                    break;
                case SMS_REGISTER:
                    sb.append("操作：短信注册==>>成功");
                    boolean islogin = AuthUI.getInstance().getPageSetting().smsRegisterPage_autoLogin;
                    if (!islogin) {
                        passport = ((AuthEvent.RegisterEvent) event).passport;
                        uid = ((AuthEvent.RegisterEvent) event).uid;
                        yyid = ((AuthEvent.RegisterEvent) event).yyid;
                    } else {
                        passport = ((AuthEvent.LoginEvent) event).passport;
                        credit = ((AuthEvent.LoginEvent) event).credit;
                        uid = ((AuthEvent.LoginEvent) event).uid;
                        yyid = ((AuthEvent.LoginEvent) event).yyid;
                        emailMask = ((AuthEvent.LoginEvent) event).emailMask;
                        mobileMask = ((AuthEvent.LoginEvent) event).mobileMask;

                        AuthUI.getInstance().getDatabase().deleteAllAccounts();
                        AuthUI.getInstance().getDatabase().addOrReplace(uid, yyid, passport, credit, null, null, null);
                        AuthUI.getInstance().getDatabase().setActiviedAccount(uid);
                    }
                    break;
                case SMS_LOGIN:
                    sb.append("操作：短信登录==>>成功");
                    passport = ((AuthEvent.LoginEvent) event).passport;
                    credit = ((AuthEvent.LoginEvent) event).credit;
                    uid = ((AuthEvent.LoginEvent) event).uid;
                    yyid = ((AuthEvent.LoginEvent) event).yyid;
                    emailMask = ((AuthEvent.LoginEvent) event).emailMask;
                    mobileMask = ((AuthEvent.LoginEvent) event).mobileMask;
                    AuthUI.getInstance().getDatabase().deleteAllAccounts();
                    AuthUI.getInstance().getDatabase().addOrReplace(uid, yyid, passport, credit, null, null, null);
                    AuthUI.getInstance().getDatabase().setActiviedAccount(uid);
                    break;
                case PWD_LOGIN:
                    sb.append("操作：密码登录==>>成功");
                    passport = ((AuthEvent.LoginEvent) event).passport;
                    credit = ((AuthEvent.LoginEvent) event).credit;
                    uid = ((AuthEvent.LoginEvent) event).uid;
                    yyid = ((AuthEvent.LoginEvent) event).yyid;
                    emailMask = ((AuthEvent.LoginEvent) event).emailMask;
                    mobileMask = ((AuthEvent.LoginEvent) event).mobileMask;
                    AuthUI.getInstance().getDatabase().deleteAllAccounts();
                    AuthUI.getInstance().getDatabase().addOrReplace(uid, yyid, passport, credit, null, null, null);
                    AuthUI.getInstance().getDatabase().setActiviedAccount(uid);
                    break;
                case CREDIT_LOGIN:
                    sb.append("操作：凭证登录==>>成功");
                    passport = ((AuthEvent.LoginEvent) event).passport;
                    credit = ((AuthEvent.LoginEvent) event).credit;
                    uid = ((AuthEvent.LoginEvent) event).uid;
                    yyid = ((AuthEvent.LoginEvent) event).yyid;
                    emailMask = ((AuthEvent.LoginEvent) event).emailMask;
                    mobileMask = ((AuthEvent.LoginEvent) event).mobileMask;
                    AuthUI.getInstance().getDatabase().deleteAllAccounts();
                    AuthUI.getInstance().getDatabase().addOrReplace(uid, yyid, passport, credit, null, null, null);
                    AuthUI.getInstance().getDatabase().setActiviedAccount(uid);
                    break;
                case NEXT_VERIFY:
                    sb.append("操作：二次验证==>>成功");
                    passport = ((AuthEvent.LoginEvent) event).passport;
                    credit = ((AuthEvent.LoginEvent) event).credit;
                    uid = ((AuthEvent.LoginEvent) event).uid;
                    yyid = ((AuthEvent.LoginEvent) event).yyid;
                    emailMask = ((AuthEvent.LoginEvent) event).emailMask;
                    mobileMask = ((AuthEvent.LoginEvent) event).mobileMask;
                    AuthUI.getInstance().getDatabase().deleteAllAccounts();
                    AuthUI.getInstance().getDatabase().addOrReplace(uid, yyid, passport, credit, null, null, null);
                    AuthUI.getInstance().getDatabase().setActiviedAccount(uid);
                    break;
                default:
                    sb.append("未能识别你所做的操作：");
                    break;
            }

            sb.append("\n 账号信息：");
            sb.append("\n uid：");
            sb.append(uid);
            sb.append("\n passport：");
            sb.append(passport);
            sb.append("\n yyid：");
            sb.append(yyid);
            sb.append("\n emailMask：");
            sb.append(emailMask);
            sb.append("\n mobileMask：");
            sb.append(mobileMask);
            LogUtils.e("onUdbAuthListener" + sb.toString());
            boolean isreglogin = AuthUI.getInstance().getPageSetting().smsRegisterPage_autoLogin;
            YYEngine.GetOneIMUInfoReq("loginUserInfo", YYEngine.GetCurrentUid());
            if (type == OpreateType.CREDIT_LOGIN || type == OpreateType.PWD_LOGIN || type == OpreateType.SMS_LOGIN
                    || type == OpreateType.NEXT_VERIFY || (type == OpreateType.SMS_REGISTER && isreglogin)) {
                //设置为在线状态==>>并返回主页面
                YYEngine.getInstance().setOnline(true);
                PreferencesUtils.putBoolean(_mActivity, "autoLogin", true);
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        }

        @Override
        public void onCancel(OpreateType type) {
            toast("你已经取消了操作");
        }

        @Override
        public void onError(int errCode, OpreateType type) {
            toast("操作出错了 ");
        }

    };
}
