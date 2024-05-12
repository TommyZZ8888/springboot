package com.www.rbac.security.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.www.rbac.security.service.CustomUserDetailsService;
import com.www.rbac.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpMethod.*;

/**
 * @Description JwtAuthenticationFilter
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomConfig customConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (checkIgnores(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = jwtUtil.getJwtFromRequest(request);
        if (StrUtil.isNotBlank(jwt)) {
            String username = jwtUtil.getUserNameFromJwt(jwt);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }

    private boolean checkIgnores(HttpServletRequest request) {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if (ObjectUtil.isNull(httpMethod)) {
            httpMethod = HttpMethod.GET;
        }

        Set<String> ignores = Sets.newHashSet();

        switch (httpMethod.name()) {
            case "GET":
                ignores.addAll(customConfig.getIgnores().getGet());
                break;
            case "PUT":
                ignores.addAll(customConfig.getIgnores().getPut());
                break;
            case "HEAD":
                ignores.addAll(customConfig.getIgnores().getHead());
                break;
            case "POST":
                ignores.addAll(customConfig.getIgnores().getPost());
                break;
            case "PATCH":
                ignores.addAll(customConfig.getIgnores().getPatch());
                break;
            case "TRACE":
                ignores.addAll(customConfig.getIgnores().getTrace());
                break;
            case "DELETE":
                ignores.addAll(customConfig.getIgnores().getDelete());
                break;
            case "OPTIONS":
                ignores.addAll(customConfig.getIgnores().getOptions());
                break;
            default:
                break;
        }

        ignores.addAll(customConfig.getIgnores().getPattern());

        if (CollUtil.isNotEmpty(ignores)) {
            for (String ignore : ignores) {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
                if (matcher.matches(request)) {
                    return true;
                }
            }
        }
        return false;
    }
}
