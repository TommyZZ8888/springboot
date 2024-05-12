package com.www.rbac.security.repository;

import com.www.rbac.security.domain.model.Permission;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description PermissionDao
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
public interface PermissionDao extends JpaRepository<Permission,Long>, JpaSpecificationExecutor<Permission> {


    @Query(value = "select distinct sec_permission.* from sec_permission,sec_role,sec_role_permission where sec_role.id =sec_role_permission.role_id and sec_permission.id = sec_role_permission.permission_id and sec_role.id in (:ids)",nativeQuery = true)
List<Permission> selectByRoleIdList(@Param("ids") List<Long> ids);

}
