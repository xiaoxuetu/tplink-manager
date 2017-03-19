package com.xiaoxuetu.tplink.data.flag;

/**
 * Created by kevin on 2017/3/19.
 */

public interface FlagDataSource {

    /**
     * 配置文件的名称
     */
    String SHARE_PREFERENCES_FILENAME = "flag";

    /**
     * 是否首次启动的存储关键字
     */
    String IS_FIRST_START_KEY = "IS_FIRST_START";

    /**
     * 是否首次启动
     * @return
     */
    boolean isFirstStart();

    /**
     * 设置为非首次启动
     */
    void setNotFistStart();
}
