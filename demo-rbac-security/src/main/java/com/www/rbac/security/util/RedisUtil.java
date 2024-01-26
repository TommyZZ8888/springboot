package com.www.rbac.security.util;

import com.google.common.collect.Lists;
import com.www.rbac.security.common.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Description RedisUtil
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public PageResult<String> findKeysForPage(String patternKey, int currentPage, int pageSize) {
        ScanOptions options = ScanOptions.scanOptions().match(patternKey).build();
        RedisConnectionFactory factory = stringRedisTemplate.getConnectionFactory();
        RedisConnection connection = factory.getConnection();
        Cursor<byte[]> cursor = connection.scan(options);

        List<String> result = Lists.newArrayList();

        long tmpIndex = 0;
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = currentPage * pageSize;
        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            if (tmpIndex >= startIndex && tmpIndex < endIndex) {
                result.add(key);
            }
            tmpIndex++;
        }

        try {
            cursor.close();
            RedisConnectionUtils.releaseConnection(connection, factory);
        } catch (Exception e) {
            log.warn("redisclose error, ", e);
        }

        return new PageResult<>(result, tmpIndex);
    }


    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }


    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

}
