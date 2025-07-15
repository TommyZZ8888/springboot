CREATE TABLE workflow_definition
(
    id          INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(100) NOT NULL COMMENT '工作流名称',
    description TEXT COMMENT '工作流描述',
    version     INT          NOT NULL DEFAULT 1 COMMENT '版本号',
    is_active   BOOLEAN               DEFAULT TRUE COMMENT '是否激活(1:激活,0:禁用)',
    created_by  INT COMMENT '创建人ID',
    created_at  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by  INT COMMENT '更新人ID',
    updated_at  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY         idx_name (name),
    KEY         idx_active (is_active)
) ENGINE=InnoDB COMMENT='工作流定义表';