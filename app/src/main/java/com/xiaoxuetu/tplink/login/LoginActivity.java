package com.xiaoxuetu.tplink.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.RouteApiFactory;
import com.xiaoxuetu.route.RouteModel;
import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.data.flag.FlagDataRepository;
import com.xiaoxuetu.tplink.data.route.RouteLocalDataRepository;

public class LoginActivity extends AppCompatActivity {

    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // 在4.0以后必须使用这种方式进行标题栏的隐藏
//        ActionBar actionBar = getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.hide();
//        }

        RouteLocalDataRepository routeLocalDataRepository = RouteLocalDataRepository.getInstance(getApplicationContext());
        RouteApi routeApi = RouteApiFactory.createRoute(RouteModel.TPLink.WR842N);
        FlagDataRepository flagDataRepository = FlagDataRepository.getInstance();

        LoginView view = (LoginView) findViewById(R.id.login_view);
        mPresenter = new LoginPresenter(routeLocalDataRepository, flagDataRepository, routeApi, view);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            Toast.makeText(LoginActivity.this, "再按一次退出哦", Toast.LENGTH_SHORT).show();
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
