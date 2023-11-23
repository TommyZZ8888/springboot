package com.www.exception.handler.controller;

import com.www.exception.handler.domain.ApiResponse;
import com.www.exception.handler.enums.Status;
import com.www.exception.handler.exception.JsonException;
import com.www.exception.handler.exception.PageException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description TestController
 * @Author 张卫刚
 * @Date Created on 2023/11/23
 */
@Controller
public class TestController {

    @GetMapping("/json")
    @ResponseBody
    public ApiResponse jsonException() {
        throw new JsonException(Status.UNKNOWN_ERROR);
    }

    @GetMapping("/page")
    public ModelAndView pageException() {
        throw new PageException(Status.UNKNOWN_ERROR);
    }
}
