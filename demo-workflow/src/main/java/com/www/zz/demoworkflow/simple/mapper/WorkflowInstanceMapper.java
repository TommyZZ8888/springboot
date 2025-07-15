package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.WorkflowInstanceEntity;

public interface WorkflowInstanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowInstanceEntity record);

    int insertSelective(WorkflowInstanceEntity record);

    WorkflowInstanceEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowInstanceEntity record);

    int updateByPrimaryKey(WorkflowInstanceEntity record);
}