package com.www.orm.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.List;

/**
 * @Author jiuping.zhou
 * @Date 2023/10/9 下午4:28
 */
@Data
@TableName(value = "violation", autoResultMap = true)
public class ViolationEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orgId;
    @TableField(fill = FieldFill.INSERT)
    private Long createdAt;
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer isDeleted;

    private String refNumber;
    private String unifiedNumber;

    /**
     * 转移前原始单号
     */
    private String rawRefNumber;
    /**
     * 转移给了谁
     */
    private String transferedRefNumber;
    // 举报人
    private String fullName;
    private String nationality;
    private String source;
    private String sourceOther;
    private String mobile;
    private String complainantRole;
    private String email;
    private String eid;
    //违规信息
    private String emirate;
    private String emirateEntity;
    private String emirateEntityOther;
    private String violationType;
    private String violationTypeDesc;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> violationBoxType;
    //TODO 格式?
    private String violationDate;
    private String violationDesc;
    private String violationAdditionalDetails;
    /**
     * 是否提交给过其他机构
     */
    private boolean hasReported;
    private String refEntity;
    private String refRefNumber;
    private String refMonth;
    private String refYear;


    private String status;
    private String actionTaken;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> assignedPerson;
    private String assignedDate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updatedAt;

    private String platformSource;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> violationTypeA;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> violationTypeB;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> violationTypeBBox;
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> violationInternalBoxType;
    private Long createdBy;
    private String incidentDiscoveredDate;
    private Long linkGroup;

    private Long initiatedTimestamp;

    private Integer isFsAction;

    private int adminIsRead;
    private int entityIsRead;
    private int isForbid;
    private String publicRefNumber;
}
