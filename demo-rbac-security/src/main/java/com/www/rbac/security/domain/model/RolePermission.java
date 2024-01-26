package com.www.rbac.security.domain.model;

import com.www.rbac.security.domain.model.unionkey.RolePermissionKey;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * @Description RolePermission
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
@Entity
@Table(name = "sec_role_permission")
public class RolePermission {

    @EmbeddedId
    private RolePermissionKey id;
}
