package com.www.rbac.security.config;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.www.rbac.security.common.Consts;
import com.www.rbac.security.common.Status;
import com.www.rbac.security.domain.model.Permission;
import com.www.rbac.security.domain.model.Role;
import com.www.rbac.security.domain.vo.UserPrincipal;
import com.www.rbac.security.repository.PermissionDao;
import com.www.rbac.security.repository.RoleDao;
import jakarta.servlet.http.HttpServletRequest;
import org.antlr.v4.runtime.misc.MultiMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Description RbacAuthorityService
 * @Author 张卫刚
 * @Date Created on 2024/1/25
 */
@Component
public class RbacAuthorityService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RequestMappingHandlerMapping mapping;


    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        checkRequest(request);

        Object userInfo = authentication.getPrincipal();
        boolean hasPermission = false;

        if (userInfo instanceof UserDetails) {
            UserPrincipal principal = (UserPrincipal) userInfo;
            Long userId = principal.getId();

            List<Role> roles = roleDao.selectByUserId(userId);
            List<Long> roleIds = roles.stream().map(Role::getId).toList();
            List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);

            //获取资源 前后端分离，所以过滤页面权限，只保留按钮
            List<Permission> btnPerms = permissions.stream()
                    // 过滤页面权限
                    .filter(permission -> Objects.equals(permission.getType(), Consts.BUTTON))
                    // 过滤 url 为空
                    .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
                    // 过滤 method 为空
                    .filter(permission -> StrUtil.isNotBlank(permission.getMethod())).toList();

            for (Permission btnPerm : btnPerms) {
                AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher(btnPerm.getUrl(), btnPerm.getMethod());
                if (antPathRequestMatcher.matches(request)) {
                    hasPermission = true;
                    break;
                }
            }
            return hasPermission;
        } else {
            return false;
        }
    }



    /**
     * 检验请求是否存在
     *
     * @param request 请求
     */
    private void checkRequest(HttpServletRequest request) {
        String currentMethod = request.getMethod();
        Multimap<String, String> urlMapping = allUrlMapping();

        for (String uri : urlMapping.keySet()) {
            //通过 AntPathRequestMatcher 匹配 url
            //可以通过 2种方式创建 AntPathRequestMatcher
            //1. new AntPathRequestMatcher(uri,method) 这种方式可以直接判断方法是否匹配，因为这里我们把 方法不匹配 自定义抛出，所以，我们使用第二种方式创建
            //2.new AntPathRequestMatcher(uri) 这种方式不检验请求方法，只检验请求路径
            AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
            if (antPathMatcher.matches(request)) {
                if (!urlMapping.get(uri).contains(currentMethod)) {
                    throw new SecurityException(Status.HTTP_BAD_METHOD.getMessage());
                } else {
                    return;
                }
            }
        }
        throw new SecurityException(Status.REQUEST_NOT_FOUND.getMessage());
    }

    /**
     * 获取 所有URL Mapping，返回格式为{"/test":["GET","POST"],"/sys":["GET","DELETE"]}
     *
     * @return {@link ArrayListMultimap} 格式的 URL Mapping
     */
    private Multimap<String, String> allUrlMapping() {
        Multimap<String, String> urlMapping = ArrayListMultimap.create();

        //获取url类和方法的对应关系
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        handlerMethods.forEach((k, v) -> {
            Set<String> url = k.getPatternsCondition().getPatterns();
            RequestMethodsRequestCondition method = k.getMethodsCondition();

            url.forEach(s -> urlMapping.putAll(s, method.getMethods().stream().map(Enum::toString).collect(Collectors.toList())));
        });

        return urlMapping;
    }

}
