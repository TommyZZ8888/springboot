package com.www.task.quartz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.task.quartz.common.SchedulerConst;
import com.www.task.quartz.common.SchedulerStatus;
import com.www.task.quartz.config.QuartzFactory;
import com.www.task.quartz.mapper.SchedulerJobMapper;
import com.www.task.quartz.entity.SchedulerJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Describtion: QuartzService
 * @Author: 张卫刚
 * @Date: 2025/3/18 10:26
 */
@Service
public class QuartzServiceImpl implements QuartzService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private SchedulerJobMapper schedulerJobMapper;

	public void timingTask() {
		QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", SchedulerStatus.ENABLE.getCode());
		List<SchedulerJob> schedulerJobs = schedulerJobMapper.selectList(queryWrapper);
		if (schedulerJobs != null) {
			schedulerJobs.forEach(this::addJob);
		}
	}

	public void addJob(SchedulerJob schedulerJob) {
		if (checkJob(schedulerJob)) {
			// 添加之前检查是否已经添加该任务
			JobKey jobKey = new JobKey(schedulerJob.getCode());
			JobDetail jobDetail = null;
			try {
				jobDetail = scheduler.getJobDetail(jobKey);
			} catch (SchedulerException e) {
				logger.debug("获取任务失败[{}]", schedulerJob.getCode(), e);
			}
			if (jobDetail == null) {
				// 创建触发器
				Trigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerJob.getCode())
						.withSchedule(CronScheduleBuilder.cronSchedule(schedulerJob.getCron()))
						.startNow()
						.build();
				// 创建任务
				jobDetail = JobBuilder.newJob(QuartzFactory.class)
						.withIdentity(schedulerJob.getCode())
						.build();
				// 传入调度任务对象
				jobDetail.getJobDataMap().put(SchedulerConst.SCHEDULER_JOB_KEY, schedulerJob);
				try {
					scheduler.scheduleJob(jobDetail, trigger);
				} catch (SchedulerException e) {
					logger.warn("创建定时任务失败", e);
				}
			}
		}
	}

	/**
	 * 检查任务是否可以添加到定时任务
	 *
	 * @param schedulerJob 任务
	 * @return true/false
	 */
	private boolean checkJob(SchedulerJob schedulerJob) {
		return schedulerJob != null &&
				StrUtil.isNotBlank(schedulerJob.getCode()) &&
				StrUtil.isNotBlank(schedulerJob.getCron()) &&
				StrUtil.isNotBlank(schedulerJob.getBean()) &&
				StrUtil.isNotBlank(schedulerJob.getMethod());
	}

	public void operateJob(SchedulerJob schedulerJob, SchedulerStatus schedulerStatus) {
		JobKey jobKey = new JobKey(schedulerJob.getCode());
		try {
			JobDetail jobDetail = scheduler.getJobDetail(jobKey);
			if (jobDetail == null) {
				// 如果任务里没有则添加任务
				addJob(schedulerJob);
			}
			switch (schedulerStatus) {
				case ENABLE:
					scheduler.resumeJob(jobKey);
					break;
				case DISABLE:
					scheduler.pauseJob(jobKey);
					break;
				case DELETE:
					scheduler.deleteJob(jobKey);
					break;
				default:
			}
		} catch (SchedulerException e) {
			logger.warn("获取定时任务[" + schedulerJob.getCode() + "]失败", e);
		}
	}

	public void startAll() throws SchedulerException {
		// 全部开始之前要将所有已开启任务添加到任务里
		timingTask();
		scheduler.start();
	}

	public void pauseAll() throws SchedulerException {
		scheduler.standby();
	}
}
