package com.www.rbac.security.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.www.rbac.security.common.Consts;
import com.www.rbac.security.domain.model.User;
import lombok.Data;

/**
 * @Description OnlineUser
 * @Author 张卫刚
 * @Date Created on 2024/1/15
 */
@Data
public class OnlineUser {


    private Long id;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private Long birthday;

    private Integer sex;

    public static OnlineUser create(User user) {
        OnlineUser onlineUser = new OnlineUser();
        BeanUtil.copyProperties(user, onlineUser);

        // 脱敏
        onlineUser.setPhone(StrUtil.hide(user.getPhone(), 3, 7));
        onlineUser.setEmail(StrUtil.hide(user.getEmail(), 1, StrUtil.indexOfIgnoreCase(user.getEmail(), Consts.SYMBOL_EMAIL)));
        return onlineUser;
    }
}
