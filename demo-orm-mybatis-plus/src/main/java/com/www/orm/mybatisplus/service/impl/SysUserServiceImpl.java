package com.www.orm.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.orm.mybatisplus.entity.SysUserEntity;
import com.www.orm.mybatisplus.entity.ViolationEntity;
import com.www.orm.mybatisplus.mapper.SysUserMapper;
import com.www.orm.mybatisplus.mapper.ViolationMapper;
import com.www.orm.mybatisplus.service.SysUserService;
import com.www.orm.mybatisplus.service.ViolationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.www.orm.mybatisplus.config.ComplainantEnums.*;

/**
 * @Description UserServiceImpl
 * @Author 张卫刚
 * @Date Created on 2023/12/6
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {


}
