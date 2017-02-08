package com.xiaoxuetu.shield;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xiaoxuetu.shield.common.widget.titlebar.TitleBar;
import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.api.impl.TPLinkRouteApiImpl;
import com.xiaoxuetu.shield.route.model.CommandResult;

public class LoginActivity extends AppCompatActivity {

    private static final String IS_LOGIN_SUCCESS_KEY = "is_login_success";
    private static final String LOGIN_MSG_KEY = "login_msg";

    private EditText hostEditText;
    private EditText passwordEditText;

    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            Bundle isLoginSuccessBundle = new Bundle();

            String msg = "";
            boolean isLoginSuccess = false;


            String host = hostEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(host)) {
                msg = "IP地址不能为空";
            } else if (TextUtils.isEmpty(password)) {
                msg = "密码不能为空";
            } else {
                IRouteApi routeApi = TPLinkRouteApiImpl.getInstance();
                CommandResult commandResult = routeApi.login(host, password);

                if (commandResult.getCode() == CommandResult.CODE_FAILURE) {
                    msg = "密码错误";
                } else {
                    isLoginSuccess = true;
                }
            }



            isLoginSuccessBundle.putBoolean(IS_LOGIN_SUCCESS_KEY, isLoginSuccess);
            isLoginSuccessBundle.putString(LOGIN_MSG_KEY, msg);
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

            if (isLoginSuccess) {
                // 进入到主界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, msgStr, Toast.LENGTH_LONG).show();
            }
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
}
