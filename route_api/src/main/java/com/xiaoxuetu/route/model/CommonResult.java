package com.xiaoxuetu.route.model;

import java.io.Serializable;

/**
 * Created by kevin on 2017/1/14.
 */

public class CommonResult implements Serializable {

    public static final int CODE_FAILURE = 0;

    public static final int CODE_SUCCESS = 1;

    private int code;

    private String message;

    private Object data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    private CommonResult() {}

    public boolean isFailure() {
        if (code == CODE_FAILURE) {
           return true;
        }
        return false;
    }

    public boolean isSuccess() {
        return !isFailure();
    }

    public static CommonResult success() {
        CommonResult commonResult = new CommonResult();
        commonResult.code = CODE_SUCCESS;
        return commonResult;
    }

    public static CommonResult success(String message) {
        CommonResult commonResult = success();
        commonResult.setMessage(message);
        return commonResult;
    }


    public static CommonResult success(String message, Object data) {
        CommonResult commonResult = success(message);
        commonResult.setData(data);
        return commonResult;
    }

    public  static CommonResult failure() {
        CommonResult commonResult = new CommonResult();
        commonResult.code = CODE_FAILURE;
        return commonResult;
    }

    public static CommonResult failure(String message) {
        CommonResult commonResult = failure();
        commonResult.setMessage(message);
        return commonResult;
    }

    public static CommonResult failure(String message, Object data) {
        CommonResult commonResult = failure(message);
        commonResult.setData(data);
        return commonResult;
    }
}
