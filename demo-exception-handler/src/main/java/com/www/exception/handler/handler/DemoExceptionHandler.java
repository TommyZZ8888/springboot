package com.www.exception.handler.handler;

import com.www.exception.handler.domain.ApiResponse;
import com.www.exception.handler.exception.JsonException;
import com.www.exception.handler.exception.PageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description DemoExceptionHandler
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */

@ControllerAdvice
@Slf4j
public class DemoExceptionHandler {

    private final static String DEFAULT_ERROR_VIEW = "error";


    @ExceptionHandler(value = JsonException.class)
    @ResponseBody
    public ApiResponse jsonErrorHandler(JsonException jsonException){
        log.error("【JsonException】:{}", jsonException.getMessage());
        return ApiResponse.ofException(jsonException);
    }

    @ExceptionHandler(value = PageException.class)
    public ModelAndView pageErrorHandler(PageException exception) {
        log.error("【DemoPageException】:{}", exception.getMessage());
        ModelAndView view = new ModelAndView();
        view.addObject("message", exception.getMessage());
        view.setViewName(DEFAULT_ERROR_VIEW);
        return view;
    }
}
