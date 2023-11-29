package com.www.orm.jdbctemplate.annotation;

import java.lang.annotation.*;

/**
 * @Description Column
 * @Author 张卫刚
 * @Date Created on 2023/11/28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PK {

    boolean auto() default true;
}
