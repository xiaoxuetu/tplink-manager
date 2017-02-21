package com.xiaoxuetu.route;

import com.xiaoxuetu.route.impl.tplink.WR842NRouteApiImpl;

/**
 * Created by kevin on 2017/2/21.
 */

public class RouteApiFactory {

    /**
     * 根据型号获取路由器API对象
     * @param model 型号
     * @return 返回型号对应的操作API对象
     */
    public static RouteApi createRoute(String model) {
        RouteApi routeApi = null;

        model = model.toLowerCase();

        switch (model) {
            case "wr842n":
                routeApi = new WR842NRouteApiImpl();
                break;

            default:
                break;
        }
        return routeApi;
    }
}
