package com.www.rbac.security.payload;

import lombok.Data;

/**
 * @Description PageCondition
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@Data
public class PageCondition {

    /**
     * 当前页码
     */
    private Integer currentPage;

    /**
     * 每页条数
     */
    private Integer pageSize;

}
