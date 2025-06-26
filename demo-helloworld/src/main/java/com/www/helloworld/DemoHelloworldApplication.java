package com.www.helloworld;

import com.www.zz.TomTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoHelloworldApplication {

    public static void main(String[] args) {
        System.out.println(TomTest.sayHello());
        SpringApplication.run(DemoHelloworldApplication.class, args);
    }

}
