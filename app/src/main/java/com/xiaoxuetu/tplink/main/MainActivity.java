package com.xiaoxuetu.tplink.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;
import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.RouteApiFactory;
import com.xiaoxuetu.route.RouteModel;
import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.common.widget.dialog.MLTextView;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;
import com.xiaoxuetu.tplink.login.LoginActivity;
import com.xiaoxuetu.tplink.utils.DeviceUtils;
import com.xiaoxuetu.tplink.utils.PkgUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: MAC 地址格式的统一
 */
public class MainActivity extends AppCompatActivity {

    private final String PGYSDK_UPDATE_PROVIDER = "com.xiaoxuetu.shield.UpdateCenterProvider";

    private static final String LOGIN_RESULT_KEY = "login_result";
    private static final String DEVICES_KEY = "devices";
    private static final String ROUTE_KEY = "route";

    private String currentDeviceMacAddress = "";

    private View emptyView;
    private ListView listView;

    private Runnable deviceRefreshRunnable = new Runnable() {
        @Override
        public void run() {

            Route route = RouteLocalDataRepository.getInstance(getApplicationContext()).findOnFocusRoute();

            RouteApi routeApi = RouteApiFactory.createRoute(RouteModel.TPLink.WR842N);
            CommonResult loginResult = routeApi.login(route.ip, route.password);
            Bundle deviceBundle = new Bundle();

            if (loginResult.isSuccess()) {
                deviceBundle.putBoolean(LOGIN_RESULT_KEY, true);
                CommonResult routeCommonResult = routeApi.getRoute();
                CommonResult devicesCommonResult = routeApi.getOnlineDevices();
                deviceBundle.putSerializable(ROUTE_KEY, routeCommonResult);
                deviceBundle.putSerializable(DEVICES_KEY, devicesCommonResult);
            } else {
                deviceBundle.putBoolean(LOGIN_RESULT_KEY, false);
            }

            Message deviceMessage = new Message();
            deviceMessage.setData(deviceBundle);
            MainActivity.this.deviceRefreshHandler.sendMessage(deviceMessage);
        }
    };

    private Handler deviceRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            boolean isLoginSuccess = msg.getData().getBoolean(LOGIN_RESULT_KEY);

            if (!isLoginSuccess) {
                Toast.makeText(MainActivity.this, "登录失败，请重新登录", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }


            CommonResult devicesCommonResult = (CommonResult) msg.getData().getSerializable(DEVICES_KEY);

            if (devicesCommonResult.isFailure()) {
                Toast.makeText(MainActivity.this, "获取设备列表失败，请手动刷新", Toast.LENGTH_LONG).show();
            } else {
                List<Device> deviceList = (List<Device>) devicesCommonResult.getData();
                if (deviceList.isEmpty()) {
                    return;
                }

                List<Map<String, Object>> deviceData = covertToMapList(deviceList);

                listView = (ListView) findViewById(R.id.client_list_view);
                ListAdapter adapter = new ClientListAdapter(MainActivity.this, deviceData);
                listView.setAdapter(adapter);

                emptyView = findViewById(R.id.empty_view);
                emptyView.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);

            }

            CommonResult routeCommonResult = (CommonResult) msg.getData().getSerializable(ROUTE_KEY);

            if (routeCommonResult.isFailure()) {
                Toast.makeText(MainActivity.this, "获取路由信息失败，请手动刷新", Toast.LENGTH_LONG).show();
            } else {
                Route route = (Route) routeCommonResult.getData();
                String wifiName = route.wifiName;

                TextView routeNameTextView = (TextView) findViewById(R.id.router_name);
                routeNameTextView.setText(wifiName);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PkgUtils.isApkInRelease(this)) {
            PgyUpdateManager.register(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PgyUpdateManager.unregister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentDeviceMacAddress = DeviceUtils.getMacAddress(getApplicationContext());
        new Thread(deviceRefreshRunnable).start();
    }


    private List<Map<String, Object>> covertToMapList(List<Device> deviceList) {

        List<Map<String, Object>> deviceMapList = new ArrayList<>();

        for (Device device : deviceList) {
            Map<String, Object> deviceMap = new HashMap<>();
            deviceMap.put("client_icon", R.drawable.client_device_list_unknown);

            deviceMap.put("client_name", device.deviceName);
            deviceMap.put("client_mac_address", device.macAddress);
            deviceMap.put("client_event", "2.4G连接");
//            deviceMap.put("event_time", "2016-12-24 00:10:1" + i);
//            deviceMap.put("client_net_speed", "1.9 KB/s");

            deviceMapList.add(deviceMap);
        }

        return deviceMapList;
    }


    public class ClientListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private  List<Map<String, Object>> dataList;

        public ClientListAdapter(Context context, List<Map<String, Object>> dataList) {
            this.inflater = LayoutInflater.from(context);
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        private boolean isMacAddressTheSame(Map<String, Object> currentDataMap) {
            String macAddress = currentDataMap.get("client_mac_address")
                    .toString()
                    .toLowerCase()
                    .replaceAll("-", ":");

            return macAddress.equals(MainActivity.this.currentDeviceMacAddress);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, Object> currentDataMap = dataList.get(position);
            convertView = inflater.inflate(R.layout.client_list_device_item, parent, false);
            // 设置客户端图片
            ((ImageView) convertView.findViewById(R.id.client_icon))
                    .setImageResource((int)(currentDataMap.get("client_icon")));

            if (!isMacAddressTheSame(currentDataMap)) {
                convertView.findViewById(R.id.admin_icon)
                        .setVisibility(View.INVISIBLE);
            }

            ((MLTextView) convertView.findViewById(R.id.client_name))
                    .setText(currentDataMap.get("client_name").toString());

            ((MLTextView) convertView.findViewById(R.id.client_event))
                    .setText(currentDataMap.get("client_event").toString());

//            ((MLTextView) convertView.findViewById(R.id.event_time))
//                    .setText(currentDataMap.get("event_time").toString());
//
//            ((MLTextView) convertView.findViewById(R.id.client_net_speed))
//                    .setText(currentDataMap.get("client_net_speed").toString());
            return convertView;
        }
    }


    private boolean isExit = false;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {// 当keyCode等于退出事件值时
            quit();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void quit() {
        if (isExit) {
            // ACTION_MAIN with category CATEGORY_HOME 启动主屏幕
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);// 使虚拟机停止运行并退出程序
        } else {
            isExit = true;
            Toast.makeText(MainActivity.this, "再按一次退出哦", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0, 3000);// 3秒后发送消息
        }
    }

    //创建Handler对象，用来处理消息
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {//处理消息
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            isExit = false;
        }
    };
}
