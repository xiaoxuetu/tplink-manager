package com.xiaoxuetu.shield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

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


    }

    private List<Map<String, Object>> getMockData() {
        List<Map<String, Object>> mockData = new ArrayList<>();

        Map<String, Object> data1 = new HashMap<>();
        data1.put("client_icon", R.drawable.client_device_list_unknown);
        data1.put("client_name", "XiaoMi");
        data1.put("client_event", "5G连接");
        data1.put("event_time", "2016-12-24 00:10:12");
        data1.put("client_net_speed", "1.9 KB/s");

        mockData.add(data1);
    }
}
