package com.xiaoxuetu.tplink.main;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.TpLinkApplication;
import com.xiaoxuetu.tplink.data.device.DeviceLocalDataRepository;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;
import com.xiaoxuetu.tplink.utils.NetworkUtils;

import java.util.List;

/**
 * Created by kevin on 2017/3/29.
 */

public class DevicePresenter implements DeviceContract.Presenter {

    private final String TAG = getClass().getSimpleName();

    private RouteApi mRouteApi;
    private DeviceContract.View mView;
    private RouteLocalDataRepository mRouteLocalDataRepository;
    private DeviceLocalDataRepository mDeviceLocalDataRepository;

    public DevicePresenter(RouteLocalDataRepository routeLocalDataRepository,
                           DeviceLocalDataRepository deviceLocalDataRepository,
                           RouteApi routeApi, DeviceContract.View view) {
        this.mRouteLocalDataRepository = routeLocalDataRepository;
        this.mDeviceLocalDataRepository = deviceLocalDataRepository;
        this.mRouteApi = routeApi;
        this.mView = view;
        view.setPresenter(this);

    }

    @Override
    public void loadDevices() {
        Route route = mRouteLocalDataRepository.findOnFocusRoute();
        final long routeId = route.id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonResult commonResult = mRouteApi.getOnlineDevices();

                if (commonResult.isFailure()) {
                    String msg = commonResult.getMessage();
                    mView.showFailureMessage(msg);
                    return;
                }

                List<Device> deviceList = (List<Device>) commonResult.getData();

                if (deviceList == null || deviceList.isEmpty()) {
                    mView.showNoDevices();
                    return;
                }

                for (Device device: deviceList) {
                    boolean isExists = mDeviceLocalDataRepository.isExists(device.macAddress, routeId);
                    // 存在的就获取设备的别名
                    if (isExists) {
                        Device devicePO = mDeviceLocalDataRepository.findByMacAddress(device.macAddress, routeId);
                        device.aliasName = devicePO.aliasName;
                    } else {
                        device.aliasName = device.deviceName;
                        mDeviceLocalDataRepository.save(device);
                    }
                }
                mView.showDevices(deviceList);

            }
        }).start();

    }

    @Override
    public void loadRoute(final DeviceActivity deviceActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonResult commonResult = mRouteApi.getRoute();

                if (commonResult.isFailure()) {
                    String msg = commonResult.getMessage();
                    mView.showFailureMessage(msg);
                    return;
                }

                Route route = (Route) commonResult.getData();
                deviceActivity.showRoute(route);
            }
        }).start();
    }

    public void judgeNetState(final DeviceActivity deviceActivity) {
        int networkState = NetworkUtils.getNetype(deviceActivity.getBaseContext());
        Log.d(TAG, "网络状态是 " + networkState);
        if (networkState == NetworkUtils.NO_NETWORK) {
            deviceActivity.showOpenNetworkTips();
        } else if (networkState != NetworkUtils.WIFI_NETWORK) {
            deviceActivity.showSwitchWifiTips();
        } else {
            deviceActivity.dismissNetWorkTips();
        }
    }

    @Override
    public void start() {
        final Route route = mRouteLocalDataRepository.findOnFocusRoute();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mRouteApi.login(route.ip, route.password);
                loadDevices();
            }
        }).start();
    }
}
