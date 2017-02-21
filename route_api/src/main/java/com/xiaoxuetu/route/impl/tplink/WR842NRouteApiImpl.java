package com.xiaoxuetu.route.impl.tplink;

import com.xiaoxuetu.route.RouteApi;
import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.util.Formatter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xiaoxuetu.route.impl.tplink.WR842NEncryption.base64Encoding;
import static com.xiaoxuetu.route.impl.tplink.WR842NHtmlParser.parseHtmlForGetDevices;
import static com.xiaoxuetu.route.impl.tplink.WR842NHtmlParser.parseHtmlForOnlineDeviceMacAddress;
import static com.xiaoxuetu.route.impl.tplink.WR842NHtmlParser.parseHtmlForWlanStateSummary;

/**
 * Created by kevin on 2017/2/21.
 */

public class WR842NRouteApiImpl implements RouteApi {

    public static final String TPLINK_MODEL = "WR842N 4.0 00000000";

    public static final String VERSION = "1.3.11 Build 140528 Rel.34010n";

    private String mIP;
    private String mPassword;

    private String mCookie;

    @Override
    public CommonResult login(String ip, String password) {
        mIP = ip;
        mPassword = password;

        // cookie 计算
        String auth = "Basic " + base64Encoding("admin:" + password);

        mCookie = Formatter.htmlEncode(auth).replaceAll(" ", "%20");

        String url = "/userRpm/StatusRpm.htm";
        String html = WR842NRequestor.request(mIP, url, mCookie);

        boolean isLoginSuccess = WR842NHtmlParser.isLoginSuccess(html);

        CommonResult commonResult;
        if (isLoginSuccess) {
            commonResult = CommonResult.success("登录成功");
        } else {
            commonResult = CommonResult.failure("登录失败");
        }
        return commonResult;
    }

    @Override
    public CommonResult getOnlineDevices() {
        if (mCookie == null || mCookie.length() == 0) {
            return CommonResult.failure("尚未登录");
        }

        CommonResult commonResult = getOnlineDevicesMacAddress();

        if (commonResult.isFailure()) {
            return CommonResult.failure("解析失败");
        }

        List<String> macList = (List<String>) commonResult.getData();
        List<Device> onLineDeviceList = new ArrayList<>();

        if (macList.isEmpty()) {
            return CommonResult.success("无设备在线", onLineDeviceList);
        }

        String url = "/userRpm/AssignedIpAddrListRpm.htm";
        String html = WR842NRequestor.request(mIP, url, mCookie);
        commonResult = parseHtmlForGetDevices(html);

        if (commonResult.isFailure()) {
            return CommonResult.failure("解析失败");
        }

        List<Device> deviceList = (List<Device>) commonResult.getData();

        if (deviceList.isEmpty()) {
            return CommonResult.success("无设备在线", onLineDeviceList);
        }

        for (Device device : deviceList) {
            String macAddress = device.macAddress;

            if (macList.contains(macAddress)) {
                onLineDeviceList.add(device);
            }
        }
        return CommonResult.success("解析成功", onLineDeviceList);
    }

    @Override
    public CommonResult getRoute() {
        if (mCookie == null || mCookie.length() == 0) {
            return CommonResult.failure("登录失败");
        }

        String url = "/userRpm/StatusRpm.htm";
        String html = WR842NRequestor.request(mIP, url, mCookie);
        return WR842NHtmlParser.parseHtmlForRoute(html);
    }

    private CommonResult getOnlineDevicesMacAddress()  {
        if (mCookie == null || mCookie.length() == 0) {
            return CommonResult.failure("尚未登录");
        }

        // 获取整体情况
        String summaryHtml = getWlanStationHtml(1);
        CommonResult commonResult = parseHtmlForWlanStateSummary(summaryHtml);

        if (commonResult.isFailure()) {
            return CommonResult.failure("获取整体情况失败");
        }

        // 计算设备页数
        Map<String, Integer> summaryMap = (Map<String, Integer>) commonResult.getData();
        int deviceNumber = summaryMap.get("device_number");
        int perPageNumber = summaryMap.get("per_page_number");
        int totalPageNum = (deviceNumber  +  perPageNumber  - 1) / perPageNumber;

        // 获取在线设备mac地址
        List<String> macList = new ArrayList<>();

        for (int i=1; i<= totalPageNum; i++) {
            String resultHtml = getWlanStationHtml(i);
            CommonResult tempCommonResult = parseHtmlForOnlineDeviceMacAddress(resultHtml);

            if (tempCommonResult.isFailure()) {
                continue;
            }

            List<String> tempMacList = (List<String>) tempCommonResult.getData();
            macList.addAll(tempMacList);
        }
        return CommonResult.success("解析成功", macList);
    }

    private String getWlanStationHtml(int i) {
        String url =  "/userRpm/WlanStationRpm.htm?Page=" + i;
        return WR842NRequestor.request(mIP, url, mCookie);
    }
}
