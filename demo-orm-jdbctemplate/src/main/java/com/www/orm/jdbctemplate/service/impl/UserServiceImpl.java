package com.www.orm.jdbctemplate.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.www.orm.jdbctemplate.constant.Const;
import com.www.orm.jdbctemplate.dao.UserDao;
import com.www.orm.jdbctemplate.entity.User;
import com.www.orm.jdbctemplate.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description UserServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/11/29
 */
@Service
public class UserServiceImpl implements IUserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean save(User user) {
        String password = user.getPassword();
        String salt = IdUtil.simpleUUID();
        String pass = SecureUtil.md5(password + Const.SALT_PREFIX + salt);

        user.setPassword(pass);
        user.setSalt(salt);
        return userDao.insert(user) > 0;
    }

    @Override
    public Boolean delete(Long id) {
        return userDao.delete(id)>0;
    }

    @Override
    public Boolean update(User user, Long id) {
        User exist = getUser(id);
        if (StrUtil.isNotBlank(user.getPassword())) {
            String rawPass = user.getPassword();
            String salt = IdUtil.simpleUUID();
            String pass = SecureUtil.md5(rawPass + Const.SALT_PREFIX + salt);
            user.setPassword(pass);
            user.setSalt(salt);
        }
        BeanUtils.copyProperties(user,exist);
        exist.setLastUpdateTime(new DateTime());
        return userDao.update(exist, id) > 0;
    }

    @Override
    public User getUser(Long id) {
        return userDao.selectById(id);
    }

    @Override
    public List<User> getUser(User user) {
        return userDao.findByExample(user);
    }
}
