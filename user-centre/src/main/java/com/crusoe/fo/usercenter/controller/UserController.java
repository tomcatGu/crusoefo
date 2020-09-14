package com.crusoe.fo.usercenter.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.crusoe.fo.usercenter.dto.RoleDTO;
import com.crusoe.fo.usercenter.dto.UserDTO;
import com.crusoe.fo.usercenter.entity.Department;
import com.crusoe.fo.usercenter.entity.Role;
import com.crusoe.fo.usercenter.entity.User;
import com.crusoe.fo.usercenter.repository.DepartmentRepository;
import com.crusoe.fo.usercenter.repository.RoleRepository;
import com.crusoe.fo.usercenter.repository.UserRepository;
import com.crusoe.fo.usercenter.services.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoleRepository roleRepository;

    @PostMapping(value = "create")
    @ResponseBody
    public UserDTO create(@RequestBody UserDTO uDto) {
        Optional<Department> dept = departmentRepository.findById(uDto.getDepartment().getId());
        List<RoleDTO> roles=uDto.getRoles();
        User user =new User();
        user.setUsername(uDto.getUsername());
        user.setPassword(uDto.getPassword());
        user.setDepartment(dept.get());

        Iterator<RoleDTO> iter=roles.iterator();
        while(iter.hasNext()){
            Role role=roleRepository.findById(((RoleDTO)iter.next()).getId()).get();
            user.getRoleList().add(role);
        }
        user=userService.create(user);
        uDto.setId(user.getId());


        return uDto;
    }
    
}
