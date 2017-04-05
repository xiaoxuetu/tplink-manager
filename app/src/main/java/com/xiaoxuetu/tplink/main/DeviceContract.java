package com.xiaoxuetu.tplink.main;

import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.tplink.BasePresenter;
import com.xiaoxuetu.tplink.BaseView;

import java.util.List;

/**
 * Created by kevin on 2017/3/29.
 */

public interface DeviceContract {

    interface Presenter extends BasePresenter {
        void loadDevices();

        void loadRoute(final DeviceActivity deviceActivity);

    }

    interface View extends BaseView<Presenter> {
        void showDevices(List<Device> deviceList);

        void showNoDevices();

        void showFailureMessage(String msg);
    }


}
