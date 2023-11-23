package com.www.exception.handler.exception;

import com.www.exception.handler.enums.Status;
import lombok.Getter;

/**
 * @Description PageException
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */
@Getter
public class PageException extends BaseException{

    public PageException(Status status) {
        super(status);
    }

    public PageException(Integer code, String message) {
        super(code, message);
    }
}
