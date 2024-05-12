package com.www.rbac.security.repository;

import com.www.rbac.security.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Description UserRoleDao
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
public interface UserRoleDao extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
}
