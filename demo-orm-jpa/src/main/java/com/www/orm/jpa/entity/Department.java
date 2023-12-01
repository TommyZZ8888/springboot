package com.www.orm.jpa.entity;

import com.www.orm.jpa.entity.base.AbstractAuditModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

/**
 * @Description Department
 * @Author 张卫刚
 * @Date Created on 2023/11/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orm_department")
@ToString(callSuper = true)
public class Department extends AbstractAuditModel {

    @Column(name = "name",columnDefinition = "varchar(255) not null")
    private String name;

    @ManyToOne(cascade = {CascadeType.REFRESH},optional = true)
    @JoinColumn(name = "superior",referencedColumnName = "id")
    private Department superior;

    @Column(name = "levels",columnDefinition = "int(11) not null default 0")
    private Integer levels;

    @Column(name = "order_no",columnDefinition = "int(11) not null default 0")
    private Integer orderNo;

    @OneToMany(cascade = {CascadeType.REFRESH,CascadeType.REMOVE},fetch = FetchType.EAGER,mappedBy = "superior")
    private Collection<Department> children;

    @ManyToMany(mappedBy = "departmentList")
    private Collection<User> userList;


}
