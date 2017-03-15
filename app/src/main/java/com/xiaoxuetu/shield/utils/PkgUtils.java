package com.xiaoxuetu.shield.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by kevin on 2017/3/16.
 */

public class PkgUtils {

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isApkInRelease(Context context) {
        return !isApkInDebug(context);
    }
}
