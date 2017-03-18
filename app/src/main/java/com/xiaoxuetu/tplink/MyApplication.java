package com.xiaoxuetu.tplink;

import android.app.Application;
import android.util.Log;

import com.pgyersdk.crash.PgyCrashManager;
import com.xiaoxuetu.tplink.utils.PkgUtils;

/**
 * Created by kevin on 2017/3/6.
 */

public class MyApplication extends Application {


    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        if (PkgUtils.isApkInRelease(this)) {
            Log.d(TAG, "这是发布包");
            PgyCrashManager.register(this);
        } else {
            Log.d(TAG, "这是开发包");
        }


    }
}
