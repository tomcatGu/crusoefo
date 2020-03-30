package com.crusoe.fo.usercenter.dto;

import java.util.List;

import lombok.Data;

@Data
public class RoleDTO {
    private long id;
    private String rolename;
    private List<UserDTO> userList;
}
