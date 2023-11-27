package com.www.templatefreemarker.controller;

import cn.hutool.core.util.ObjectUtil;
import com.www.templatefreemarker.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description IndexController
 * @Author 张卫刚
 * @Date Created on 2023/11/27
 */
@Controller
@Slf4j
public class IndexController {

    @GetMapping(value = {"", "/"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");
        if (ObjectUtil.isNull(user)) {
            modelAndView.setViewName("redirect:/user/login");
        } else {
            modelAndView.setViewName("page/index");
            modelAndView.addObject(user);
        }
        return modelAndView;
    }

    public static void main(String[] args) {
        String name = "Alice";
        int age = 25;
        String message = STR."My name is \{name} and I'm \{age} years old.";
        System.out.println(message);

    }

}
