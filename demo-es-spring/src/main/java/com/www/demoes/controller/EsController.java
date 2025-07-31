package com.www.demoes.controller;

import com.www.demoes.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Describtion: EsController
 * @Author: 张卫刚
 * @Date: 2025/7/30 16:09
 */
@RestController
@RequestMapping("/es")
public class EsController {
	@Autowired
	private EsService esService;


	@PostMapping("/createIndex")
	public String putAll() throws IOException {
		esService.testCreate();
		return "success";
	}
}
