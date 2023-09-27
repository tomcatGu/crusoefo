package com.crusoe.fo.usercenter.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import javax.websocket.server.PathParam;

import com.crusoe.fo.usercenter.dto.DepartmentDTO;
import com.crusoe.fo.usercenter.entity.Department;
import com.crusoe.fo.usercenter.repository.DepartmentRepository;
import com.crusoe.fo.usercenter.services.IDepartmentService;
import com.crusoe.fo.usercenter.services.impl.DepartmentServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "department")
public class DepartmentController {
    @Autowired
    IDepartmentService departmentService;

    @GetMapping(value = "getDepartments/{parentId}")
    @ResponseBody
    public List<DepartmentDTO> getDepartments(@PathVariable("parentId") Long parentId) {

        ArrayList<DepartmentDTO> deptDTOs=new ArrayList<DepartmentDTO>();
        Iterator iter=departmentService.findByParent(parentId).iterator();
        while(iter.hasNext()){
            Department dept= (Department) iter.next();
            DepartmentDTO deptDTO=new DepartmentDTO();
            deptDTO.setId(dept.getId());
            deptDTO.setName(dept.getName());
            deptDTO.setOrdinal(dept.getOrdinal());
            deptDTOs.add(deptDTO);

        }
       
        return deptDTOs;
        
    }
    
}