package com.www.task.quartz.common;

import org.quartz.SchedulerContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SchedulerContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 通过Class获取Bean
     *
     * @param requiredType class eg: Sample.class
     * @return bean
     */
    public static <T> T getBean(Class<T> requiredType) {
        checkApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new RuntimeException("applicationContext is null");
        }
    }
}
