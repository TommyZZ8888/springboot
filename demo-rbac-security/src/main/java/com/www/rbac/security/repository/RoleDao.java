package com.www.rbac.security.repository;

import com.www.rbac.security.domain.model.Role;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Description RoleDao
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
public interface RoleDao extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    @Query(value = "select sec_role.* from sec_role,sec_user,sec_user_role where sec_user.id = sec_user_role.user_id and sec_role.id = sec_user_role.role_id and sec_user.id = :userId", nativeQuery = true)
    List<Role> selectByUserId(@Param("userId") Long userId);

}
