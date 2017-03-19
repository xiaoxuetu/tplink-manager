package com.xiaoxuetu.tplink.splash;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xiaoxuetu.tplink.R;
import com.xiaoxuetu.tplink.login.LoginActivity;
import com.xiaoxuetu.tplink.main.MainActivity;

/**
 * Created by kevin on 2017/3/19.
 */

public class SplashView extends FrameLayout implements SplashContract.View {

    private SplashContract.Presenter mSplashPresenter;

    public SplashView(Context context) {
        super(context);
        init();
    }

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.splash_view, this);
    }

    @Override
    public void showOnLineDevices() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void showLoginUI() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void setPresenter(SplashContract.Presenter presenter) {
        mSplashPresenter = presenter;
    }
}
