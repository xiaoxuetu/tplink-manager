package com.xiaoxuetu.tplink.data.route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaoxuetu.route.model.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/3/20.
 */

public class RouteLocalDataRepository implements RouteDataSource {

    private static RouteLocalDataRepository INSTANCE;

    private RouteDBHelper mDBHelper;

    private SQLiteDatabase mDB;

    private RouteLocalDataRepository(@NonNull Context context) {
        mDBHelper = new RouteDBHelper(context);
        mDB = mDBHelper.getWritableDatabase();
    }

    public static RouteLocalDataRepository getInstance(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("context 的值不能为空");
        }

        if (INSTANCE == null) {
            INSTANCE = new RouteLocalDataRepository(context);
        }
        return INSTANCE;
    }

    @Override
    @NonNull
    public List<Route> findAll() {
        List<Route> routeList = new ArrayList<>();

        Cursor cursor = mDB.query(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            return routeList;
        }

        while (cursor.moveToNext()) {
            Route route = changeCursorToObject(cursor);
            routeList.add(route);
        }
        cursor.close();
        return routeList;
    }

    @Override
    @Nullable
    public Route findByMacAddress(@NonNull String macAddress) {
        Route route = null;
        String selection = RoutePersistenceContract.RouteEntry.COLUMN_NAME_MAC_ADDRESS + "LIKE ?";
        String[] selectionArgs = {macAddress};
        Cursor cursor = mDB.query(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            return route;
        }

        cursor.moveToFirst();
        route = changeCursorToObject(cursor);
        cursor.close();
        return route;
    }

    @Override
    public long save(@NonNull Route route) {
        ContentValues contentValues = changeObjectToContentValues(route);
        long id = mDB.insert(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, contentValues);
        return id;
    }

    @Override
    public long update(@NonNull Route route) {
        ContentValues values = changeObjectToContentValues(route);
        int affectedRowNumber = mDB.update(RoutePersistenceContract.RouteEntry.TABLE_NAME, values, "id = ?", new String[] {String.valueOf(route.id)});
        return affectedRowNumber;
    }

    @Override
    public Route findOnFocusRoute() {
        Cursor cursor = mDB.query(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, "on_focus = ?", new String[]{"1"}, null, null,null);

        Route route = null;
        if (cursor != null
                && cursor.getCount() > 0
                && cursor.moveToFirst()) {
            route = changeCursorToObject(cursor);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return route;
    }

    @Override
    public boolean isExists(@NonNull String macAddress) {
        Cursor cursor = mDB.query(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, "mac_address = ?", new String[]{macAddress}, null, null,null);

        boolean isExists = false;

        if (cursor != null && cursor.getCount() > 0) {
            isExists = true;
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return isExists;
    }

    @Override
    public void updateAllRouteToUnFocus() {
        ContentValues values = new ContentValues();
        values.put("on_focus", 0);
        mDB.update(RoutePersistenceContract.RouteEntry.TABLE_NAME, values, "on_focus = ?", new String[]{"1"});
    }

    private ContentValues changeObjectToContentValues(@NonNull Route route) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_IP, route.ip);
        contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_PASSWORD, route.password);
        contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_MAC_ADDRESS, route.macAddress);
        contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_WIFI_NAME, route.wifiName);
        contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_DEVICE_ALIAS, route.aliasName);

        if (route.isOnFocus) {
            contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_ON_FOCUS,
                    RoutePersistenceContract.COLUMN_VALUE_ON_FOCUS);
        } else {
            contentValues.put(RoutePersistenceContract.RouteEntry.COLUMN_NAME_ON_FOCUS,
                    RoutePersistenceContract.COLUMN_VALUE_UN_FOCUS);
        }
        return contentValues;
    }

    private Route changeCursorToObject(@NonNull Cursor cursor) {
        Route route = new Route();
        route.id = cursor.getInt(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry._ID));
        route.ip = cursor.getString(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_IP));
        route.password = cursor.getString(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_PASSWORD));
        route.macAddress = cursor.getString(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_MAC_ADDRESS));
        route.wifiName = cursor.getString(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_WIFI_NAME));
        route.aliasName = cursor.getString(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_DEVICE_ALIAS));

        int onFocus = cursor.getInt(cursor.getColumnIndex(RoutePersistenceContract.RouteEntry.COLUMN_NAME_ON_FOCUS));

        if (onFocus == RoutePersistenceContract.COLUMN_VALUE_UN_FOCUS) {
            route.isOnFocus = false;
        } else {
            route.isOnFocus = true;
        }
        return route;
    }
}
