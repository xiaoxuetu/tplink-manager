package com.xiaoxuetu.tplink.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.data.flag.FlagDataRepository;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;

/**
 * Created by kevin on 2017/3/28.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private RouteLocalDataRepository mRouteLocalDataRepository;
    private FlagDataRepository mFlagDataRepository;
    private RouteApi mRouteApi;
    private LoginView mView;

    public LoginPresenter(RouteLocalDataRepository routeLocalDataRepository,
                          FlagDataRepository flagDataRepository,
                          RouteApi routeApi, LoginView view) {
        this.mRouteLocalDataRepository = routeLocalDataRepository;
        this.mFlagDataRepository = flagDataRepository;
        this.mRouteApi = routeApi;
        this.mView = view;
        this.mView.setPresenter(this);
    }


    @Override
    public void login(final String ip, final String password) {
        String msg = null;

        if (TextUtils.isEmpty(ip)) {
            msg = "IP地址不能为空";
        } else if (TextUtils.isEmpty(password)) {
            msg = "密码不能为空";
        }

        if (!TextUtils.isEmpty(msg)) {
            mView.showFailureMessage(msg);
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String msg = null;
                CommonResult commonResult = mRouteApi.login(ip, password);

                if (commonResult.getCode() == CommonResult.CODE_FAILURE) {
                    msg = "密码错误";
                }

                if (!TextUtils.isEmpty(msg)) {
                    Looper.prepare();
                    mView.showFailureMessage(msg);
                    Looper.loop();
                    return;
                }
                CommonResult routeInfoCommonResult = mRouteApi.getRoute();
                Route route = (Route) routeInfoCommonResult.getData();
                route.password = password;
                route.isOnFocus = true;
                mRouteLocalDataRepository.updateAllRouteToUnFocus();

                if (mRouteLocalDataRepository.isExists(route.macAddress)) {
                    Route routeTemp = mRouteLocalDataRepository.findByMacAddress(route.macAddress);
                    route.id = routeTemp.id;
                    mRouteLocalDataRepository.update(route);
                } else {
                    mRouteLocalDataRepository.save(route);
                }

                // 进入到主界面
                // 设置为非首次启动
                Log.d("SplashActivity", "设置设备为非首次启动");
                mFlagDataRepository.setNotFistStart();
                mView.showOnLineDevices();
            }
        }).start();
    }

    @Override
    public void start() {

    }
}
