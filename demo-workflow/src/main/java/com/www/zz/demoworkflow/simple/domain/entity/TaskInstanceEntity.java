package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * task_instance
 * @author 
 */
@Data
public class TaskInstanceEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 流程实例ID
     */
    private Integer instanceId;

    /**
     * 节点定义ID
     */
    private Integer nodeId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 处理人ID
     */
    private Integer assigneeId;

    /**
     * 状态
     */
    private Object status;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 完成时间
     */
    private Date endTime;

    /**
     * 截止时间
     */
    private Date dueTime;

    /**
     * 审批意见
     */
    private String comments;

    /**
     * 表单数据
     */
    private Object formData;

    /**
     * 操作人ID
     */
    private Integer operationBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}