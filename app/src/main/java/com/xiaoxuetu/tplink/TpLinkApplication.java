package com.xiaoxuetu.tplink;

import android.app.Application;
import android.util.Log;

import com.pgyersdk.crash.PgyCrashManager;
import com.xiaoxuetu.tplink.utils.PkgUtils;

/**
 * Created by kevin on 2017/3/6.
 */

public class TpLinkApplication extends Application {

    private static TpLinkApplication INSTANCE;


    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        if (PkgUtils.isApkInRelease(this)) {
            Log.d(TAG, "这是发布包");
            PgyCrashManager.register(this);
        } else {
            Log.d(TAG, "这是开发包");
        }
    }

    public static TpLinkApplication getInstance() {
        return INSTANCE;
    }
}
