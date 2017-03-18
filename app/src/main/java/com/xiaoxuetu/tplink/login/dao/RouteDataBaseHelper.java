package com.xiaoxuetu.tplink.login.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kevin on 2017/2/9.
 */

public class RouteDataBaseHelper extends SQLiteOpenHelper {

    private final String TAG = this.getClass().getSimpleName();

    private static RouteDataBaseHelper sInstance;

    private final String CREATE_ROUTE_TABLE_SQL = "create table route ("
            + " id integer primary key autoincrement, "
            + " ip text,"
            + " password text, "
            + " mac_address text, "
            + " wifi_name text, "
            + " device_alias text, "
            + " on_focus integer)";


    private RouteDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private RouteDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "create route table");
        db.execSQL(CREATE_ROUTE_TABLE_SQL);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public static synchronized RouteDataBaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new RouteDataBaseHelper(context, "RouteManager.db", null, 1);
        }
        return sInstance;
    }
}
