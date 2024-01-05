package com.www.file.download.easyexcel.test;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TestExcel
 * @Author 张卫刚
 * @Date Created on 2024/1/5
 */


@Data
@TableName("test_excel")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TestExcel {

    /**
     * 可以通过index和name去匹配excel里的列
     * 注意name,index值不要重复
     */
    @TableId
    private String userId;

    @ExcelProperty(value = "姓名",index = 0)
    private String userName;

    @ExcelProperty(value = "年龄",index = 1)
    private String userAge;

    @ExcelProperty(value = "身份证号",index = 2)
    private String userCardid;


}
