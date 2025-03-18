package com.www.task.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.service.SchedulerJobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Describtion: SchedulerJobController
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:37
 */
@RestController
@RequestMapping("/api/scheduler/job")
public class SchedulerJobController {
	/**
	 * 定时任务  service
	 */
	@Autowired
	private SchedulerJobService service;

	/**
	 * 列表
	 *
	 * @param schedulerJob 查询条件
	 * @return Page<SchedulerJob>
	 */
	@GetMapping()
	public Page<SchedulerJob> select(SchedulerJob schedulerJob, Page<SchedulerJob> page) {
		return service.select(schedulerJob, page);
	}

	/**
	 * 详情
	 *
	 * @param id id
	 * @return SchedulerJob
	 */
	@GetMapping("{id}")
	public SchedulerJob get(@PathVariable("id") String id) {
		return service.get(id);
	}

	/**
	 * 新增
	 *
	 * @return SchedulerJob
	 */
	@GetMapping("/add")
	public SchedulerJob add() {
		return service.add();
	}

	/**
	 * 删除
	 *
	 * @param ids 数据ids
	 * @return true/false
	 */
	@DeleteMapping("{ids}")
	public boolean remove(@PathVariable("ids") String ids) {
		return service.remove(ids);
	}

	/**
	 * 保存
	 *
	 * @param schedulerJob 表单内容
	 * @return SchedulerJob
	 */
	@PostMapping()
	public void saveData(@Valid @RequestBody SchedulerJob schedulerJob) {
		 service.saveData(schedulerJob);
	}

	/**
	 * 开启
	 *
	 * @param id 数据id
	 * @return true/false
	 */
	@PostMapping("start/{id}")
	public boolean start(@PathVariable("id") String id) {
		service.start(id);
		return true;
	}

	/**
	 * 暂停
	 *
	 * @param id 数据id
	 * @return true/false
	 */
	@PostMapping("pause/{id}")
	public boolean pause(@PathVariable("id") String id) {
		service.pause(id);
		return true;
	}

	/**
	 * 全部开启
	 *
	 * @return true/false
	 */
	@PostMapping("start/all")
	public boolean startAll() {
		service.startAll();
		return true;
	}

	/**
	 * 全部暂停
	 *
	 * @return true/false
	 */
	@PostMapping("pause/all")
	public boolean pauseAll() {
		service.pauseAll();
		return true;
	}

	/**
	 * 立即执行指定任务
	 *
	 * @param id 数据id
	 * @return true/false
	 */
	@PostMapping("immediate/execution/{id}")
	public boolean immediateExecution(@PathVariable("id") String id) {
		return service.immediateExecution(id);
	}
}
