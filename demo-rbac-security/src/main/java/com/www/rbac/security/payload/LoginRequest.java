package com.www.rbac.security.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @Description LoginRequest
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@Data
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String usernameOrEmailOrPhone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 记住我
     */
    private Boolean rememberMe = false;
}
