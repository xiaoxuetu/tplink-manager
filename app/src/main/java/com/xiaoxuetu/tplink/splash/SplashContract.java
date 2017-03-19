package com.xiaoxuetu.tplink.splash;

import com.xiaoxuetu.tplink.BasePresenter;
import com.xiaoxuetu.tplink.BaseView;

/**
 * Created by kevin on 2017/3/19.
 */
public interface SplashContract {

    interface Presenter extends BasePresenter {

        /**
         * 判断应用是否首次启动
         */
        void isFirstStart();

    }

    interface View extends BaseView<Presenter> {

        /**
         * 跳转到在线路由器显示界面
         */
        void showOnLineDevices();

        /**
         * 跳转到登录界面
         */
        void showLoginUI();

    }
}
