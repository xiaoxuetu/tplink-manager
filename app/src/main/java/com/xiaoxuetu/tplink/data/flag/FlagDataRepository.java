package com.xiaoxuetu.tplink.data.flag;

import android.content.SharedPreferences;

import com.xiaoxuetu.tplink.TpLinkApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kevin on 2017/3/19.
 */

public class FlagDataRepository implements FlagDataSource {

    private static FlagDataRepository INSTANCE;

    private FlagDataRepository(){}

    public static FlagDataRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FlagDataRepository();
        }
        return INSTANCE;
    }

    @Override
    public boolean isFirstStart() {
        SharedPreferences sharedPreferences = TpLinkApplication.getInstance()
                .getSharedPreferences(SHARE_PREFERENCES_FILENAME, MODE_PRIVATE);
        boolean isFirstStart = sharedPreferences.getBoolean(IS_FIRST_START_KEY, true);
        return isFirstStart;
    }

    @Override
    public void setNotFistStart() {
        SharedPreferences sharedPreferences = TpLinkApplication.getInstance()
                .getSharedPreferences(SHARE_PREFERENCES_FILENAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_FIRST_START_KEY, false);
        editor.commit();
    }
}
