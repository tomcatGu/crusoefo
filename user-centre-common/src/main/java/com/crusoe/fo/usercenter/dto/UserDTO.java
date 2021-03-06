package com.crusoe.fo.usercenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable{
    private Long id;
	private String username;
	private String name;
	private String password;
	private Long ordinal;
	private boolean enabled = true;
    private List<RoleDTO> roles = new ArrayList<RoleDTO>();
    private DepartmentDTO department;


}
