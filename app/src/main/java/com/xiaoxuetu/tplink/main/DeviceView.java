package com.xiaoxuetu.tplink.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.util.Formatter;
import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.common.widget.dialog.MLTextView;
import com.xiaoxuetu.tplink.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * Created by kevin on 2017/3/30.
 */

public class DeviceView extends FrameLayout implements DeviceContract.View {

    private final String TAG = getClass().getSimpleName();

    private String currentDeviceMacAddress = "";
    private DeviceContract.Presenter mPresenter;
    private PtrFrameLayout mDevicePtrFrameLayout;

    public DeviceView(Context context) {
        super(context);
        init();
    }

    public DeviceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.device_view, this);

        mDevicePtrFrameLayout = (PtrFrameLayout) findViewById(R.id.device_pull_refresh_view);
        final ListView listView = (ListView) findViewById(R.id.client_list_view);
        mDevicePtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "刷新路由器信息");
                        mPresenter.loadDevices();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, listView, header);
            }


        });

        final MaterialHeader header = new MaterialHeader(getContext());

        //可以设置一组 颜色数组，改变进度显示的颜色变化
        header.setColorSchemeColors(new int[]{Color.RED, Color.BLUE, Color.GRAY, Color.GREEN});
        //设置布局参数，-1指匹配父窗体，-2指包裹内容
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        //设置内边距...PtrLocalDisplay框架自带
        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
        //告诉创建一个MaterialHeader 布局绑定在那个下拉刷新控件上
        header.setPtrFrameLayout(mDevicePtrFrameLayout);

        //给下拉刷新设置下拉头部 MaterialHeader布局
        mDevicePtrFrameLayout.setHeaderView(header);
        //添加一个UI时间处理回调函数。为MaterialHeader的内部实现回调。
        mDevicePtrFrameLayout.addPtrUIHandler(header);


        currentDeviceMacAddress = DeviceUtils.getMacAddress(getContext());
    }

    private List<Device> mDeviceList;

    private Handler mShowDeviceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            List<Map<String, Object>> deviceData = covertToMapList(mDeviceList);

            ListView listView = (ListView) findViewById(R.id.client_list_view);
            ListAdapter adapter = new ClientListAdapter(getContext(), deviceData);
            listView.setAdapter(adapter);

            View emptyView = findViewById(R.id.empty_view);
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            mDevicePtrFrameLayout.refreshComplete();
            return false;
        }
    });

    @Override
    public void showDevices(final List<Device> deviceList) {
        mDeviceList = deviceList;
        Log.d(TAG, mDeviceList.toString());
        mShowDeviceHandler.sendEmptyMessage(0);
    }


    private Handler mShowNoDevicesHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            ListView listView = (ListView) findViewById(R.id.client_list_view);
            View emptyView = findViewById(R.id.empty_view);
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            return false;
        }
    });

    @Override
    public void showNoDevices() {
        mShowNoDevicesHandler.sendEmptyMessage(0);
    }

    @Override
    public void showFailureMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(DeviceContract.Presenter presenter) {
        mPresenter = presenter;
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
            String macAddress = Formatter.formatMacAddress(currentDataMap.get("client_mac_address").toString());
            return macAddress.equals(currentDeviceMacAddress);
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
