package com.www.task.quartz.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.www.task.quartz.common.SpringContextUtil;
import com.www.task.quartz.common.SchedulerConst;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.entity.SchedulerJobLog;
import com.www.task.quartz.service.SchedulerJobLogService;
import com.www.task.quartz.service.SchedulerJobServiceImpl;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Describtion: QuartzFactory
 * @Author: 张卫刚
 * @Date: 2025/3/18 10:29
 */
@Component
public class QuartzFactory implements Job {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		SchedulerJob schedulerJob = (SchedulerJob) jobExecutionContext.getMergedJobDataMap().get(SchedulerConst.SCHEDULER_JOB_KEY);
		if (StrUtil.isBlank(schedulerJob.getBean())) logger.warn("定时任务[" + schedulerJob.getName() + "]缺少bean");
		Object bean = SpringContextUtil.getBean(schedulerJob.getBean());
		if (ObjectUtil.isNull(bean)) logger.warn("定时任务[" + schedulerJob.getName() + "]bean不存在");

		try {
			Method method = bean.getClass().getMethod(schedulerJob.getMethod());
			Date date = new Date();
			method.invoke(bean);
			saveJobLog(schedulerJob, date);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			logger.warn("调用定时任务[" + schedulerJob.getName() + "] method[" + schedulerJob.getMethod() + "]失败", e);
		}
	}


	private void updateLastRunDate(String id) {
		SchedulerJobServiceImpl schedulerJobService = SpringContextUtil.getBean("schedulerJobService");
		schedulerJobService.updateLastRunDate(id);
	}

	private void saveJobLog(SchedulerJob schedulerJob, Date startDate) {
		SchedulerJobLogService schedulerJobLogService = SpringContextUtil.getBean("schedulerJobLogService");
		schedulerJobLogService.saveData(new SchedulerJobLog(schedulerJob.getId(),
				startDate, System.currentTimeMillis() - startDate.getTime()));
	}

}
