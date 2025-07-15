CREATE TABLE workflow_instance
(
    id              INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    workflow_id     INT NOT NULL COMMENT '工作流定义ID',
    business_key    VARCHAR(100) COMMENT '关联业务KEY',
    business_type   VARCHAR(50) COMMENT '业务类型',
    status          ENUM('running', 'completed', 'terminated', 'error') NOT NULL DEFAULT 'running' COMMENT '状态',
    starter_id      INT NOT NULL COMMENT '发起人ID',
    start_time      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time        TIMESTAMP NULL COMMENT '结束时间',
    current_node_id INT COMMENT '当前节点ID',
    variables       JSON COMMENT '流程变量',
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (workflow_id) REFERENCES workflow_definition (id),
    FOREIGN KEY (current_node_id) REFERENCES workflow_node (id),
    KEY             idx_workflow_id (workflow_id),
    KEY             idx_business (business_type, business_key),
    KEY             idx_status (status),
    KEY             idx_starter (starter_id)
) ENGINE=InnoDB COMMENT='工作流实例表';