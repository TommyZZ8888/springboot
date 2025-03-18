package com.www.task.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.task.quartz.entity.SchedulerJobLog;
import com.www.task.quartz.service.SchedulerJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Describtion: SchedulerJobLogController
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:38
 */
@RestController
@RequestMapping("/api/scheduler/job/log")
public class SchedulerJobLogController {

	/**
	 * 定时任务执行日志 service
	 */
	@Autowired
	private SchedulerJobLogService service;


	/**
	 * 列表
	 *
	 * @param schedulerJobLog 查询条件
	 * @return Page<SchedulerJobLog>
	 */
	@GetMapping()
	public Page<SchedulerJobLog> select(SchedulerJobLog schedulerJobLog, Page<SchedulerJobLog> page) {
		return service.select(schedulerJobLog, page);
	}
}
