package com.www.zz.demoworkflow.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowOperationLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: WorkflowOperationLogMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:15
 */
@Mapper
public interface WorkflowOperationLogMapper extends BaseMapper<WorkflowOperationLogEntity> {
}
