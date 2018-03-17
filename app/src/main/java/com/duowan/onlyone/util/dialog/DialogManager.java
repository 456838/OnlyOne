package com.duowan.onlyone.util.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Html;


import com.salton123.util.LogUtils;
import com.salton123.util.PreferencesUtils;


/**
 * User: 巫金生(newSalton@outlook.com)
 * Date: 2017/3/24 16:49
 * Time: 16:49
 * Description:
 */
public class DialogManager {

    /**
     * 登录多端互踢
     */
    public static void showKickoffDialog(final Activity pActivity, String pReason) {
        LogUtils.e("showKickoffDialog:" + pReason);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(pActivity);
        builder.
                setTitle("Hi").
                setMessage(Html.fromHtml(pReason)).
                setPositiveButton("嗯", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PreferencesUtils.putBoolean(pActivity, "autoLogin", false);
//                        pActivity.startActivity(new Intent(pActivity, LoginActivity.class));
//                        pActivity.finish();
                    }
                }).show();
    }

    /**
     * 退出Application
     *
     * @param pActivity
     * @param pReason
     */
    public static void showExitDialog(final Activity pActivity, String pReason) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(pActivity);
        builder.
                setTitle("Hi").
                setMessage(Html.fromHtml(pReason)).
                setPositiveButton("嗯", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                }).show();
    }


}
