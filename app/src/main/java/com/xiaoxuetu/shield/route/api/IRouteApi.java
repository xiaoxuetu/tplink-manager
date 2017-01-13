package com.xiaoxuetu.shield.route.api;

import com.xiaoxuetu.shield.route.model.CommandResult;
import com.xiaoxuetu.shield.route.model.Device;

import java.util.List;

/**
 * Created by kevin on 2017/1/10.
 */

public interface IRouteApi {

    /**
     * 通过IP、密码进行登录
     * @param ip 路由器IP地址
     * @param password 路由器登录密码
     * @return 是否登录成功
     */
    CommandResult login(String ip, String password);

    CommandResult getDevices();


}
