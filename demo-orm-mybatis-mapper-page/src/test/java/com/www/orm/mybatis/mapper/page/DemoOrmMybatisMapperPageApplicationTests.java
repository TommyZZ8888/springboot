package com.www.orm.mybatis.mapper.page;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.www.orm.mybatis.mapper.page.entity.User;
import com.www.orm.mybatis.mapper.page.mapper.UserMapper;
import org.assertj.core.util.Lists;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class DemoOrmMybatisMapperPageApplicationTests {

    @Autowired
private UserMapper userMapper;

    /**
     * 测试通用Mapper - 保存
     */
    @Test
    public void testInsert() {
        String salt = IdUtil.fastSimpleUUID();
        User testSave3 = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com").phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).createTime(new DateTime()).lastUpdateTime(new DateTime()).build();
        userMapper.insertUseGeneratedKeys(testSave3);
        Assertions.assertNotNull(testSave3.getId());
    }

    /**
     * 测试通用Mapper - 批量保存
     */
    @Test
    public void testInsertList() {
        List<User> userList = Lists.newArrayList();
        for (int i = 4; i < 14; i++) {
            String salt = IdUtil.fastSimpleUUID();
            User user = User.builder().name("testSave" + i).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com").phoneNumber("1730000000" + i).status(1).lastLoginTime(new DateTime()).createTime(new DateTime()).lastUpdateTime(new DateTime()).build();
            userList.add(user);
        }
        int i = userMapper.insertList(userList);
        Assertions.assertEquals(userList.size(), i);
        List<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
    }

    /**
     * 测试通用Mapper - 删除
     */
    @Test
    public void testDelete() {
        Long primaryKey = 1L;
        int i = userMapper.deleteByPrimaryKey(primaryKey);
        Assertions.assertEquals(1, i);
        User user = userMapper.selectByPrimaryKey(primaryKey);
        Assertions.assertNull(user);
    }

    /**
     * 测试通用Mapper - 更新
     */
    @Test
    public void testUpdate() {
        Long primaryKey = 1L;
        User user = userMapper.selectByPrimaryKey(primaryKey);
        user.setName("通用Mapper名字更新");
        int i = userMapper.updateByPrimaryKeySelective(user);
        Assertions.assertEquals(1, i);
        User update = userMapper.selectByPrimaryKey(primaryKey);
        Assertions.assertNotNull(update);
        Assertions.assertEquals("通用Mapper名字更新", update.getName());
    }

    /**
     * 测试通用Mapper - 查询单个
     */
    @Test
    public void testQueryOne() {
        User user = userMapper.selectByPrimaryKey(1L);
        Assertions.assertNotNull(user);
    }

    /**
     * 测试通用Mapper - 查询全部
     */
    @Test
    public void testQueryAll() {
        List<User> users = userMapper.selectAll();
        Assertions.assertTrue(CollUtil.isNotEmpty(users));
    }

    /**
     * 测试分页助手 - 分页排序查询
     */
    @Test
    public void testQueryByPageAndSort() {
        initData();
        int currentPage = 1;
        int pageSize = 5;
        String orderBy = "id desc";
        int count = userMapper.selectCount(null);
        PageHelper.startPage(currentPage, pageSize, orderBy);
        List<User> users = userMapper.selectAll();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        Assertions.assertEquals(5, userPageInfo.getSize());
        Assertions.assertEquals(count, userPageInfo.getTotal());
    }

    /**
     * 测试通用Mapper - 条件查询
     */
    @Test
    public void testQueryByCondition() {
        initData();
        Example example = new Example(User.class);
        // 过滤
        example.createCriteria().andLike("name", "%Save1%").orEqualTo("phoneNumber", "17300000001");
        // 排序
        example.setOrderByClause("id desc");
        int count = userMapper.selectCountByExample(example);
        // 分页
        PageHelper.startPage(1, 3);
        // 查询
        List<User> userList = userMapper.selectByExample(example);
        PageInfo<User> userPageInfo = new PageInfo<>(userList);
        Assertions.assertEquals(3, userPageInfo.getSize());
        Assertions.assertEquals(count, userPageInfo.getTotal());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        testInsertList();
    }


}
