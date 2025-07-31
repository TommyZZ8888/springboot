package com.www.demoes.service;

import com.www.demoes.config.EsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Describtion: EsService
 * @Author: 张卫刚
 * @Date: 2025/7/30 16:09
 */
@Service
public class EsService {

	@Autowired
	private EsUtil esUtil;

	public void testCreate() throws IOException {
		esUtil.createIndex("jd_data");
	}
}
