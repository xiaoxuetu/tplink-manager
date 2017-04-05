package com.xiaoxuetu.tplink.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.pgyersdk.update.PgyUpdateManager;
import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.RouteApiFactory;
import com.xiaoxuetu.route.RouteModel;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;
import com.xiaoxuetu.tplink.utils.PkgUtils;

/**
 * TODO: MAC 地址格式的统一
 */
public class DeviceActivity extends AppCompatActivity {

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
        mPresenter = new DevicePresenter(routeLocalDataRepository, routeApi, view);
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
        mShowRouteHandler.sendEmptyMessage(0);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (PkgUtils.isApkInRelease(this)) {
            PgyUpdateManager.unregister();
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
            Toast.makeText(DeviceActivity.this, "再按一次退出哦", Toast.LENGTH_SHORT).show();
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
