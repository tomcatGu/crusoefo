package com.crusoe.fo.usercenter.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class DepartmentDTO {

    private Long id;

    private String name;


    private List<DepartmentDTO> children=new ArrayList<DepartmentDTO>();


    private List<UserDTO> userList=new ArrayList<UserDTO>();
}
