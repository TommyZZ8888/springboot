package com.www.task.quartz.entity;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Date;

/**
 * @Describtion: SchedulerJob
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:37
 */
@Data
@TableName("scheduler_job")
public class SchedulerJob{

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    /**
     * 名称
     */
    private String name;
    /**
     * 代码
     */
    @NotBlank(message = "代码不能为空")
    private String code;
    /**
     * cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cron;

    /**
     * bean
     */
    @NotBlank(message = "bean不能为空")
    private String bean;

    /**
     * method
     */
    @NotBlank(message = "method不能为空")
    private String method;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private String status;

    /**
     * 备注
     */
    private String remarks;
    /**
     * 是否系统
     */
    private String sys;

    /**
     * 上次执行时间
     */
    private Date lastRunDate;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createDate;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String editUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date editDate;

    //

    public SchedulerJob() {
    }

    public SchedulerJob(String code) {
        this.code = code;
    }

}
