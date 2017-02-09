package com.xiaoxuetu.shield;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.xiaoxuetu.shield.common.widget.dialog.MLTextView;
import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.api.impl.TPLinkRouteApiImpl;
import com.xiaoxuetu.shield.route.model.CommandResult;
import com.xiaoxuetu.shield.route.model.Device;
import com.xiaoxuetu.shield.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String DEVICES_KEY = "devices";

    private String currentDeviceMacAddress = "";

    private View emptyView;
    private ListView listView;

    private Runnable deviceRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            IRouteApi routeApi = TPLinkRouteApiImpl.getInstance();
            CommandResult commandResult = routeApi.getDevices();

            Bundle deviceBundle = new Bundle();
            deviceBundle.putParcelable(DEVICES_KEY, commandResult);
            Message deviceMessage = new Message();
            deviceMessage.setData(deviceBundle);
            MainActivity.this.deviceRefreshHandler.sendMessage(deviceMessage);
        }
    };

    private Handler deviceRefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            CommandResult commandResult = msg.getData().getParcelable(DEVICES_KEY);

            List<Device> deviceList = (List<Device>) commandResult.getData();

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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentDeviceMacAddress = DeviceUtils.getMacAddress(getApplicationContext())
                .toLowerCase();
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
}
