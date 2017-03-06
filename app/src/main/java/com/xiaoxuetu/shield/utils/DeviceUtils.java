package com.xiaoxuetu.shield.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.xiaoxuetu.route.util.Formatter;

/**
 * Created by kevin on 2017/2/9.
 */

public class DeviceUtils {

    public static String getMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String macAddress = info.getMacAddress();
        macAddress = Formatter.formatMacAddress(macAddress);
        return macAddress;
    }
}
