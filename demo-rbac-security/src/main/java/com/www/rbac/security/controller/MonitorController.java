package com.www.rbac.security.controller;

import cn.hutool.core.collection.CollUtil;
import com.www.rbac.security.common.ApiResponse;
import com.www.rbac.security.common.PageResult;
import com.www.rbac.security.common.Status;
import com.www.rbac.security.domain.vo.OnlineUser;
import com.www.rbac.security.payload.PageCondition;
import com.www.rbac.security.service.MonitorService;
import com.www.rbac.security.util.PageUtil;
import com.www.rbac.security.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 监控 Controller，在线用户，手动踢出用户等功能
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2018-12-11 20:55
 */
@Slf4j
@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    /**
     * 在线用户列表
     *
     * @param pageCondition 分页参数
     */
    @GetMapping("/online/user")
    public ApiResponse onlineUser(PageCondition pageCondition) {
        PageUtil.checkPageCondition(pageCondition, PageCondition.class);
        PageResult<OnlineUser> pageResult = monitorService.onlineUser(pageCondition);
        return ApiResponse.ofSuccess(pageResult);
    }

    /**
     * 批量踢出在线用户
     *
     * @param names 用户名列表
     */
    @DeleteMapping("/online/user/kickout")
    public ApiResponse kickoutOnlineUser(@RequestBody List<String> names) {
        if (CollUtil.isEmpty(names)) {
            throw new SecurityException(Status.PARAM_NOT_NULL.getMessage());
        }
        if (names.contains(SecurityUtil.getCurrentUserName())) {
            throw new SecurityException(Status.KICKOUT_SELF.getMessage());
        }
        monitorService.kickOut(names);
        return ApiResponse.ofSuccess();
    }
}
