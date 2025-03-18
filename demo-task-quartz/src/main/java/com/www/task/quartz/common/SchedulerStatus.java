package com.www.task.quartz.common;

/**
 * @Describtion: SchedulerStatus
 * @Author: 张卫刚
 * @Date: 2025/3/18 9:35
 */
public enum SchedulerStatus {
    // 开启
    ENABLE("1", "开启"),
    // 暂停
    DISABLE("2", "暂停"),
    // 删除
    DELETE("0", "删除");

    final String code;
    final String message;

    SchedulerStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
