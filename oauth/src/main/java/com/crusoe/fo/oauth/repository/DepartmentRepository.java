package com.crusoe.fo.oauth.repository;

import com.crusoe.fo.oauth.entity.Department;
import com.crusoe.fo.oauth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    public Department findByName(String deptName);
}