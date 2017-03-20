package com.xiaoxuetu.tplink.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.data.flag.FlagDataRepository;

public class SplashActivity extends AppCompatActivity {

    private SplashContract.Presenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 必须在setContentView之前设置
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // 设置为全屏显示
        this.getWindow()
                .setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        // 初始化View
        SplashContract.View view = new SplashView(this);

        // 初始化Presenter
        FlagDataRepository repository = FlagDataRepository.getInstance();
        mSplashPresenter = new SplashPresenter(repository, view);

        // 在4.0以后必须使用这种方式进行标题栏的隐藏
//        ActionBar actionBar = getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.hide();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSplashPresenter.start();
    }
}
