package com.www.task.xxl.job.task;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description DemoTask
 * @Author 张卫刚
 * @Date Created on 2023/12/20
 */
@Slf4j
@Component
public class DemoTask {

    /**
     * execute handler, invoked when executor receives a scheduling request
     *
     * @return 执行状态
     * @throws Exception 任务异常
     */

    @XxlJob(value = "demoTask")
    public void demoJobHandler() throws Exception {
        log.info("111");
    }
}
