package com.www.zz.demoworkflow.simple.domain.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * workflow_definition
 * @author 
 */
@Data
public class WorkflowDefinitionEntity implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 工作流描述
     */
    private String description;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 是否激活(1:激活,0:禁用)
     */
    private Boolean isActive;

    /**
     * 创建人ID
     */
    private Integer createdBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新人ID
     */
    private Integer updatedBy;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private static final long serialVersionUID = 1L;
}