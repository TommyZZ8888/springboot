package com.www.exception.handler.enums;

/**
 * @Description Status
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */


public enum Status {

    Ok(200, "操作成功"),
    UNKNOWN_ERROR(500, "服务器出错啦"),
    ;

    private Integer code;

    private String message;

    Status(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
