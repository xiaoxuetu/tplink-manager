package com.xiaoxuetu.shield;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

/**
 * Created by kevin on 2017/3/6.
 */

public class MyApplication extends Application {




    @Override
    public void onCreate() {
        super.onCreate();

        PgyCrashManager.register(this);

    }
}
