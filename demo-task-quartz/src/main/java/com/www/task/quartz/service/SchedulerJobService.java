package com.www.task.quartz.service;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.task.quartz.common.SpringContextUtil;
import com.www.task.quartz.common.SchedulerStatus;
import com.www.task.quartz.mapper.SchedulerJobMapper;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.entity.SchedulerJobLog;
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
@Service
public class SchedulerJobService {

	@Autowired
	private SchedulerJobMapper schedulerJobMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private QuartzService quartzService;

	@Autowired
	private SchedulerJobLogService schedulerJobLogService;

	/**
	 * 列表
	 *
	 * @param schedulerJob 查询条件
	 * @param page         分页
	 * @return 数据集合
	 */
	public Page<SchedulerJob> select(SchedulerJob schedulerJob, Page<SchedulerJob> page) {
		QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
		if (schedulerJob != null) {
			// 查询条件
			// 名称
			if (Validator.isNotEmpty(schedulerJob.getName())) {
				queryWrapper.like("name", schedulerJob.getName());
			}
			// cron表达式
			if (Validator.isNotEmpty(schedulerJob.getCode())) {
				queryWrapper.like("code", schedulerJob.getCode());
			}
			// bean
			if (Validator.isNotEmpty(schedulerJob.getBean())) {
				queryWrapper.like("bean", schedulerJob.getBean());
			}
			// 方法
			if (Validator.isNotEmpty(schedulerJob.getMethod())) {
				queryWrapper.like("method", schedulerJob.getMethod());
			}
		}
		page.setRecords(schedulerJobMapper.select(page, queryWrapper));
		return page;
	}

	public List<SchedulerJob> selectAll() {
		QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", SchedulerStatus.ENABLE.getCode());
		return schedulerJobMapper.selectList(queryWrapper);
	}

	/**
	 * 详情
	 *
	 * @param id id
	 * @return 详细信息
	 */
	public SchedulerJob get(String id) {
		return schedulerJobMapper.getById(id);
	}

	/**
	 * 新增
	 *
	 * @return 默认值
	 */
	public SchedulerJob add() {
		SchedulerJob schedulerJob = new SchedulerJob();
		// 默认开启
		schedulerJob.setStatus(SchedulerStatus.ENABLE.getCode());
		return schedulerJob;
	}

