package com.xiaoxuetu.route.model;

/**
 * Created by kevin on 2017/2/9.
 */

public class Route {

    public int id;

    public String aliasName;

    public String wifiName;

    public String macAddress;

    public boolean isOnFocus;

    public String ip;

    public String password;

    public Route() {}

    public Route(String aliasName, String wifiName, String macAddress, boolean isOnFocus, String ip, String password) {
        this.aliasName = aliasName;
        this.wifiName = wifiName;
        this.macAddress = macAddress;
        this.isOnFocus = isOnFocus;
        this.ip = ip;
        this.password = password;
    }

    public Route(int id, String aliasName, String wifiName, String macAddress, boolean isOnFocus, String ip, String password) {
        this.id = id;
        this.aliasName = aliasName;
        this.wifiName = wifiName;
        this.macAddress = macAddress;
        this.isOnFocus = isOnFocus;
        this.ip = ip;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RouteApi{" +
                "id=" + id +
                ", aliasName='" + aliasName + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", isOnFocus=" + isOnFocus +
                ", ip='" + ip + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
