package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.WorkflowTransitionEntity;

public interface WorkflowTransitionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowTransitionEntity record);

    int insertSelective(WorkflowTransitionEntity record);

    WorkflowTransitionEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowTransitionEntity record);

    int updateByPrimaryKey(WorkflowTransitionEntity record);
}