	/**
	 * 删除
	 *
	 * @param ids 数据ids
	 * @return 是否成功
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	public boolean remove(String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id", idList);
		List<SchedulerJob> schedulerJobs = schedulerJobMapper.selectSchedulerJobCodes(queryWrapper);
		boolean isSuccess = schedulerJobMapper.deleteBatchIds(idList)>0;
		if (isSuccess) {
			// 删除成功后删除任务
			for (SchedulerJob schedulerJob : schedulerJobs) {
				quartzService.operateJob(schedulerJob, SchedulerStatus.DELETE);
			}
		}
		return isSuccess;
	}

	/**
	 * 保存
	 *
	 * @param schedulerJob 表单内容
	 * @return 保存后信息
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	public SchedulerJob saveData(SchedulerJob schedulerJob) {
		// 更新前的任务名称
		String jobJobCode = null;
		schedulerJob.setEditDate(new Date());
		schedulerJob.setEditUser("1");
		//
		QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("code", schedulerJob.getCode());
		if (schedulerJob.getId() != null) {
			queryWrapper.ne("id", schedulerJob.getId());
		}
//		if (schedulerJobMapper.selectCount(queryWrapper) > 0) {
//			throw new RuntimeException("code[" + schedulerJob.getCode() + "]已存在");
//		}
		boolean isSuccess;
		if (StrUtil.isBlank(schedulerJob.getId())) {
			// 新增,设置默认值
			schedulerJob.setCreateDate(new Date());
			schedulerJob.setCreateUser("1");
		isSuccess =	schedulerJobMapper.insert(schedulerJob)>0;
		} else {
			jobJobCode = schedulerJobMapper.getJobCodeById(schedulerJob.getId());
			isSuccess =	schedulerJobMapper.updateById(schedulerJob)>0;
		}

		if (isSuccess) {
			// 如果是新增直接添加任务,如果是修改则删除原任务后重新添加
			if (StrUtil.isNotBlank(jobJobCode)) {
				quartzService.operateJob(new SchedulerJob(jobJobCode), SchedulerStatus.DELETE);
			}
			if (SchedulerStatus.ENABLE.getCode().equals(schedulerJob.getStatus())) {
				// 如果保存后是启用状态,添加到任务里
				quartzService.addJob(schedulerJob);
			}
		}
		return schedulerJob;
	}


	public void start(String id) {
		boolean updateSuccess = updateJobStatus(SchedulerStatus.ENABLE.getCode(), id);
		if (updateSuccess) {
			quartzService.operateJob(schedulerJobMapper.getById(id), SchedulerStatus.ENABLE);
		} else {
			throw new RuntimeException("更新任务状态失败");
		}
	}

	public void pause(String id) {
		boolean updateSuccess = updateJobStatus(SchedulerStatus.DISABLE.getCode(), id);
		if (updateSuccess) {
			quartzService.operateJob(schedulerJobMapper.getById(id), SchedulerStatus.DISABLE);
		} else {
			throw new RuntimeException("更新任务状态失败");
		}
	}

	public void startAll() {
		try {
			boolean updateSuccess = updateJobStatus(SchedulerStatus.ENABLE.getCode(), null);
			if (updateSuccess) {
				quartzService.startAll();
			} else {
				throw new RuntimeException("更新任务状态失败");
			}
		} catch (SchedulerException e) {
			logger.error("startAll()", e);
			throw new RuntimeException("开启任务失败");
		}
	}

	public void pauseAll() {
		try {
			boolean updateSuccess = updateJobStatus(SchedulerStatus.DISABLE.getCode(), null);
			if (updateSuccess) {
				quartzService.pauseAll();
			} else {
				throw new RuntimeException("更新任务状态失败");
			}
		} catch (SchedulerException e) {
			logger.error("pauseAll()", e);
			throw new RuntimeException("暂停任务失败");
		}
	}

	private boolean updateJobStatus(String status, String ids) {
		UpdateWrapper<SchedulerJob> updateWrapper = new UpdateWrapper<>();
		updateWrapper.set("status", status);
		if (StrUtil.isNotBlank(ids)) {
			updateWrapper.in("id", ids.split(","));
		}
		return schedulerJobMapper.update(new SchedulerJob(),updateWrapper) > 0;
	}


	public void updateLastRunDate(String id){
		if (id!= null){
			UpdateWrapper<SchedulerJob> wrapper = new UpdateWrapper<>();
			wrapper.set("last_run_date", new Date());
			wrapper.eq("id", id);
			schedulerJobMapper.update(new SchedulerJob(),wrapper);
		}
	}

	public boolean immediateExecution(String id) {
		SchedulerJob schedulerJob = schedulerJobMapper.getById(id);
		if (schedulerJob == null) {
			throw new RuntimeException("查询任务信息失败");
		}
		if (StrUtil.isBlank(schedulerJob.getBean())) {
			throw new RuntimeException("该任务未设置Bean信息，请设置后重试");
		}
		Object bean = SpringContextUtil.getBean(schedulerJob.getBean());
		if (bean != null) {
			try {
				// 获取方法
				Method method = bean.getClass().getMethod(schedulerJob.getMethod());
				try {
					Date startDate = new Date();
					// 执行
					method.invoke(bean);
					// 更新最后执行时间
					updateLastRunDate(schedulerJob.getId());
					// 保存执行日志
					schedulerJobLogService.saveData(new SchedulerJobLog(schedulerJob.getId(),
							startDate, System.currentTimeMillis() - startDate.getTime()));
				} catch (IllegalAccessException | InvocationTargetException e) {
					throw new RuntimeException(schedulerJob.getName() + "失败[" + e.getCause().getMessage() + "]");
				}
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("定时任务[" + schedulerJob.getName() + "]获取method[" + schedulerJob.getMethod() + "]失败");
			}
		} else {
			throw new RuntimeException("定时任务[" + schedulerJob.getName() + "]bean不存在");
		}
		return false;
	}
}
