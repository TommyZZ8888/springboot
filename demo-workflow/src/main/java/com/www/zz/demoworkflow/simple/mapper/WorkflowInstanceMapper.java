package com.www.zz.demoworkflow.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowInstanceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: WorkflowInstanceMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:16
 */
@Mapper
public interface WorkflowInstanceMapper extends BaseMapper<WorkflowInstanceEntity> {
}
