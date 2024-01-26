package com.www.rbac.security.service;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.www.rbac.security.common.Consts;
import com.www.rbac.security.common.PageResult;
import com.www.rbac.security.domain.model.User;
import com.www.rbac.security.domain.vo.OnlineUser;
import com.www.rbac.security.payload.PageCondition;
import com.www.rbac.security.repository.UserDao;
import com.www.rbac.security.util.RedisUtil;
import com.www.rbac.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description MonitorService
 * @Author 张卫刚
 * @Date Created on 2024/1/17
 */
@Service
@Slf4j
public class MonitorService {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserDao userDao;

    public PageResult<OnlineUser> onlineUser(PageCondition pageCondition) {
        PageResult<String> keys = redisUtil.findKeysForPage(Consts.REDIS_JWT_KEY_PREFIX + Consts.SYMBOL_STAR, pageCondition.getCurrentPage(), pageCondition.getPageSize());

        List<String> rows = keys.getRows();
        Long total = keys.getTotal();

        //根据redis中键获取用户名列表
        List<String> usernameList = rows.stream().map(s -> StrUtil.subAfter(s, Consts.REDIS_JWT_KEY_PREFIX, true)).collect(Collectors.toList());
        //根据用户名列表查询用户信息
        List<User> userList = userDao.findByUsernameIn(usernameList);

        List<OnlineUser> onlineUserList = Lists.newArrayList();
        userList.forEach(user -> onlineUserList.add(OnlineUser.create(user)));

        return new PageResult<>(onlineUserList, total);
    }


    public void kickOut(List<String> names){
        List<String> redisKeys = names.stream().map(s -> Consts.REDIS_JWT_KEY_PREFIX + s).collect(Collectors.toList());
        redisUtil.delete(redisKeys);

        String currentUserName = SecurityUtil.getCurrentUserName();
        names.forEach(name->{
            // TODO: 通知被踢出的用户已被当前登录用户踢出，
            //  后期考虑使用 websocket 实现，具体伪代码实现如下。
            //  String message = "您已被用户【" + currentUsername + "】手动下线！";
            log.debug("用户【{}】被用户【{}】手动下线！", name, currentUserName);
        });
    }
}
