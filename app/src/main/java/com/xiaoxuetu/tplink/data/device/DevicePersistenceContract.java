package com.xiaoxuetu.tplink.data.device;

import android.provider.BaseColumns;

/**
 * Created by kevin on 2017/4/9.
 */

public final class DevicePersistenceContract {

    private DevicePersistenceContract() {}

    public static abstract class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "device";
        public static final String COLUMN_NAME_IP = "ip";
        public static final String COLUMN_NAME_MAC_ADDRESS = "mac_address";
        public static final String COLUMN_NAME_DEVICE_NAME = "device_name";
        public static final String COLUMN_NAME_DEVICE_ALIAS = "alias_name";
        public static final String COLUMN_NAME_ROUTE_ID = "route_id";
        public static final String COLUMN_NAME_ONLINE = "on_line";
    }



    /**
     * 设备在线
     */
    public static final int COLUMN_VALUE_ONLINE = 1;

    /**
     * 设备不在线
     */
    public static final int COLUMN_VALUE_OFFLINE = 0;
}
