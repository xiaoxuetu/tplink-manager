package com.xiaoxuetu.shield.route.api.impl;

import android.text.TextUtils;
import android.util.Log;

import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.model.CommonResult;
import com.xiaoxuetu.shield.route.model.Device;
import com.xiaoxuetu.shield.route.model.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by kevin on 2017/1/10.
 *
 * TODO: 代码优化
 */

public class TPLinkRouteApiImpl implements IRouteApi {

    private final String TAG = this.getClass().getSimpleName();

    private static TPLinkRouteApiImpl tpLinkRouteApi;

    private TPLinkRouteApiImpl(){}

    private String ip;
    private String password;

    private String cookie;

    private OkHttpClient okHttpClient = new OkHttpClient();

    public synchronized static TPLinkRouteApiImpl getInstance() {
        if (tpLinkRouteApi == null) {
            tpLinkRouteApi = new TPLinkRouteApiImpl();
        }
        return tpLinkRouteApi;
    }

    @Override
    public CommonResult login(String ip, String password) {
        this.ip = ip;
        this.password = password;

        String auth = "Basic " + base64Encoding("admin:" + password);
        this.cookie = TextUtils.htmlEncode(auth).replaceAll(" ", "%20");
        Log.d(TAG, "cookie is " + this.cookie);

        CommonResult commonResult = getRouteInfo();

        if (commonResult == null) {
            return CommonResult.failure();
        }
        return commonResult;
    }

