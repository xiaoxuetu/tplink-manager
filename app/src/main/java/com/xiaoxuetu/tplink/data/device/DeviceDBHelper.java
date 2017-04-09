package com.xiaoxuetu.tplink.data.device;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xiaoxuetu.tplink.BaseDBHelper;
import com.xiaoxuetu.tplink.data.device.DevicePersistenceContract.DeviceEntry;

/**
 * Created by kevin on 2017/4/9.
 */

public class DeviceDBHelper extends BaseDBHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Devices.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DeviceEntry.TABLE_NAME + " (" +
                    DeviceEntry._ID + INTEGER_TYPE + " primary key autoincrement" + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_IP + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_DEVICE_ALIAS + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_MAC_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_DEVICE_NAME + TEXT_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_ONLINE + BOOLEAN_TYPE + COMMA_SEP +
                    DeviceEntry.COLUMN_NAME_ROUTE_ID + INTEGER_TYPE +
                    ")";



    public DeviceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create device table");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本为1，暂时不做其他处理
    }
}
