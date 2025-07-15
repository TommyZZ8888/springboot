package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * workflow_instance
 * @author 
 */
@Data
public class WorkflowInstanceEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 工作流定义ID
     */
    private Integer workflowId;

    /**
     * 关联业务KEY
     */
    private String businessKey;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 状态
     */
    private Object status;

    /**
     * 发起人ID
     */
    private Integer starterId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 当前节点ID
     */
    private Integer currentNodeId;

    /**
     * 流程变量
     */
    private Object variables;

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