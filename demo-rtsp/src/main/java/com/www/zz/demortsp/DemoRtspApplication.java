package com.www.zz.demortsp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class DemoRtspApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoRtspApplication.class, args);
	}

}
