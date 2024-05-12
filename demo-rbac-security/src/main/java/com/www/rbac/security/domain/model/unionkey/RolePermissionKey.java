package com.www.rbac.security.domain.model.unionkey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description RolePermissionKey
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
@Embeddable
public class RolePermissionKey implements Serializable {
    private static final long serialVersionUID = 6850974328279713855L;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private Long permissionId;
}