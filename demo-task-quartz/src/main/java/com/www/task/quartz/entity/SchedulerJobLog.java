package com.www.task.quartz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Describtion: SchedulerJobLog
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:37
 */
@Data
@TableName("scheduler_job_log")
public class SchedulerJobLog{

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 任务id
     */
    private String schedulerJobId;

    /**
     * 执行时间
     */
    private Date runDate;

    /**
     * 耗时
     */
    private Long timeConsuming;

    //

    public SchedulerJobLog() {
    }

    public SchedulerJobLog(String schedulerJobId, Date runDate, Long timeConsuming) {
        this.schedulerJobId = schedulerJobId;
        this.runDate = runDate;
        this.timeConsuming = timeConsuming;
    }
}
