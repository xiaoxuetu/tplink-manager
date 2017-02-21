package com.xiaoxuetu.route.impl.tplink;



import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by kevin on 2017/2/21.
 */

public class WR842NRequestor {


    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    public static String request(String ip, String url, String cookie) {
        url = "http://" + ip + url;

        String cookieTemp = "Authorization=" + cookie + "; ChgPwdSubTag=";
        String referer = "http://" + ip +"/userRpm/MenuRpm.htm";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2)"
                + " AppleWebKit/537.36 (KHTML, like Gecko)"
                + " Chrome/55.0.2883.95 Safari/537.36";

        Request request = new Request.Builder()
                .addHeader("Cookie", cookieTemp)
                .addHeader("Referer", referer)
                .addHeader("User-Agent", userAgent)
                .url(url)
                .build();

        String html = null;
        okhttp3.Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                html =  response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
        }
        return html;
    }

}
