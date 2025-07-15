CREATE TABLE user
(
    id            INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username      VARCHAR(50)  NOT NULL COMMENT '用户名',
    password      VARCHAR(100) NOT NULL COMMENT '密码(加密)',
    name          VARCHAR(100) NOT NULL COMMENT '姓名',
    email         VARCHAR(100) COMMENT '邮箱',
    phone         VARCHAR(20) COMMENT '电话',
    department_id INT COMMENT '部门ID',
    position      VARCHAR(50) COMMENT '职位',
    is_active     BOOLEAN   DEFAULT TRUE COMMENT '是否激活',
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_username (username),
    KEY           idx_department (department_id)
) ENGINE=InnoDB COMMENT='用户表';