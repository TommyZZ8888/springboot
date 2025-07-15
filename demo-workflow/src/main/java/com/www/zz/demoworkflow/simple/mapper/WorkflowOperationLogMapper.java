package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.WorkflowOperationLogEntity;

public interface WorkflowOperationLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowOperationLogEntity record);

    int insertSelective(WorkflowOperationLogEntity record);

    WorkflowOperationLogEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowOperationLogEntity record);

    int updateByPrimaryKey(WorkflowOperationLogEntity record);
}