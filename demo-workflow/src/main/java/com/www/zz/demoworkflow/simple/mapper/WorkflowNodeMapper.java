package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.WorkflowNodeEntity;

public interface WorkflowNodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowNodeEntity record);

    int insertSelective(WorkflowNodeEntity record);

    WorkflowNodeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowNodeEntity record);

    int updateByPrimaryKey(WorkflowNodeEntity record);
}