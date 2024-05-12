package com.www.rbac.security.domain.vo;

/**
 * @Description JwtResponse
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
public class JwtResponse {
    /**
     * token字段
     */
    private String token;

    /**
     * token类型
     */
    private String tokenType = "Bearer";

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}
