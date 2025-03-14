package com.www.task.quartz.test;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class HelloSpringService {

    @Autowired
    DataSource dataSource;

    @PostConstruct
    public void ds() {
        System.out.println("-=-=-==-"+dataSource);
    }

    public String helloSpring() {
        return "hello spring";
    }
}
