package com.xiaoxuetu.shield;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xiaoxuetu.shield.common.widget.titlebar.TitleBar;
import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.api.impl.TPLinkRouteApiImpl;

public class LoginActivity extends AppCompatActivity {

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

    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            IRouteApi routeApi = TPLinkRouteApiImpl.getInstance();
            routeApi.login("192.168.0.1", "xxxx");
            routeApi.getDevices();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.login_account_password_login_button)
                .setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Thread(loginRunnable).start();

                // 进入到主界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
