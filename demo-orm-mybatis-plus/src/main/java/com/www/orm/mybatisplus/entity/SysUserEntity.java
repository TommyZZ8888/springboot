package com.www.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orgId;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDeleted;
    private String name;
    private String account;
    private String email;
    private String phone;
    private String password;
    private String status;
   // private Long orgId;
}
