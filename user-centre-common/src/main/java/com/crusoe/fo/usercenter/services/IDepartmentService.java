package com.crusoe.fo.usercenter.services;

import java.util.List;

import com.crusoe.fo.usercenter.entity.Department;

public interface IDepartmentService {
public List<Department> findByParent(Long parentId);
}
