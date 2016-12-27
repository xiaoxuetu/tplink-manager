package com.xiaoxuetu.shield;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaoxuetu.shield.common.widget.dialog.MLTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initListView();
    }



    private void initListView() {
        listView = (ListView) findViewById(R.id.client_list_view);
        List<Map<String, Object>> list = getMockData();
        ListAdapter adapter = new ClientListAdapter(this, list);
        listView.setAdapter(adapter);


    }
    private List<Map<String, Object>> getMockData() {

        List<Map<String, Object>> mockData = new ArrayList<>();

        for (int i=0; i<5; i++) {
            Map<String, Object> data1 = new HashMap<>();
            if (i%2 == 0) {
                data1.put("client_icon", R.drawable.client_device_list_camera);
            } else {
                data1.put("client_icon", R.drawable.client_device_list_unknown);
            }
            data1.put("client_name", "XiaoMi-" + i);
            data1.put("client_event", "5G连接");
            data1.put("event_time", "2016-12-24 00:10:1" + i);
            data1.put("client_net_speed", "1.9 KB/s");

            mockData.add(data1);
        }

        return mockData;
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Map<String, Object> currentDataMap = dataList.get(position);
            convertView = inflater.inflate(R.layout.client_list_device_item, parent, false);
            // 设置客户端图片
            ((ImageView) convertView.findViewById(R.id.client_icon))
                    .setImageResource((int)(currentDataMap.get("client_icon")));

            ((MLTextView) convertView.findViewById(R.id.client_name))
                    .setText(currentDataMap.get("client_name").toString());

            ((MLTextView) convertView.findViewById(R.id.client_event))
                    .setText(currentDataMap.get("client_event").toString());

            ((MLTextView) convertView.findViewById(R.id.event_time))
                    .setText(currentDataMap.get("event_time").toString());

            ((MLTextView) convertView.findViewById(R.id.client_net_speed))
                    .setText(currentDataMap.get("client_net_speed").toString());



            return convertView;
        }
    }
}
