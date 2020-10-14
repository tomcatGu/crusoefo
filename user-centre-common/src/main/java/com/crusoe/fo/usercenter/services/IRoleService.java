package com.crusoe.fo.usercenter.services;

import java.util.List;

import com.crusoe.fo.usercenter.entity.Role;

public interface IRoleService {
    public Role create(Role role);
    public List<Role> findAll();
}
