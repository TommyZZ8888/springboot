package com.www.zz.demoworkflow.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowNodeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: WorkflowNodeMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:18
 */
@Mapper
public interface WorkflowNodeMapper extends BaseMapper<WorkflowNodeEntity> {
}
