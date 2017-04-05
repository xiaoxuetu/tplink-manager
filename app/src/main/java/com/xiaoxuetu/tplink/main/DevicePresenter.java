package com.xiaoxuetu.tplink.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Looper;

import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by kevin on 2017/3/29.
 */

public class DevicePresenter implements DeviceContract.Presenter {

    private RouteApi mRouteApi;
    private DeviceContract.View mView;
    private RouteLocalDataRepository mRouteLocalDataRepository;

    public DevicePresenter(RouteLocalDataRepository routeLocalDataRepository, RouteApi routeApi, DeviceContract.View view) {
        this.mRouteLocalDataRepository = routeLocalDataRepository;
        this.mRouteApi = routeApi;
        this.mView = view;
        view.setPresenter(this);

    }

    @Override
    public void loadDevices() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonResult commonResult = mRouteApi.getOnlineDevices();

                if (commonResult.isFailure()) {
                    String msg = commonResult.getMessage();
                    Looper.prepare();
                    mView.showFailureMessage(msg);
                    Looper.loop();
                    return;
                }

                List<Device> deviceList = (List<Device>) commonResult.getData();

                if (deviceList == null || deviceList.isEmpty()) {
                    mView.showNoDevices();
                    return;
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

                Looper.prepare();
                if (commonResult.isFailure()) {
                    String msg = commonResult.getMessage();
                    mView.showFailureMessage(msg);
                    return;
                }

                Route route = (Route) commonResult.getData();
                deviceActivity.showRoute(route);
                Looper.loop();
            }
        }).start();

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
