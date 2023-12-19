package com.www.task.quartz.util;

import com.www.task.quartz.job.BaseJob;

import java.lang.reflect.InvocationTargetException;

/**
 * @Description JobUtil
 * @Author 张卫刚
 * @Date Created on 2023/12/19
 */
public class JobUtil {

    public static BaseJob getClass(String className) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName(className);
        return (BaseJob) aClass.getDeclaredConstructor().newInstance();
    }
}
