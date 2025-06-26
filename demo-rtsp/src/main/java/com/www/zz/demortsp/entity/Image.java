package com.www.zz.demortsp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * * @projectName videoservice
 * * @title   Image 
 * * @package  com.de.entity 
 * * @description  存放图片数据的实体
 * * @author IT_CREAT  
 * * @date 2020 2020/4/18 0018 下午 22:54 
 * * @version V1.0.0
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Image {
    private byte[] imageByte;
}
