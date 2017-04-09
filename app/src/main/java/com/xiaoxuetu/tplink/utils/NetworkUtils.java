package com.xiaoxuetu.tplink.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by kevin on 2017/4/10.
 */

public class NetworkUtils {


    public static final int NO_NETWORK = -1;

    public static final int WIFI_NETWORK = 1;

    public static final int WAP_NETWORK = 2;

    public static final int NET_NETWORK = 3;

    //返回值 -1：没有网络  1：WIFI网络2：wap网络3：net网络
    public static int getNetype(Context context)
    {
        int netType = NO_NETWORK;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null)
        {
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE)
        {
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
            {
                netType = NET_NETWORK;
            }
            else
            {
                netType = WAP_NETWORK;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI)
        {
            netType = WIFI_NETWORK;
        }
        return netType;
    }

}
