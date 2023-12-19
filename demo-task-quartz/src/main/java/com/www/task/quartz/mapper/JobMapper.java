package com.www.task.quartz.mapper;

import com.www.task.quartz.entity.domain.JobAndTrigger;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description JobMapper
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
@Component
public interface JobMapper {

    List<JobAndTrigger> list();
}
