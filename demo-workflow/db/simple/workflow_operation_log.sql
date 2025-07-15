CREATE TABLE workflow_operation_log
(
    id               INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    instance_id      INT         NOT NULL COMMENT '流程实例ID',
    task_id          INT COMMENT '关联的任务ID',
    operation_type   VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_desc   VARCHAR(255) COMMENT '操作描述',
    operator_id      INT         NOT NULL COMMENT '操作人ID',
    operation_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    operation_result VARCHAR(50) COMMENT '操作结果',
    comments         TEXT COMMENT '备注',
    details          JSON COMMENT '详细数据',
    FOREIGN KEY (instance_id) REFERENCES workflow_instance (id),
    FOREIGN KEY (task_id) REFERENCES task_instance (id),
    KEY              idx_instance_id (instance_id),
    KEY              idx_task_id (task_id),
    KEY              idx_operator (operator_id),
    KEY              idx_operation_time (operation_time)
) ENGINE=InnoDB COMMENT='工作流操作日志表';