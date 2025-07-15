CREATE TABLE task_instance
(
    id           INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    instance_id  INT          NOT NULL COMMENT '流程实例ID',
    node_id      INT          NOT NULL COMMENT '节点定义ID',
    task_name    VARCHAR(100) NOT NULL COMMENT '任务名称',
    assignee_id  INT COMMENT '处理人ID',
    status       ENUM('pending', 'approved', 'rejected', 'completed', 'cancelled') NOT NULL DEFAULT 'pending' COMMENT '状态',
    start_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time     TIMESTAMP NULL COMMENT '完成时间',
    due_time     TIMESTAMP NULL COMMENT '截止时间',
    comments     TEXT COMMENT '审批意见',
    form_data    JSON COMMENT '表单数据',
    operation_by INT COMMENT '操作人ID',
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (instance_id) REFERENCES workflow_instance (id) ON DELETE CASCADE,
    FOREIGN KEY (node_id) REFERENCES workflow_node (id),
    KEY          idx_instance_id (instance_id),
    KEY          idx_node_id (node_id),
    KEY          idx_assignee (assignee_id),
    KEY          idx_status (status)
) ENGINE=InnoDB COMMENT='任务实例表';