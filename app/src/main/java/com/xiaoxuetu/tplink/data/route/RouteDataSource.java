package com.xiaoxuetu.tplink.data.route;

import com.xiaoxuetu.route.model.Route;

import java.util.List;

/**
 * Created by kevin on 2017/3/20.
 */

public interface RouteDataSource {

    List<Route> findAll();

    Route findByMacAddress(String macAddress);

    long save(Route route);

    long update(Route route);

    void updateAllRouteToUnFocus();

    Route findOnFocusRoute();

    boolean isExists(String macAddress);


}
