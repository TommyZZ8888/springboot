package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * workflow_operation_log
 * @author 
 */
@TableName("workflow_operation_log")
@Data
public class WorkflowOperationLogEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 流程实例ID
     */
    private Integer instanceId;

    /**
     * 关联的任务ID
     */
    private Integer taskId;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String operationDesc;

    /**
     * 操作人ID
     */
    private Integer operatorId;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 操作结果
     */
    private String operationResult;

    /**
     * 备注
     */
    private String comments;

    /**
     * 详细数据
     */
    private Object details;

    private static final long serialVersionUID = 1L;
}