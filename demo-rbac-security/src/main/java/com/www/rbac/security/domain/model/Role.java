package com.www.rbac.security.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @Description Role
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
@Entity
@Table(name = "sec_role")
public class Role {

    @Id
    private Long id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;



    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Long updateTime;
}
