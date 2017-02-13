package com.xiaoxuetu.shield.route.model;

/**
 * Created by kevin on 2017/1/10.
 */

public class Device {

    public String aliasName;

    public String deviceName;

    public String macAddress;

    public String ipAddress;

    public String validTime;


    public Device() {}

    public Device(String deviceName, String macAddress, String ipAddress, String validTime) {
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.validTime = validTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "aliasName='" + aliasName + '\'' +
                "deviceName='" + deviceName + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", validTime='" + validTime + '\'' +
                '}';
    }
}
