package com.www.zz.demojedis.model;


import lombok.Data;


@Data
public class GoodsCategory {


    private Long categoryId;

    private Byte categoryLevel;

    private Long parentId;

    private String categoryName;

    private Integer categoryRank;

    private Byte isDeleted;
}
