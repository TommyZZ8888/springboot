package com.www.task.quartz.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DFUtil {
    /**
     * 有网友指出，SimpleDateFormat 存在线程安全问题
     * 有三种方式可以解决这个问题
     * 1、用局部变量
     * 2、加锁
     * 3、用 ThreadLocal
     * 这里为了方便，就用局部变量的方式吧
     */
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String format(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
