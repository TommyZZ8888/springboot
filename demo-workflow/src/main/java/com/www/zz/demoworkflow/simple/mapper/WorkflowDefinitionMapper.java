package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.WorkflowDefinitionEntity;

public interface WorkflowDefinitionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WorkflowDefinitionEntity record);

    int insertSelective(WorkflowDefinitionEntity record);

    WorkflowDefinitionEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkflowDefinitionEntity record);

    int updateByPrimaryKey(WorkflowDefinitionEntity record);
}