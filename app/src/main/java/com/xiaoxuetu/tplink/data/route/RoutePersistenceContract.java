package com.xiaoxuetu.tplink.data.route;

import android.provider.BaseColumns;

/**
 * Created by kevin on 2017/3/20.
 */

public final class RoutePersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private RoutePersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME = "route";
        public static final String COLUMN_NAME_IP = "ip";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_MAC_ADDRESS = "mac_address";
        public static final String COLUMN_NAME_WIFI_NAME = "wifi_name";
        public static final String COLUMN_NAME_DEVICE_ALIAS = "device_alias";
        public static final String COLUMN_NAME_ON_FOCUS = "on_focus";
    }


    /**
     * 用于表示该路由器处于关注阶段
     */
    public static final int COLUMN_VALUE_ON_FOCUS = 1;

    /**
     * 用于标准路由器不在关注阶段
     */
    public static final int COLUMN_VALUE_UN_FOCUS = 0;
}
