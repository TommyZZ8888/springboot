package com.www.rbac.security.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @Description User
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
@Entity
@Table(name = "sec_user")
public class User {

    @Id
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 性别，男-1，女-2
     */
    private Integer sex;

    /**
     * 状态，启用-1，禁用-0
     */
    private Integer status;

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
