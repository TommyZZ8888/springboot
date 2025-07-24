package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * workflow_transition
 * @author 
 */
@TableName("workflow_transition")
@Data
public class WorkflowTransitionEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 所属工作流ID
     */
    private Integer workflowId;

    /**
     * 起始节点ID
     */
    private Integer fromNodeId;

    /**
     * 目标节点ID
     */
    private Integer toNodeId;

    /**
     * 流转名称
     */
    private String name;

    /**
     * 条件表达式
     */
    private String conditionExpression;

    /**
     * 优先级
     */
    private Integer priority;

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