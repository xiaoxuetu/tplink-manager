package com.xiaoxuetu.shield.route.model;

/**
 * Created by kevin on 2017/1/14.
 */

public class CommandResult {

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
}
