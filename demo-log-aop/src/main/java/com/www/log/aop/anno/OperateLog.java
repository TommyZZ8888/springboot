package com.www.log.aop.anno;

import java.lang.annotation.*;

/**
 * @Description OperateLog
 * @Author 张卫刚
 * @Date Created on 2023/11/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
@Documented
public @interface OperateLog {

}
