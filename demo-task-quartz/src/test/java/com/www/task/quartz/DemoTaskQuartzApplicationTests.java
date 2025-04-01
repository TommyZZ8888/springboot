package com.www.task.quartz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.task.quartz.mapper.SchedulerJobMapper;
import com.www.task.quartz.entity.SchedulerJob;
import com.www.task.quartz.service.QuartzService;
import com.www.task.quartz.service.SchedulerJobService;
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
    private SchedulerJobService schedulerJobService;

    @Test
    public void test() {



        System.out.println();
    }
}
