package com.www.zz.demojedis.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class GoodsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2420217592493206057L;

    private Long goodsId;

    private String goodsName;

    private String goodsIntro;

    private Long goodsCategoryId;

    private String goodsCoverImg;

    private String goodsCarousel;

    private Integer originalPrice;

    private Integer sellingPrice;

    private Integer stockNum;

    private String tag;

    private Byte goodsSellStatus;

    private String goodsDetailContent;

    private Integer configRank;
}
