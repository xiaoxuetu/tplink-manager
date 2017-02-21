package com.xiaoxuetu.route.impl.tplink;

import com.xiaoxuetu.route.model.CommonResult;
import com.xiaoxuetu.route.model.Device;
import com.xiaoxuetu.route.model.Route;
import com.xiaoxuetu.route.util.Formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 2017/2/21.
 */

public class WR842NHtmlParser {

    /**
     * 解析URL获取路由器信息
     * @param resultHtml 路由器信息页面的HTML
     * @return 路由器信息
     */
    public static CommonResult parseHtmlForRoute(String resultHtml) {

        // 登录失败
        if (!isLoginSuccess(resultHtml)) {
            return CommonResult.failure("登录失败");
        }

        String pattern = "var wlanPara=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        // 匹配不到想要的信息
        if (!matcher.find()) {
            return CommonResult.failure("解析失败");
        }

        String result = matcher.group();
        result = result.replace("var wlanPara=new Array(", "");
        String[] resultArray = result.split(",");

        // 匹配到的信息不对
        if (resultArray.length != 13) {
            return CommonResult.failure("列表获取失败");
        }

        // 匹配成功
        String wifiName = resultArray[1].replaceAll("\n", "")
                .replaceAll("\"", "");
        String macAddress = resultArray[4].replaceAll("\n", "")
                .replaceAll("\"", "")
                .replaceAll("-", ":")
                .toLowerCase();
        String ip = resultArray[5].replaceAll("\n", "")
                .replaceAll("\"", "");

        Route route = new Route("", wifiName, macAddress, false, ip, "");
        return CommonResult.success("解析成功", route);
    }

    /**
     * 解析获取在线的设备数量和路由器上每页显示的设备数量
     * @param resultHtml wifi概况页面的HTML
     * @return 在线的设备数量和路由器上每页显示的设备数量
     */
    public static CommonResult parseHtmlForWlanStateSummary(String resultHtml) {

        // 登录失败
        if (!isLoginSuccess(resultHtml)) {
            return CommonResult.failure("登录失败");
        }

        String pattern = "var wlanHostPara=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        // 匹配不到想要的信息
        if (!matcher.find()) {
            return CommonResult.failure("解析失败");
        }

        String result = matcher.group();
        result = result.replace("var wlanHostPara=new Array(", "");
        String[] resultArray = result.split(",");

        // 匹配到的信息不对
        if (resultArray.length < 3) {
            return CommonResult.failure("信息获取失败");
        }


        // 匹配成功
        int deviceNumber = Integer.parseInt(resultArray[0].replaceAll("\n", "").replaceAll("\"", ""));
        int perPageNumber = Integer.parseInt(resultArray[2].replaceAll("\n", "").replaceAll("\"", ""));

        Map<String, Integer> summaryMap = new HashMap<>();
        summaryMap.put("device_number", deviceNumber);
        summaryMap.put("per_page_number", perPageNumber);
        return CommonResult.success("解析成功", summaryMap);
    }

    /**
     * 获取在线的设备MAC地址
     * @param resultHtml 在线设备Mac地址显示页面
     * @return 在心啊设备的mac地址
     */
    public static CommonResult parseHtmlForOnlineDeviceMacAddress(String resultHtml) {

        // 登录失败
        if (!isLoginSuccess(resultHtml)) {
            return CommonResult.failure("登录失败");
        }

        String pattern = "var hostList=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        // 匹配不到想要的信息
        if (!matcher.find()) {
            return CommonResult.failure("解析失败");
        }


        List<String> macList = new ArrayList<>();

        String result = matcher.group();
        result = result.replace("var hostList=new Array(", "");
        String[] resultArray = result.split(",");


        if ((resultArray.length - 2) < 1) {
            return CommonResult.success("解析成功，但无设备在线", macList);
        }

        if ((resultArray.length - 2) % 7 != 0) {
            return CommonResult.failure("解析失败");
        }

        int i = 0;
        while (i < (resultArray.length - 2)) {
            String macAddress = resultArray[i].replaceAll("\n", "").replaceAll("\"", "");
            macAddress = Formatter.formatMacAddress(macAddress);
            i = i + 7;
            macList.add(macAddress);
        }

        return CommonResult.success("解析成功", macList);
    }


    /**
     * 获取已经分配了IP地址的设备
     * @param resultHtml 设备IP地址分配页面HTML
     * @return 已经分配了IP地址的设备列表
     */
    public static CommonResult parseHtmlForGetDevices(String resultHtml) {

        // 登录失败
        if (!isLoginSuccess(resultHtml)) {
            return CommonResult.failure("登录失败");
        }

        String pattern = "var DHCPDynList=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        // 匹配不到想要的信息
        if (!matcher.find()) {
            return CommonResult.failure("解析失败");
        }

        String result = matcher.group();
        result = result.replace("var DHCPDynList=new Array(", "");
        String[] resultArray = result.split(",");


        if ((resultArray.length - 2) < 1) {
            return CommonResult.success("解析成功，但是无设备在线");
        }

        if ((resultArray.length - 2) % 4 != 0) {
            return CommonResult.failure("解析错误，没有获取到设备列表");
        }

        int i = 0;
        List<Device> deviceList = new ArrayList<>();

        while (i < (resultArray.length - 2)) {
            String deviceName = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
            String macAddress = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
            macAddress = Formatter.formatMacAddress(macAddress);
            String ipAddress  = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
            String validTime  = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
            Device device = new Device(deviceName, macAddress, ipAddress, validTime);
            deviceList.add(device);

        }
        return  CommonResult.success("解析成功", deviceList);
    }

    /**
     * 根据页面判断是否登录成功
     * @param resultHtml 页面HTML
     * @return 是否登录成功
     */
    public static boolean isLoginSuccess(String resultHtml) {
        if (resultHtml == null
                || resultHtml.contains("You have no authority to access this device!")
                || resultHtml.contains("loginBtn")) {
            return false;
        }
        return true;
    }
}
