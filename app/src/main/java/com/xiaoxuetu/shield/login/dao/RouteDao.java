package com.xiaoxuetu.shield.login.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xiaoxuetu.shield.route.model.Route;

/**
 * Created by kevin on 2017/2/10.
 */

public class RouteDao {

    private Context mContext;

    private final String TABLE_NAME = "route";

    public RouteDao(Context context) {
        mContext = context;
    }

    public long save(Route route) {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = changeObjectToContentValues(route);
        return db.insert(TABLE_NAME, null, contentValues);
    }

    public void updateAllRouteToUnFocus() {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("on_focus", 0);
        db.update(TABLE_NAME, values, "on_focus = ?", new String[]{"1"});
    }

    public boolean isRouteExists(String macAddress) {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "mac_address = ?", new String[]{macAddress}, null, null,null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public Route findOnFocusRoute() {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "on_focus = ?", new String[]{"1"}, null, null,null);

        Route route = null;
        if (cursor != null
                && cursor.getCount() > 0
                && cursor.moveToFirst()) {
            route = new Route();
            route.id = cursor.getInt(cursor.getColumnIndex("id"));
            route.ip = cursor.getString(cursor.getColumnIndex("ip"));
            route.password = cursor.getString(cursor.getColumnIndex("password"));
            route.macAddress = cursor.getString(cursor.getColumnIndex("mac_address"));
            route.wifiName = cursor.getString(cursor.getColumnIndex("wifi_name"));
            route.aliasName = cursor.getString(cursor.getColumnIndex("device_alias"));

            int onFocus = cursor.getInt(cursor.getColumnIndex("on_focus"));

            if (onFocus == 0) {
                route.isOnFocus = false;
            } else {
                route.isOnFocus = true;
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return route;
    }

    public Route findByMacAddress(String macAddress) {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "mac_address = ?", new String[]{macAddress}, null, null,null);

        Route route = null;
        if (cursor != null
                && cursor.getCount() > 0
                && cursor.moveToFirst()) {
            route = new Route();
            route.id = cursor.getInt(cursor.getColumnIndex("id"));
            route.ip = cursor.getString(cursor.getColumnIndex("ip"));
            route.password = cursor.getString(cursor.getColumnIndex("password"));
            route.macAddress = cursor.getString(cursor.getColumnIndex("mac_address"));
            route.wifiName = cursor.getString(cursor.getColumnIndex("wifi_name"));
            route.aliasName = cursor.getString(cursor.getColumnIndex("device_alias"));

            int onFocus = cursor.getInt(cursor.getColumnIndex("on_focus"));

            if (onFocus == 0) {
                route.isOnFocus = false;
            } else {
                route.isOnFocus = true;
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return route;
    }

    public boolean update(Route route) {
        RouteDataBaseHelper dbHelper = RouteDataBaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ContentValues values = changeObjectToContentValues(route);
        int affectedRowNumber = db.update(TABLE_NAME, values, "id = ?", new String[] {String.valueOf(route.id)});

        if (affectedRowNumber == 0) {
            return false;
        }
        return true;
    }

    private ContentValues changeObjectToContentValues(Route route) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ip", route.ip);
        contentValues.put("password", route.password);
        contentValues.put("mac_address", route.macAddress);
        contentValues.put("wifi_name", route.wifiName);
        contentValues.put("device_alias", route.aliasName);
        if (route.isOnFocus) {
            contentValues.put("on_focus", 1);
        } else {
            contentValues.put("on_focus", 0);
        }
        return contentValues;
    }
}
