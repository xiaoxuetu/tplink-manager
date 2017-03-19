package com.xiaoxuetu.tplink.splash;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.xiaoxuetu.tplink.data.flag.FlagDataRepository;

/**
 * Created by kevin on 2017/3/19.
 */

public class SplashPresenter implements SplashContract.Presenter {

    private final String TAG = getClass().getSimpleName();
    private SplashContract.View mSplashView;
    private final FlagDataRepository mFlagDataRepository;

    public SplashPresenter(@NonNull FlagDataRepository mFlagDataRepository,
                           @NonNull SplashContract.View mSplashView) {
        this.mFlagDataRepository = mFlagDataRepository;
        this.mSplashView = mSplashView;

        mSplashView.setPresenter(this);
    }

    @Override
    public void isFirstStart() {
        // 延迟500ms再进行跳转
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                boolean isFirstStart = mFlagDataRepository.isFirstStart();
                Log.d(TAG, "isFirstStart的值是： " + isFirstStart);

                if (isFirstStart) {
                    Log.d(TAG, "这是首次启动");
                    mSplashView.showLoginUI();
                } else {
                    Log.d(TAG, "这是非首次启动，可以直接跳转到设备管理界面");
                    mSplashView.showOnLineDevices();
                }
                return false;
            }
        }).sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void start() {
        isFirstStart();
    }
}
