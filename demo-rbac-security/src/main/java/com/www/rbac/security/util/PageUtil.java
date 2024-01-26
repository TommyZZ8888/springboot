package com.www.rbac.security.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.www.rbac.security.common.Consts;
import com.www.rbac.security.payload.PageCondition;
import org.springframework.data.domain.PageRequest;
/**
 * @Description PageUtil
 * @Author 张卫刚
 * @Date Created on 2024/1/16
 */
public class PageUtil {

    public static <T extends PageCondition> void checkPageCondition(T condition,Class<T> clazz) {
        if (ObjectUtil.isNull(condition)) {
            condition = ReflectUtil.newInstance(clazz);
        }
        // 校验分页参数
        if (ObjectUtil.isNull(condition.getCurrentPage())) {
            condition.setCurrentPage(Consts.DEFAULT_CURRENT_PAGE);
        }
        if (ObjectUtil.isNull(condition.getPageSize())) {
            condition.setPageSize(Consts.DEFAULT_PAGE_SIZE);
        }
    }

        /**
         * 根据分页参数构建{@link PageRequest}
         *
         * @param condition 查询参数
         * @param <T>       {@link PageCondition}
         * @return {@link PageRequest}
         */
        public static <T extends PageCondition> PageRequest ofPageRequest(T condition) {
            return PageRequest.of(condition.getCurrentPage(), condition.getPageSize());
        }

}
