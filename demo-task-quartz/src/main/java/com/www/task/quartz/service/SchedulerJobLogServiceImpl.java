package com.www.task.quartz.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.task.quartz.mapper.SchedulerJobLogMapper;
import com.www.task.quartz.entity.SchedulerJobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Describtion: SchedulerJobLogService
 * @Author: 张卫刚
 * @Date: 2025/3/18 10:26
 */
@Service
public class SchedulerJobLogServiceImpl  extends ServiceImpl<SchedulerJobLogMapper, SchedulerJobLog> implements SchedulerJobLogService {

	@Autowired
	private SchedulerJobLogMapper schedulerJobLogMapper;

	public Page<SchedulerJobLog> select(SchedulerJobLog schedulerJobLog, Page<SchedulerJobLog> page) {
		QueryWrapper<SchedulerJobLog> queryWrapper = new QueryWrapper<>();
		if (schedulerJobLog == null || StrUtil.isBlank(schedulerJobLog.getSchedulerJobId())) {
			return page;
		}
		queryWrapper.eq("scheduler_job_id", schedulerJobLog.getSchedulerJobId());
		List<OrderItem> orders = page.orders();
		orders.add(OrderItem.desc("run_date"));
		page.setOrders(orders);
		return page(page, queryWrapper);
	}



	public void saveData(SchedulerJobLog schedulerJobLog) {
		if (schedulerJobLog.getId() == null){
			baseMapper.insert(schedulerJobLog);
		}
		SchedulerJobLog schedulerJobLog1 = baseMapper.selectById(schedulerJobLog.getId());
		BeanUtil.copyProperties(schedulerJobLog, schedulerJobLog1);
		baseMapper.updateById(schedulerJobLog1);
	}
}
