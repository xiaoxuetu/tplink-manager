package com.xiaoxuetu.shield.route.api.impl;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.xiaoxuetu.shield.route.api.IRouteApi;
import com.xiaoxuetu.shield.route.model.Device;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by kevin on 2017/1/10.
 */

public class TPLinkRouteApiImpl implements IRouteApi {

    private final String TAG = this.getClass().getName();

    private String ip;
    private String password;

    private String cookie;

    @Override
    public boolean login(String ip, String password) {
        this.ip = ip;
        this.password = password;

        String auth = "Basic " + base64Encoding("admin:" + password);
        this.cookie = TextUtils.htmlEncode(auth).replaceAll(" ", "%20");
        Log.d(TAG, "cookie is " + this.cookie);
        return true;

    }

    @Override
    public List<Device> getDevices() {
        return null;
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
