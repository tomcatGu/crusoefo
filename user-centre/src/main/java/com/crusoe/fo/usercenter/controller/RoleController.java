package com.crusoe.fo.usercenter.controller;

import com.crusoe.fo.usercenter.dto.RoleDTO;
import com.crusoe.fo.usercenter.entity.Role;
import com.crusoe.fo.usercenter.services.IRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/role")
public class RoleController {
    @Autowired
    IRoleService roleService;
    @PostMapping(value="create")
    @ResponseBody
    public RoleDTO create(@RequestBody RoleDTO rdto){
        Role role=new Role();
        role.setRolename(rdto.getRolename());
        role=roleService.create(role);
        rdto.setId(role.getId());
        return rdto;
    }
    
}