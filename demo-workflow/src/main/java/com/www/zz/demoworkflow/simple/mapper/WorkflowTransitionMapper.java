package com.www.zz.demoworkflow.simple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.www.zz.demoworkflow.simple.domain.entity.WorkflowTransitionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describtion: WorkflowTransitionMapper
 * @Author: 张卫刚
 * @Date: 2025/7/24 11:17
 */
@Mapper
public interface WorkflowTransitionMapper extends BaseMapper<WorkflowTransitionEntity> {
}
