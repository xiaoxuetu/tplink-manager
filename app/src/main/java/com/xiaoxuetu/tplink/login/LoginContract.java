package com.xiaoxuetu.tplink.login;

import com.xiaoxuetu.tplink.BasePresenter;
import com.xiaoxuetu.tplink.BaseView;

/**
 * Created by kevin on 2017/3/28.
 */

public interface LoginContract {

    interface Presenter extends BasePresenter {

        /**
         * 登录
         * @param ip 路由器的Ip地址
         * @param password 路由器的密码
         */
        void login(String ip, String password);
    }

    interface View extends BaseView<Presenter> {

        /**
         * 显示失败的原因
         * @param msg 失败信息
         */
        void showFailureMessage(String msg);

        /**
         * 跳转到在线路由器显示界面
         */
        void showOnLineDevices();
    }
}
