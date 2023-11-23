package com.www.exception.handler.exception;

import com.www.exception.handler.enums.Status;
import lombok.Data;

/**
 * @Description BaseException
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */
@Data
public class BaseException extends RuntimeException{

    private Integer code;

    private String message;

    public BaseException(Status status) {
        super(status.getMessage());
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
