package com.xiaoxuetu.tplink;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kevin on 2017/4/9.
 */

public abstract class BaseDBHelper extends SQLiteOpenHelper {

    protected final String TAG = getClass().getSimpleName();

    protected static final String TEXT_TYPE = " TEXT";

    protected static final String INTEGER_TYPE = " INTEGER";

    protected static final String BOOLEAN_TYPE = " INTEGER";

    protected static final String COMMA_SEP = ",";

    public BaseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
