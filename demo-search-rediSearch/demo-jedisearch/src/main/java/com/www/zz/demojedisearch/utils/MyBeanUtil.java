package com.www.zz.demojedisearch.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Slf4j
public class MyBeanUtil {

    public static Object copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null) {
            return target;
        }
        BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

    public static <T> List<T> copyList(List sources, Class<T> clazz) {
        return copyList(sources, clazz, null);
    }

    public static <T> List<T> copyList(List sources, Class<T> clazz, Callback<T> callback) {
        List<T> targetList = new ArrayList<>();
        if (sources != null) {
            try {
                for (Object source : sources) {
                    T target = clazz.getDeclaredConstructor().newInstance();
                    copyProperties(source, target);
                    if (callback != null) {
                        callback.set(source, target);
                    }
                    targetList.add(target);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return targetList;
    }

    public static Map<String, String> toMap(Object bean, String... ignoreProperties) {
        Map<String, String> map = new LinkedHashMap<>();
        List<String> ignoreList = new ArrayList<>(Arrays.asList(ignoreProperties));
        ignoreList.add("class");
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
        for (PropertyDescriptor pd : beanWrapper.getPropertyDescriptors()) {
            if (!ignoreList.contains(pd.getName()) && beanWrapper.isReadableProperty(pd.getName())) {
                Object propertyValue = beanWrapper.getPropertyValue(pd.getName());
                map.put(pd.getName(), String.valueOf(propertyValue));
            }
        }
        return map;
    }


    public static <T> T toBean(Map<String, Object> map, Class<T> beanType) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(beanType);
        map.forEach((key, value) -> {
            if (beanWrapper.isWritableProperty(key)) {
                beanWrapper.setPropertyValue(key, value);
            }
        });
        return (T) beanWrapper.getWrappedInstance();
    }

    public static <T> T propertiesToBean(Iterable<Map.Entry<String, Object>> properties, Class<T> beanType) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(beanType);
        properties.forEach(map -> {
            if (beanWrapper.isWritableProperty(map.getKey())) {
                beanWrapper.setPropertyValue(map.getKey(), map.getValue());
            }
        });

        return (T) beanWrapper.getWrappedInstance();
    }

    public interface Callback<T> {
        void set(Object source, T target);
    }

    // 检查Pojo对象是否有null字段
    public static boolean checkPojoNullField(Object o, Class<?> clz) {
        try {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(o) == null) {
                    return false;
                }
            }
            if (clz.getSuperclass() != Object.class) {
                return checkPojoNullField(o, clz.getSuperclass());
            }
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
