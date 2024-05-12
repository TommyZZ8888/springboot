package com.www.rbac.security.config;

import com.www.rbac.security.common.Status;
import com.www.rbac.security.util.ResponseUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @Description SecurityHandlerConfig
 * @Author 张卫刚
 * @Date Created on 2024/1/24
 */
@Configuration
public class SecurityHandlerConfig {

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> ResponseUtil.readerJson(response, Status.ACCESS_DENIED, null);
    }

}
