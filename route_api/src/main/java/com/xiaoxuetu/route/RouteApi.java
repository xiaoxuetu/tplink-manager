package com.xiaoxuetu.route;

import com.xiaoxuetu.route.model.CommonResult;

public interface RouteApi {

    /**
     * 通过IP、密码进行登录
     * @param ip 路由器IP地址
     * @param password 路由器登录密码
     * @return 是否登录成功
     */
    CommonResult login(String ip, String password);

    /**
     * 获取在线设备
     * @return 返回在线的设备列表
     */
    CommonResult getOnlineDevices();

    /**
     * 获取路由设备信息
     * @return 返回路由设别信息
     */
    CommonResult getRoute();
}
