package com.www.templatefreemarker.controller;

import com.www.templatefreemarker.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description LoginController
 * @Author 张卫刚
 * @Date Created on 2023/11/27
 */
@Controller
@RequestMapping(value = "/user")
public class LoginController {


    @PostMapping(value = "/login")
    public ModelAndView login(User user, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("redirect:/");
        modelAndView.addObject(user);

        request.getSession().setAttribute("user", user);
        return modelAndView;
    }


    @GetMapping(value = "/login")
    public ModelAndView login() {
        return new ModelAndView("page/login");
    }
}
