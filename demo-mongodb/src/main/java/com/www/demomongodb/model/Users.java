package com.www.demomongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Describtion: Users
 * @Author: 张卫刚
 * @Date: 2024/8/4 10:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    private String id;

    private String name;

    private Integer age;

    private String status;
}
