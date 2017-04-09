package com.xiaoxuetu.tplink.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.RouteApiFactory;
import com.xiaoxuetu.route.RouteModel;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.BaseActivity;
import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.TpLinkApplication;
import com.xiaoxuetu.tplink.data.device.DeviceLocalDataRepository;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;
import com.xiaoxuetu.tplink.utils.NetworkUtils;
import com.xiaoxuetu.tplink.utils.PkgUtils;

/**
 * TODO: MAC 地址格式的统一
 */
public class DeviceActivity extends BaseActivity {

    private DevicePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_activity);

        if (PkgUtils.isApkInRelease(this)) {
            PgyUpdateManager.register(this);
        }

        RouteApi routeApi = RouteApiFactory.createRoute(RouteModel.TPLink.WR842N);
        DeviceView view = (DeviceView) findViewById(R.id.device_view);
        RouteLocalDataRepository routeLocalDataRepository  = RouteLocalDataRepository.getInstance(this);
        DeviceLocalDataRepository deviceLocalDataRepository = DeviceLocalDataRepository.getInstance(this);
        mPresenter = new DevicePresenter(routeLocalDataRepository, deviceLocalDataRepository, routeApi, view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.judgeNetState(this);

        if (NetworkUtils.getNetype(getBaseContext()) != NetworkUtils.WIFI_NETWORK) {
            return;
        }
        mPresenter.start();
        mPresenter.loadRoute(this);
    }

    private Route mRoute;
    private Handler mShowRouteHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String wifiName = mRoute.wifiName;
            TextView routeNameTextView = (TextView) findViewById(R.id.router_name);
            routeNameTextView.setText(wifiName);
            return false;
        }
    });

    public void showRoute(Route route) {
        mRoute = route;
        Log.d(TAG, "路由器的名称是 " + route.wifiName);
        mShowRouteHandler.sendEmptyMessage(0);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (PkgUtils.isApkInRelease(this)) {
            PgyUpdateManager.unregister();
        }
    }

    public void dismissNetWorkTips() {
        findViewById(R.id.router_header_tips).setVisibility(View.GONE);
    }

    public void showOpenNetworkTips() {
        // 修改按钮文本
        TextView btnTextView = (TextView) findViewById(R.id.client_header_item_button);
        btnTextView.setText("打开");

        btnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.wifi.WifiPickerActivity");
                Intent localIntent = new Intent();
                localIntent.setComponent(componentName);
                startActivityForResult(localIntent, 1);
            }
        });

        btnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent();
                localIntent.setComponent(new ComponentName(
                        "com.android.settings",
                        "com.android.settings.wifi.WifiPickerActivity" ));
                startActivityForResult(localIntent, 1);
            }
        });

        // 打开网络提示
        TextView textView = (TextView) findViewById(R.id.client_header_item_text);
        textView.setText(getString(R.string.reminder_open_network));
        findViewById(R.id.router_header_tips).setVisibility(View.VISIBLE);
    }

    public void showSwitchWifiTips() {
        // 修改按钮文本
        TextView btnTextView = (TextView) findViewById(R.id.client_header_item_button);
        btnTextView.setText("切换");

        btnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComponentName componentName = new ComponentName(
                        "com.android.settings",
                        "com.android.settings.wifi.WifiPickerActivity");
                Intent localIntent = new Intent();
                localIntent.setComponent(componentName);
                startActivityForResult(localIntent, 1);
            }
        });

        // 打开WI-FI提示
        TextView textView = (TextView) findViewById(R.id.client_header_item_text);
        textView.setText(getString(R.string.reminder_switch_wifi));
        findViewById(R.id.router_header_tips).setVisibility(View.VISIBLE);
    }
}
