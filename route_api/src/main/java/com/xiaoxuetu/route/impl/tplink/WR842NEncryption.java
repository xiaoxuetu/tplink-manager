package com.xiaoxuetu.route.impl.tplink;

/**
 * Created by kevin on 2017/2/21.
 */

public class WR842NEncryption {

    private static String utf8Encode(String password) {
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
    public static String base64Encoding(String password) {
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
