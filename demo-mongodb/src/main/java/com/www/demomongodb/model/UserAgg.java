package com.www.demomongodb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Describtion: Users
 * @Author: 张卫刚
 * @Date: 2024/8/4 10:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAgg {

    private String name;

    private Integer age;

    private Integer netPricePlus1;
    private Integer netPriceMinus1;

    private Integer grossPrice;
    private Integer netPriceDiv2;

}
