package com.crusoe.fo.usercenter.repository;

import java.util.List;

import com.crusoe.fo.usercenter.entity.Department;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    public Department findByName(String deptName);
    public List<Department> findByParent(Department parent);
}