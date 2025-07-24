package com.www.zz.demoworkflow.simple.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.TaskInstanceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: TaskInstanceMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:16
 */
@Mapper
public interface TaskInstanceMapper extends BaseMapper<TaskInstanceEntity> {
}
