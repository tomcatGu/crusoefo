package com.crusoe.fo.usercenter.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RoleDTO implements Serializable {
    private Long id;
    private String rolename;
    private Long ordinal;
    private List<UserDTO> userList;

}
