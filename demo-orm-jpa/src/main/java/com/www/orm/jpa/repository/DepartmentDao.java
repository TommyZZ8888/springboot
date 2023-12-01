package com.www.orm.jpa.repository;

import com.www.orm.jpa.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description DepartmentDao
 * @Author 张卫刚
 * @Date Created on 2023/11/30
 */
@Repository
public interface DepartmentDao extends JpaRepository<Department,Long> {


List<Department> findDepartmentsByLevels(Integer levels);
}
