package com.crusoe.fo.usercenter.services.impl;

import com.crusoe.fo.usercenter.entity.Role;
import com.crusoe.fo.usercenter.repository.RoleRepository;
import com.crusoe.fo.usercenter.services.IRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("RoleService")
public class RoleServiceImpl implements IRoleService{
    @Autowired
    RoleRepository roleRepository;
    public Role create(Role role){
        return roleRepository.save(role);
    }

}