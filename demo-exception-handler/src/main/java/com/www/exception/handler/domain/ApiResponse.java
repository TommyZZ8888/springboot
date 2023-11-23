package com.www.exception.handler.domain;

import com.www.exception.handler.enums.Status;
import com.www.exception.handler.exception.BaseException;
import lombok.Data;

/**
 * @Description ApiResponse
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */
@Data
public class ApiResponse {

    private Integer code;

    private String msg;

    private Object data;

    private ApiResponse() {

    }

    private ApiResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResponse of(Integer code, String msg, Object data) {
        return new ApiResponse(code, msg, data);
    }

    public static ApiResponse ofSuccess(Object data) {
        return ofStatus(Status.Ok, data);
    }

    public static ApiResponse ofSuccess(String message) {
        return of(Status.Ok.getCode(), message, null);
    }

    public static ApiResponse ofFail() {
        return ofStatus(Status.UNKNOWN_ERROR);
    }

    /**
     * 构造一个有状态的API返回
     * @param status 状态 {@link Status}
     * @return ApiResponse
     */
    public static ApiResponse ofStatus(Status status) {
        return ofStatus(status, null);
    }

    /**
     * 构造一个有状态且带数据的API返回
     * @param status 状态 {@link Status}
     * @param data   返回数据
     * @return ApiResponse
     */
    public static ApiResponse ofStatus(Status status, Object data) {
        return of(status.getCode(), status.getMessage(), data);
    }

    public static <T extends BaseException> ApiResponse ofException(T t, Object data) {
        return of(t.getCode(), t.getMessage(), data);
    }

    public static <T extends BaseException> ApiResponse ofException(T t) {
        return ofException(t, null);
    }
}
