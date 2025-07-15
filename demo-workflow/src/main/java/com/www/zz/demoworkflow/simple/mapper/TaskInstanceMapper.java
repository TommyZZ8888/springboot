package com.www.zz.demoworkflow.simple.mapper;

import com.www.zz.demoworkflow.simple.domain.entity.TaskInstanceEntity;

public interface TaskInstanceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TaskInstanceEntity record);

    int insertSelective(TaskInstanceEntity record);

    TaskInstanceEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TaskInstanceEntity record);

    int updateByPrimaryKey(TaskInstanceEntity record);
}