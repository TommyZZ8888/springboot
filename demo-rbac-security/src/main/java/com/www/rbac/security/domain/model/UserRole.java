package com.www.rbac.security.domain.model;

import com.www.rbac.security.domain.model.unionkey.UserRoleKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @Description UserRole
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
@Entity
@Table(name = "sec_user_role")
public class UserRole {

    @EmbeddedId
    private UserRoleKey id;
}
