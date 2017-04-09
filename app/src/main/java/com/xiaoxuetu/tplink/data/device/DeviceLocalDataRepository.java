package com.xiaoxuetu.tplink.data.device;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.tplink.data.device.DevicePersistenceContract.DeviceEntry;
import com.xiaoxuetu.tplink.data.route.RoutePersistenceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/4/9.
 */

public class DeviceLocalDataRepository implements DeviceDataSource {

    private static DeviceLocalDataRepository INSTANCE;

    private DeviceDBHelper mDBHelper;

    private SQLiteDatabase mDB;

    private DeviceLocalDataRepository(@NonNull Context context) {
        mDBHelper = new DeviceDBHelper(context);
        mDB = mDBHelper.getWritableDatabase();
    }

    public static DeviceLocalDataRepository getInstance(@NonNull Context context) {
        if (context == null) {
            throw new NullPointerException("context 的值不能为空");
        }

        if (INSTANCE == null) {
            INSTANCE = new DeviceLocalDataRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Device> findAll() {
        List<Device> list = new ArrayList<>();
        Cursor cursor = mDB.query(DeviceEntry.TABLE_NAME, null, null, null, null, null, null);

        if (cursor == null || cursor.getCount() <= 0) {
            return list;
        }

        while (cursor.moveToNext()) {
            Device device = changeCursorToObject(cursor);
            list.add(device);
        }
        cursor.close();
        return list;
    }

    @Override
    public Device findByMacAddress(String macAddress, long routeId) {
        Device device;
        String routeIdStr = routeId == 0 ? null : String.valueOf(routeId);
        String selection = DeviceEntry.COLUMN_NAME_MAC_ADDRESS + "= ? ";

        if (!TextUtils.isEmpty(routeIdStr)) {
            selection = selection + "AND " + DeviceEntry.COLUMN_NAME_ROUTE_ID + "= ?";
        }
        String[] selectionArgs = {macAddress};

        if (!TextUtils.isEmpty(routeIdStr)) {
            selectionArgs = new String[]{macAddress, routeIdStr};
        }
        Cursor cursor = mDB.query(RoutePersistenceContract.RouteEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        device = changeCursorToObject(cursor);
        cursor.close();
        return device;
    }

    @Override
    public Device findByMacAddress(String macAddress) {
        return findByMacAddress(macAddress, 0);
    }

    @Override
    public boolean isExists(String macAddress, long routeId) {
        Device device =  findByMacAddress(macAddress, routeId);

        if (device == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isExists(String macAddress) {
        return isExists(macAddress, 0);
    }

    @Override
    public long save(Device device) {
        ContentValues contentValues = changeObjectToContentValues(device);
        long id = mDB.insert(DeviceEntry.TABLE_NAME, null, contentValues);
        return id;
    }

    @Override
    public long update(Device device) {
        ContentValues values = changeObjectToContentValues(device);
        int affectedRowNumber = mDB.update(DeviceEntry.TABLE_NAME, values, "id = ? and route_id = ?",
                new String[] {String.valueOf(device.id), String.valueOf(device.routeId)});
        return affectedRowNumber;
    }


    private Device changeCursorToObject(Cursor cursor) {
        Device device = new Device();
        device.id = cursor.getInt(cursor.getColumnIndex(DeviceEntry._ID));
        device.ipAddress = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_NAME_IP));
        device.deviceName = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_NAME_DEVICE_NAME));
        device.aliasName = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_NAME_DEVICE_ALIAS));

        int isOnLineInt = cursor.getInt(cursor.getColumnIndex(DeviceEntry.COLUMN_NAME_ONLINE));

        if (DevicePersistenceContract.COLUMN_VALUE_ONLINE == isOnLineInt) {
            device.isOnLine = true;
        } else {
            device.isOnLine = false;
        }
        return device;
    }

    private ContentValues changeObjectToContentValues(Device device) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DeviceEntry.COLUMN_NAME_IP, device.ipAddress);
        contentValues.put(DeviceEntry.COLUMN_NAME_ROUTE_ID, device.routeId);
        contentValues.put(DeviceEntry.COLUMN_NAME_MAC_ADDRESS, device.macAddress);
        contentValues.put(DeviceEntry.COLUMN_NAME_DEVICE_ALIAS, device.aliasName);
        contentValues.put(DeviceEntry.COLUMN_NAME_DEVICE_NAME, device.deviceName);

        if (device.isOnLine) {
            contentValues.put(DeviceEntry.COLUMN_NAME_ONLINE,
                    DevicePersistenceContract.COLUMN_VALUE_ONLINE);
        } else {
            contentValues.put(DeviceEntry.COLUMN_NAME_ONLINE,
                    DevicePersistenceContract.COLUMN_VALUE_OFFLINE);
        }
        return contentValues;
    }
}
