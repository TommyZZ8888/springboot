package com.www.task.quartz.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.task.quartz.common.SchedulerConst;
import com.www.task.quartz.common.SchedulerStatus;
import com.www.task.quartz.config.QuartzFactory;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.mapper.SchedulerJobMapper;
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
public interface QuartzService {




	public void timingTask();

	public void addJob(SchedulerJob schedulerJob);



	public void operateJob(SchedulerJob schedulerJob, SchedulerStatus schedulerStatus) ;

	public void startAll() throws SchedulerException;

	public void pauseAll() throws SchedulerException;
}
