package com.www.task.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.task.quartz.mapper.SchedulerJobMapper;
import com.www.task.quartz.entity.SchedulerJob;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@SpringBootTest
class DemoTaskQuartzApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private SchedulerJobMapper schedulerJobMapper;

    @Test
    public void test() {



        QueryWrapper<SchedulerJob> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", "1");
        Long l = schedulerJobMapper.selectCount(queryWrapper);
        System.out.println(l);
    }
}
