package com.www.rbac.security.service;

import com.www.rbac.security.domain.model.Permission;
import com.www.rbac.security.domain.model.Role;
import com.www.rbac.security.domain.model.User;
import com.www.rbac.security.domain.vo.UserPrincipal;
import com.www.rbac.security.repository.PermissionDao;
import com.www.rbac.security.repository.RoleDao;
import com.www.rbac.security.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description CustomUserDetailsService
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsernameOrEmailOrPhone(username, username, username).orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        List<Role> roles = roleDao.selectByUserId(user.getId());
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);
        return UserPrincipal.create(user, roles, permissions);
    }
}
