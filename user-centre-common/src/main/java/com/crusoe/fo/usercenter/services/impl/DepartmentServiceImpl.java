package com.crusoe.fo.usercenter.services.impl;

import java.util.List;

import com.crusoe.fo.usercenter.entity.Department;
import com.crusoe.fo.usercenter.repository.DepartmentRepository;
import com.crusoe.fo.usercenter.services.IDepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DepartmentService")
public class DepartmentServiceImpl implements IDepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> findByParent(Long parentId) {
        // TODO Auto-generated method stub
        Department parent=departmentRepository.findById(parentId).get();
        return departmentRepository.findByParent(parent);

    }
    
}