    @Override
    public CommonResult getRouteInfo() {
        CommonResult commonResult = null;

        if (TextUtils.isEmpty(cookie)) {
            return null;
        }

        String url = "http://" + this.ip + "/userRpm/StatusRpm.htm";
        String cookieTemp = "Authorization=" + cookie + "; ChgPwdSubTag=";
        Request request = new Request.Builder()
                .addHeader("Cookie", cookieTemp)
                .addHeader("Referer", "http://192.168.0.1/userRpm/MenuRpm.htm")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36")
                .url(url)
                .build();

        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {

                String resultHtml = response.body().string();

                if (isLoginSuccess(resultHtml)) {
                    commonResult = parseHtmlForRouteInfo(resultHtml);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return commonResult;
    }

    @Override
    public CommonResult getDevices() {
        CommonResult commonResult = null;

        if (TextUtils.isEmpty(cookie)) {
            return commonResult;
        }

        String url = "http://" + this.ip + "/userRpm/AssignedIpAddrListRpm.htm";
        String cookieTemp = "Authorization=" + cookie + "; ChgPwdSubTag=";

        Request request = new Request.Builder()
                .addHeader("Cookie", cookieTemp)
                .addHeader("Referer", "http://" + ip +"/userRpm/MenuRpm.htm")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36")
                .url(url)
                .build();

        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {

                String resultHtml = response.body().string();

                if (isLoginSuccess(resultHtml)) {
                    commonResult = parseHtmlForGetDevices(resultHtml);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonResult;
    }



    private boolean isLoginSuccess(String resultHtml) {
        if (resultHtml.contains("You have no authority to access this device!")
                || resultHtml.contains("loginBtn")) {
            return false;
        }
        return true;
    }

    private CommonResult parseHtmlForRouteInfo(String resultHtml) {
        CommonResult commonResult = null;

        String pattern = "var wlanPara=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        if (matcher.find()) {
            String result = matcher.group();
            result = result.replace("var wlanPara=new Array(", "");
            String[] resultArray = result.split(",");
            Log.d(TAG, Arrays.toString(resultArray));

            if (resultArray.length != 13) {
                commonResult = CommonResult.failure("列表获取失败");
            } else {
                String wifiName = resultArray[1].replaceAll("\n", "")
                        .replaceAll("\"", "");
                String macAddress = resultArray[4].replaceAll("\n", "")
                        .replaceAll("\"", "")
                        .replaceAll("-", ":")
                        .toLowerCase();
                String ip = resultArray[5].replaceAll("\n", "")
                        .replaceAll("\"", "");

                Route route = new Route("", wifiName, macAddress, false, ip, "");
                commonResult = CommonResult.success("解析成功", route);
            }
        }
        return commonResult;
    }

    private List<String> getOnlineDevicesMacAddress() {
        List<String> macList = new ArrayList<>();

        if (TextUtils.isEmpty(cookie)) {
            return macList;
        }

        String summaryHtml = requestWlanStation(1);
        Map<String, Integer> summaryMap = parseHtmlForWlanStateSummary(summaryHtml);
        int deviceNumber = summaryMap.get("device_number");
        int perPageNumber = summaryMap.get("per_page_number");
        int totalPageNum = (deviceNumber  +  perPageNumber  - 1) / perPageNumber;

        for (int i=1; i<= totalPageNum; i++) {
            String resultHtml = requestWlanStation(i);
            macList.addAll(parseHtmlForOnlineDeviceMacAddress(resultHtml));
        }
        return macList;
    }

    private String requestWlanStation(int i) {
        String url = "http://" + this.ip + "/userRpm/WlanStationRpm.htm?Page=" + i;
        String cookieTemp = "Authorization=" + cookie + "; ChgPwdSubTag=";

        Request request = new Request.Builder()
                .addHeader("Cookie", cookieTemp)
                .addHeader("Referer", "http://" + ip +"/userRpm/MenuRpm.htm")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.95 Safari/537.36")
                .url(url)
                .build();
        String resultHtml = "";
        try {
            okhttp3.Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                resultHtml = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultHtml;
    }

    private Map<String, Integer> parseHtmlForWlanStateSummary(String resultHtml) {
        String pattern = "var wlanHostPara=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        if (matcher.find()) {
            String result = matcher.group();
            result = result.replace("var wlanHostPara=new Array(", "");
            String[] resultArray = result.split(",");
            Log.d(TAG, Arrays.toString(resultArray));

            if (resultArray.length < 3) {
                return null;
            } else {
                int deviceNumber = Integer.parseInt(resultArray[0].replaceAll("\n", "").replaceAll("\"", ""));
                int perPageNumber = Integer.parseInt(resultArray[2].replaceAll("\n", "").replaceAll("\"", ""));

                Map<String, Integer> summaryMap = new HashMap<>();
                summaryMap.put("device_number", deviceNumber);
                summaryMap.put("per_page_number", perPageNumber);
                return summaryMap;
            }
        }
        return null;
    }

    private List<String> parseHtmlForOnlineDeviceMacAddress(String resultHtml) {
        String pattern = "var hostList=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        List<String> macList = new ArrayList<>();

        if (matcher.find()) {

            String result = matcher.group();
            result = result.replace("var hostList=new Array(", "");
            String[] resultArray = result.split(",");
            Log.d(TAG, Arrays.toString(resultArray));


            if ((resultArray.length - 2) < 1) {

            } else if ((resultArray.length - 2) % 7 == 0) {
                int i = 0;
                while (i < (resultArray.length - 2)) {
                    String macAddress = resultArray[i].replaceAll("\n", "").replaceAll("\"", "");
                    i = i + 7;
                    macList.add(macAddress);
                }
            }
        }

        return macList;
    }


    private CommonResult parseHtmlForGetDevices(String resultHtml) {
        List<String> macList = getOnlineDevicesMacAddress();
        CommonResult commonResult;
        String pattern = "var DHCPDynList=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        if (matcher.find()) {

            String result = matcher.group();
            result = result.replace("var DHCPDynList=new Array(", "");
            String[] resultArray = result.split(",");
            Log.d(TAG, Arrays.toString(resultArray));


            if ((resultArray.length - 2) < 1) {
                commonResult = CommonResult.success();
            } else if ((resultArray.length - 2) % 4 == 0) {
                int i = 0;
                List<Device> deviceList = new ArrayList<>();
                while (i < (resultArray.length - 2)) {
                    String deviceName = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
                    String macAddress = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
                    String ipAddress  = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");
                    String validTime  = resultArray[i++].replaceAll("\n", "").replaceAll("\"", "");

                    Device device = new Device(deviceName, macAddress, ipAddress, validTime);

                    if (macList.contains(macAddress)) {
                        deviceList.add(device);
                    }
                }
                Log.d(TAG, deviceList.toString());
                commonResult = CommonResult.success("解析成功", deviceList);
            } else {
                commonResult = CommonResult.failure("解析错误，没有获取到设备列表");
            }
        } else {
            commonResult = CommonResult.failure("解析失败，没有获取到设备列表");
        }

        return commonResult;

    }

    private String utf8Encode(String password) {
        password = password.replaceAll("/\r\n/g", "\n");

        String utfText = "";

        for (int i = 0; i < password.length(); i++) {
            int charCode = password.codePointAt(i);

            if (charCode < 128) {
                utfText += (char) charCode;
            } else if ((charCode > 127) && (charCode < 2048)) {
                utfText += (char)((charCode >> 6) | 192);
                utfText += (char)((charCode & 63) | 128);
            } else {
                utfText += (char)((charCode >> 12) | 224);
                utfText += (char)(((charCode >> 6) & 63) | 128);
                utfText += (char)((charCode & 63) | 128);
            }
        }

        return utfText;
    }

    /**
     * 按照TP-Link路由器的算法将字符串按照base64算法进行转换
     * @param password 登录密码
     * @return 按照base64算法转换后的字符串
     */
    private String base64Encoding(String password) {
        String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        String output = "";
        password = utf8Encode(password);
        int i = 0;

        while (i < password.length()) {
            Integer charCode1 = null;
            Integer charCode2 = null;
            Integer charCode3 = null;
            try {
                charCode1 = password.codePointAt(i++);
            } catch (IndexOutOfBoundsException exception){
            }

            try {
                charCode2 = password.codePointAt(i++);
            } catch (IndexOutOfBoundsException exception){
            }

            try {
                charCode3 = password.codePointAt(i++);
            } catch (IndexOutOfBoundsException exception){
            }


            int enc1 = charCode1 >> 2;

            int enc2 = ((charCode1 & 3) << 4);

            if (charCode2 != null) {
                enc2 = enc2 | (charCode2 >> 4);
            }

            Integer enc3 = null;
            Integer enc4 = null;

            if (charCode2 != null) {
                enc3 = ((charCode2 & 15) << 2);
            }

            if (charCode3 != null) {
                enc3 = enc3 | (charCode3 >> 6);
                enc4 = charCode3 & 63;
            }

            if (charCode2 == null) {
                enc3 = enc4 = 64;
            } else if (charCode3 == null) {
                enc4 = 64;
            }

            output = output +
                keyStr.charAt(enc1) + keyStr.charAt(enc2) +
                keyStr.charAt(enc3) + keyStr.charAt(enc4);
        }

        return output;


    }
}
