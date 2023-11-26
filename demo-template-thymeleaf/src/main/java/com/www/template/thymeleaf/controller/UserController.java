package com.www.template.thymeleaf.controller;

import com.www.template.thymeleaf.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Describtion: UserController
 * @Author: 张卫刚
 * @Date: 2023/11/26 14:31
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
public ModelAndView login(User user, HttpServletRequest request){
    ModelAndView mv = new ModelAndView();
    mv.addObject(user);
    mv.setViewName("redirect:/");

    request.getSession().setAttribute("user",user);
    return mv;
}

@GetMapping("/login")
public ModelAndView login(){
    return new ModelAndView("page/login");
}

}
