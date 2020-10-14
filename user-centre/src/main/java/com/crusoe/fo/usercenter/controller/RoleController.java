package com.crusoe.fo.usercenter.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.crusoe.fo.usercenter.dto.RoleDTO;
import com.crusoe.fo.usercenter.entity.Role;
import com.crusoe.fo.usercenter.services.IRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/role")
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
    @GetMapping(value="getAllRoles")
    @ResponseBody
    public List<RoleDTO> getAllRoles(){
        List<RoleDTO> roles=new ArrayList<RoleDTO>();
        Iterator iter=roleService.findAll().iterator();
        while(iter.hasNext()){
            Role r= (Role) iter.next();
            RoleDTO rDTO=new RoleDTO();
            rDTO.setId(r.getId());
            rDTO.setRolename(r.getRolename());
            rDTO.setOrdinal(r.getOrdinal());
            roles.add(rDTO);

        }
        return roles;
        
    }
    
}