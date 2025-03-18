package com.www.task.quartz.service;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.task.quartz.common.SchedulerStatus;
import com.www.task.quartz.common.SpringContextUtil;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.entity.SchedulerJobLog;
import com.www.task.quartz.mapper.SchedulerJobMapper;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Describtion: SchedulerJobService
 * @Author: 张卫刚
 * @Date: 2025/3/18 10:26
 */
public interface SchedulerJobService extends IService<SchedulerJob> {


	public Page<SchedulerJob> select(SchedulerJob schedulerJob, Page<SchedulerJob> page);

	public List<SchedulerJob> selectAll();

	/**
	 * 详情
	 *
	 * @param id id
	 * @return 详细信息
	 */
	public SchedulerJob get(String id);

	/**
	 * 新增
	 *
	 * @return 默认值
	 */
	public SchedulerJob add();

	/**
	 * 删除
	 *
	 * @param ids 数据ids
	 * @return 是否成功
	 */
	public boolean remove(String ids);

	/**
	 * 保存
	 *
	 * @param schedulerJob 表单内容
	 * @return 保存后信息
	 */
;	public SchedulerJob saveData(SchedulerJob schedulerJob);


	public void start(String id);

	public void pause(String id);

	public void startAll();

	public void pauseAll();



	public void updateLastRunDate(String id);

	public boolean immediateExecution(String id);
}
