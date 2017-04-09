package com.xiaoxuetu.tplink.data.route;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xiaoxuetu.tplink.BaseDBHelper;

/**
 * Created by kevin on 2017/3/20.
 */

public class RouteDBHelper extends BaseDBHelper {

    private final String TAG = getClass().getSimpleName();

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Routes.db";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RoutePersistenceContract.RouteEntry.TABLE_NAME + " (" +
                    RoutePersistenceContract.RouteEntry._ID + INTEGER_TYPE + " primary key autoincrement" + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_IP + TEXT_TYPE + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_MAC_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_WIFI_NAME + TEXT_TYPE + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_DEVICE_ALIAS + TEXT_TYPE + COMMA_SEP +
                    RoutePersistenceContract.RouteEntry.COLUMN_NAME_ON_FOCUS + BOOLEAN_TYPE +
            ")";


    public RouteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create route table");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库版本号为1，暂时不做处理
    }
}
