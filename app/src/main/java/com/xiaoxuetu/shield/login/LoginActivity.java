package com.xiaoxuetu.shield.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoxuetu.shield.MainActivity;
import com.xiaoxuetu.shield.R;
import com.xiaoxuetu.shield.login.dao.RouteDao;
import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.api.impl.TPLinkRouteApiImpl;
import com.xiaoxuetu.shield.route.model.CommonResult;
import com.xiaoxuetu.shield.route.model.Route;

public class LoginActivity extends AppCompatActivity {

    private static final String IS_LOGIN_SUCCESS_KEY = "is_login_success";
    private static final String LOGIN_MSG_KEY = "login_msg";
    private static final String ROUTE_INFO_KEY = "route_info";

    private EditText hostEditText;
    private EditText passwordEditText;

    private String host;
    private String password;

    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            Bundle isLoginSuccessBundle = new Bundle();

            String msg = "";
            boolean isLoginSuccess = false;


            host = hostEditText.getText().toString();
            password = passwordEditText.getText().toString();
            CommonResult commonResult = null;

            if (TextUtils.isEmpty(host)) {
                msg = "IP地址不能为空";
            } else if (TextUtils.isEmpty(password)) {
                msg = "密码不能为空";
            } else {
                IRouteApi routeApi = TPLinkRouteApiImpl.getInstance();
                commonResult = routeApi.login(host, password);

                if (commonResult.getCode() == CommonResult.CODE_FAILURE) {
                    msg = "密码错误";
                } else {
                    isLoginSuccess = true;
                }
            }

            isLoginSuccessBundle.putBoolean(IS_LOGIN_SUCCESS_KEY, isLoginSuccess);
            isLoginSuccessBundle.putString(LOGIN_MSG_KEY, msg);
            isLoginSuccessBundle.putParcelable(ROUTE_INFO_KEY, commonResult);
            message.setData(isLoginSuccessBundle);
            LoginActivity.this.loginResultHandler.sendMessage(message);
        }
    };

    private Handler loginResultHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            boolean isLoginSuccess = msg.getData().getBoolean(IS_LOGIN_SUCCESS_KEY);
            String msgStr = msg.getData().getString(LOGIN_MSG_KEY);

            if (!isLoginSuccess) {
                Toast.makeText(LoginActivity.this, msgStr, Toast.LENGTH_LONG).show();
                return;
            }

            CommonResult commonResult = msg.getData().getParcelable(ROUTE_INFO_KEY);
            boolean isSaveSuccess = saveRouteInfo(commonResult);

            if (!isSaveSuccess) {
                Toast.makeText(LoginActivity.this, "路由器信息获取失败，请重新登录", Toast.LENGTH_LONG).show();
                return;
            }
            // 进入到主界面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        private boolean saveRouteInfo(CommonResult commonResult) {

            if (commonResult == null ||
                    commonResult.getCode() == CommonResult.CODE_FAILURE) {
                return false;
            }

            Route route = (Route) commonResult.getData();

            route.password = password;
            route.isOnFocus = true;

            RouteDao routeDao = new RouteDao(getApplicationContext());
            routeDao.updateAllRouteToUnFocus();
            if (routeDao.isRouteExists(route.macAddress)) {
                Route routeTemp = routeDao.findByMacAddress(route.macAddress);
                route.id = routeTemp.id;
                routeDao.update(route);
            } else {
                routeDao.save(route);
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 在4.0以后必须使用这种方式进行标题栏的隐藏
//        ActionBar actionBar = getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.hide();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        hostEditText = (EditText) findViewById(R.id.login_account_host_editor);
        passwordEditText = (EditText) findViewById(R.id.login_account_password_password_editor);

        findViewById(R.id.login_account_password_login_button)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Thread(loginRunnable).start();
            }
        });
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
