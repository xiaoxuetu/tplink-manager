package com.xiaoxuetu.shield.route.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 2017/1/14.
 */

public class CommonResult implements Parcelable {

    public static final int CODE_FAILURE = 0;

    public static final int CODE_SUCCESS = 1;

    private int code;

    private String message;

    private Object data;

    protected CommonResult(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator<CommonResult> CREATOR = new Creator<CommonResult>() {
        @Override
        public CommonResult createFromParcel(Parcel in) {
            return new CommonResult(in);
        }

        @Override
        public CommonResult[] newArray(int size) {
            return new CommonResult[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }
}
