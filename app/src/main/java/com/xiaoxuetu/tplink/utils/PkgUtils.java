package com.xiaoxuetu.tplink.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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

    /**
     *检测其他应用是否处于debug模式。
     */
    public static boolean isApkInDebug(Context context,String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (pkginfo != null ) {
                ApplicationInfo info= pkginfo.applicationInfo;
                return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
            }

        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isApkInRelease(Context context,String packageName) {
        return !isApkInDebug(context, packageName);
    }
}
