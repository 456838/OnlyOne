package com.salton123.xm.util;

import android.support.annotation.IdRes;
import android.view.View;

/**
 * User: newSalton@outlook.com
 * Date: 2017/7/29 21:47
 * ModifyTime: 21:47
 * Description:
 */
public class ViewUtils {
    /**
     * 查找View
     *
     * @param itemView   控件父类
     * @param resId 控件id
     * @return
     */
    public static <T extends View> T f(View itemView, @IdRes int resId) {
        return (T) itemView.findViewById(resId);
    }
}
