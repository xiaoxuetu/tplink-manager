package com.xiaoxuetu.shield.route.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kevin on 2017/1/14.
 */

public class CommandResult implements Parcelable {

    public static final int CODE_FAILURE = 0;

    public static final int CODE_SUCCESS = 1;

    private int code;

    private String message;

    private Object data;

    protected CommandResult(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator<CommandResult> CREATOR = new Creator<CommandResult>() {
        @Override
        public CommandResult createFromParcel(Parcel in) {
            return new CommandResult(in);
        }

        @Override
        public CommandResult[] newArray(int size) {
            return new CommandResult[size];
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

    private CommandResult() {}

    public static CommandResult success() {
        CommandResult commandResult = new CommandResult();
        commandResult.code = CODE_SUCCESS;
        return commandResult;
    }

    public static CommandResult success(String message) {
        CommandResult commandResult = success();
        commandResult.setMessage(message);
        return commandResult;
    }


    public static CommandResult success(String message, Object data) {
        CommandResult commandResult = success(message);
        commandResult.setData(data);
        return commandResult;
    }

    public  static CommandResult failure() {
        CommandResult commandResult = new CommandResult();
        commandResult.code = CODE_FAILURE;
        return commandResult;
    }

    public static CommandResult failure(String message) {
        CommandResult commandResult = failure();
        commandResult.setMessage(message);
        return commandResult;
    }

    public static CommandResult failure(String message, Object data) {
        CommandResult commandResult = failure(message);
        commandResult.setData(data);
        return commandResult;
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
