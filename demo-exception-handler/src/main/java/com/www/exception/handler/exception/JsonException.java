package com.www.exception.handler.exception;

import com.www.exception.handler.enums.Status;
import lombok.Getter;

/**
 * @Description JsonException
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */
@Getter
public class JsonException extends BaseException {


    public JsonException(Status status) {
        super(status);
    }

    public JsonException(Integer code, String message) {
        super(code, message);
    }
}
