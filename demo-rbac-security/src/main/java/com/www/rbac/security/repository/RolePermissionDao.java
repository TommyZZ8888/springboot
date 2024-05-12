package com.www.rbac.security.repository;

import com.www.rbac.security.domain.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description RolePermissionDao
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
public interface RolePermissionDao extends JpaRepository<RolePermission,Long>, JpaSpecificationExecutor<RolePermission> {
}
