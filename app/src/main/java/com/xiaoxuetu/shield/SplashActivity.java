package com.xiaoxuetu.shield;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import com.xiaoxuetu.shield.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private View loginButton;

    private static final String FLAG_FISRT_START = "flag_first_start";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 必须在setContentView之前设置
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);



        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        // 在4.0以后必须使用这种方式进行标题栏的隐藏
//        ActionBar actionBar = getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.hide();
//        }

//        initButton();
    }


    @Override
    protected void onStart() {
        super.onStart();

        // 延迟两秒钟再进行跳转.sendEmptyMessageDelayed(0,1000)
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                SharedPreferences sharedPreferences = getSharedPreferences("flag", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                boolean isFirstStart = sharedPreferences.getBoolean(FLAG_FISRT_START, true);

                Intent intent;
                if (isFirstStart) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    editor.putBoolean(FLAG_FISRT_START, false);
                    editor.commit();
                } else {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                return false;
            }
        }).sendEmptyMessageDelayed(0, 500);
    }




//    private void initButton() {
//        loginButton = findViewById(R.id.button_add_new_router);
//
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
}
