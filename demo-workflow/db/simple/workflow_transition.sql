CREATE TABLE workflow_transition
(
    id                   INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    workflow_id          INT NOT NULL COMMENT '所属工作流ID',
    from_node_id         INT NOT NULL COMMENT '起始节点ID',
    to_node_id           INT NOT NULL COMMENT '目标节点ID',
    name                 VARCHAR(100) COMMENT '流转名称',
    condition_expression VARCHAR(500) COMMENT '条件表达式',
    priority             INT       DEFAULT 0 COMMENT '优先级',
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (workflow_id) REFERENCES workflow_definition (id) ON DELETE CASCADE,
    FOREIGN KEY (from_node_id) REFERENCES workflow_node (id) ON DELETE CASCADE,
    FOREIGN KEY (to_node_id) REFERENCES workflow_node (id) ON DELETE CASCADE,
    KEY                  idx_workflow_id (workflow_id),
    KEY                  idx_from_node (from_node_id),
    KEY                  idx_to_node (to_node_id)
) ENGINE=InnoDB COMMENT='工作流流转表';