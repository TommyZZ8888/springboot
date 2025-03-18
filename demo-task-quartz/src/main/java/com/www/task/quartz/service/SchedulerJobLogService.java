package com.www.task.quartz.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.task.quartz.entity.SchedulerJobLog;
import com.www.task.quartz.mapper.SchedulerJobLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Describtion: SchedulerJobLogService
 * @Author: 张卫刚
 * @Date: 2025/3/18 10:26
 */
public interface SchedulerJobLogService  extends IService<SchedulerJobLog> {



	 Page<SchedulerJobLog> select(SchedulerJobLog schedulerJobLog, Page<SchedulerJobLog> page);



	 void saveData(SchedulerJobLog schedulerJobLog);
}
