package com.xiaoxuetu.tplink.data.device;

import com.xiaoxuetu.route.model.Device;

import java.util.List;

/**
 * Created by kevin on 2017/4/9.
 */

public interface DeviceDataSource {

    List<Device> findAll();

    Device findByMacAddress(String macAddress, long routeId);


    Device findByMacAddress(String macAddress);

    boolean isExists(String macAddress, long routeId);

    boolean isExists(String macAddress);

    long save(Device device);

    long update(Device device);
}
