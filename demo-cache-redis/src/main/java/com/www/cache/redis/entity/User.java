package com.www.cache.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description User
 * @Author 张卫刚
 * @Date Created on 2023/12/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 2892248514883451461L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
}

