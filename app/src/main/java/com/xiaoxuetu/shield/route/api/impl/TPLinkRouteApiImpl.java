package com.xiaoxuetu.shield.route.api.impl;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.model.CommandResult;
import com.xiaoxuetu.shield.route.model.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by kevin on 2017/1/10.
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
    public CommandResult login(String ip, String password) {
        this.ip = ip;
        this.password = password;

        String auth = "Basic " + base64Encoding("admin:" + password);
        this.cookie = TextUtils.htmlEncode(auth).replaceAll(" ", "%20");
        Log.d(TAG, "cookie is " + this.cookie);
        return CommandResult.success();

    }

    @Override
    public CommandResult getDevices() {
        if (TextUtils.isEmpty(cookie)) {
            return null;
        }

        String url = "http://" + this.ip + "/userRpm/AssignedIpAddrListRpm.htm";
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
                parseHtml(resultHtml);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private CommandResult parseHtml(String resultHtml) {
        CommandResult commandResult;
        String pattern = "var DHCPDynList=new Array[-(\\s\\w\",.:);]+";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(resultHtml);

        if (matcher.find()) {

            String result = matcher.group();
            result = result.replace("var DHCPDynList=new Array(", "");
            String[] resultArray = result.split(",");
            Log.d(TAG, Arrays.toString(resultArray));

            //
            if ((resultArray.length - 2) < 1) {
                commandResult = CommandResult.success();
            } else if ((resultArray.length - 2) % 4 == 0) {
                int i = 0;
                List<Device> deviceList = new ArrayList<>();
                while (i < (resultArray.length - 2)) {
                    String deviceName = resultArray[i++];
                    String macAddress = resultArray[i++];
                    String ipAddress  = resultArray[i++];
                    String validTime  = resultArray[i++];

                    Device device = new Device(deviceName, macAddress, ipAddress, validTime);
                    deviceList.add(device);
                }
                Log.d(TAG, deviceList.toString());
                commandResult = CommandResult.success("解析成功", deviceList);
            } else {
                commandResult = CommandResult.failure("解析错误，没有获取到设备列表");
            }
        } else {
            commandResult = CommandResult.failure("解析失败，没有获取到设备列表");
        }

        return commandResult;

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
            int charCode1 = password.codePointAt(i++);
            int charCode2 = password.codePointAt(i++);
            int charCode3 = password.codePointAt(i++);

            int enc1 = charCode1 >> 2;
            int enc2 = ((charCode1 & 3) << 4) | (charCode2 >> 4);
            int enc3 = ((charCode2 & 15) << 2) | (charCode3 >> 6);
            int enc4 = charCode3 & 63;

            if (Float.isNaN(charCode2)) {
                enc3 = enc4 = 64;
            } else if (Float.isNaN(charCode3)) {
                enc4 = 64;
            }

            output = output +
                keyStr.charAt(enc1) + keyStr.charAt(enc2) +
                keyStr.charAt(enc3) + keyStr.charAt(enc4);
        }

        return output;


    }
}
