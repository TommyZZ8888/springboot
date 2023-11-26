package com.www.template.thymeleaf.controller;


import cn.hutool.core.util.ObjectUtil;
import com.www.template.thymeleaf.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Describtion: TestController
 * @Author: 张卫刚
 * @Date: 2023/11/25 17:10
 */
@Controller
public class TestController {

    @GetMapping({"", "/"})
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        if (ObjectUtil.isNull(user)) {
            mv.setViewName("redirect:/user/login");
        } else {
            mv.setViewName("page/index");
            mv.addObject(user);
        }
        return mv;
    }


}
