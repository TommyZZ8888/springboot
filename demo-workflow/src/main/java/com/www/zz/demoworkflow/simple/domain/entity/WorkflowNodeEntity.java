package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * workflow_node
 * @author 
 */
@Data
public class WorkflowNodeEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 所属工作流ID
     */
    private Integer workflowId;

    /**
     * 节点唯一标识
     */
    private String nodeKey;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private Object nodeType;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 节点在流程图中的X坐标
     */
    private Integer positionX;

    /**
     * 节点在流程图中的Y坐标
     */
    private Integer positionY;

    /**
     * 节点配置(JSON格式)
     */
    private Object config;

    /**
     * 排序序号
     */
    private Integer sortOrder;

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