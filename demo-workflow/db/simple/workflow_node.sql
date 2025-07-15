CREATE TABLE workflow_node
(
    id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    workflow_id INT          NOT NULL COMMENT '所属工作流ID',
    node_key    VARCHAR(50)  NOT NULL COMMENT '节点唯一标识',
    node_name   VARCHAR(100) NOT NULL COMMENT '节点名称',
    node_type   ENUM('start', 'end', 'task', 'approval', 'gateway') NOT NULL COMMENT '节点类型',
    description TEXT COMMENT '节点描述',
    position_x  INT COMMENT '节点在流程图中的X坐标',
    position_y  INT COMMENT '节点在流程图中的Y坐标',
    config      JSON COMMENT '节点配置(JSON格式)',
    sort_order  INT       DEFAULT 0 COMMENT '排序序号',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (workflow_id) REFERENCES workflow_definition (id) ON DELETE CASCADE,
    UNIQUE KEY uk_workflow_node (workflow_id, node_key),
    KEY         idx_workflow_id (workflow_id),
    KEY         idx_node_type (node_type)
) ENGINE=InnoDB COMMENT='工作流节点表';